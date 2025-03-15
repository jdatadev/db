package dev.jdata.db.engine.database;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.data.indices.Indices;
import dev.jdata.db.engine.database.Database.DatabaseState;
import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.engine.sessions.Session;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.engine.sessions.Sessions;
import dev.jdata.db.engine.statements.SQLStatements;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.engine.transactions.Transactions;
import dev.jdata.db.schema.Column;
import dev.jdata.db.schema.DatabaseSchema;
import dev.jdata.db.schema.DatabaseSchemas;
import dev.jdata.db.schema.Table;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.dml.SQLDMLUpdatingStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLSelectStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.sql.parse.SQLParser.SQLString;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.allocators.ObjectCache;
import dev.jdata.db.utils.checks.Checks;

public final class Database extends BaseDescriptorable<DatabaseState> implements IDatabaseOperations, IDatabase {

    public interface DMLEvaluatorParameterAllocator {

        DMLUpdatingEvaluatorParameter allocateDMLEvaluatorParameter();

        void freeDMLEvaluatorParameter(DMLUpdatingEvaluatorParameter evaluatorParameter);
    }

    public interface DMLPreparedStatementEvaluatorParameterAllocator {

        DMLUpdatingPreparedEvaluatorParameter allocateDMLPreparedStatementEvaluatorParameter();

        void freeDMLPreparedStatementEvaluatorParameter(DMLUpdatingPreparedEvaluatorParameter evaluatorParameter);
    }

    static enum DatabaseState implements State {

        CREATED(true);

        private final boolean initializable;

        private DatabaseState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    private final SQLStatementAdapter<StringResolver, Void, SQLValidationException> ddlStatementVisitor = new SQLStatementAdapter<StringResolver, Void, SQLValidationException>() {

        @Override
        public Void onCreateTable(SQLCreateTableStatement createTableStatement, StringResolver parameter) throws TableAlreadyExistsException {

            Database.this.processCreateTable(createTableStatement, parameter);

            return null;
        }
    };

    private final String name;
    private final IDatabasesAllocators allocators;
    private final StringManagement stringManagement;
    private final DatabaseSchemas schemas;
    private final DataCache dataCache;
//    private final TableDataStorageBackend tableDataStorageBackend;
    private final DMLEvaluatorParameterAllocator dmlEvaluatorParameterAllocator;
    private final DMLPreparedStatementEvaluatorParameterAllocator dmlPreparedStatementEvaluatorParameterAllocator;

    private final Tables tables;
    private final Indices indices;

    private final SQLStatements sqlStatements;

    private final Sessions sessions;

    private final Transactions transactions;

    private final ObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;

    Database(String name, DatabaseParameters parameters, DMLEvaluatorParameterAllocator dmlEvaluatorParameterAllocator,
            DMLPreparedStatementEvaluatorParameterAllocator dmlPreparedStatementEvaluatorParameterAllocator) {
        super(DatabaseState.CREATED, false);

        Checks.isDatabaseName(name);
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(dmlEvaluatorParameterAllocator);
        Objects.requireNonNull(dmlPreparedStatementEvaluatorParameterAllocator);

        this.name = name;
        this.allocators = parameters.getAllocators();
        this.stringManagement = parameters.getStringManagement();
        this.schemas = parameters.getDatabaseSchemas();
        this.dataCache = parameters.getDataCache();
//        this.tableDataStorageBackend = parameters;

        this.dmlEvaluatorParameterAllocator = dmlEvaluatorParameterAllocator;
        this.dmlPreparedStatementEvaluatorParameterAllocator = dmlPreparedStatementEvaluatorParameterAllocator;

        this.tables = new Tables(schemas.getCurrentSchema(), parameters.getInitialRowIds());
        this.indices = new Indices();

        this.sqlStatements = new SQLStatements();

        this.sessions = new Sessions(parameters.getLargeObjectStorer());

        this.transactions = new Transactions(parameters.getInitialTransactionId(), parameters.getTransactionFactory());

        this.processCreateTableScratchCache = new ObjectCache<>(ProcessCreateTableScratchObject::new, ProcessCreateTableScratchObject[]::new);
    }

    @Override
    public int startTransaction(int sessionId) {

        return transactions.addTransaction();
    }

    @Override
    public void commitTransaction(int sessionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollbackTransaction(int sessionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public int createSavePoint(int sessionId, long savePointName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int rollbackToSavePoint(int sessionId, long savePointName) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addSession(Charset charset) {

        return sessions.addSession(charset).getSessionId();
    }

    public Charset getSessionCharset(int sessionId) {

        Checks.isSessionDescriptor(sessionId);

        return getSession(sessionId).getCharset();
    }

    @Override
    public void closeSession(int sessionId) {

        try {
            getSession(sessionId).close();
        }
        finally {

            sessions.removeSession(sessionId);
        }
    }

    @Override
    public void executeDDLSQL(int sessionId, BaseSQLDDLOperationStatement sqlStatement) {

        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends Exception> void executeDMLSelectSQL(int sessionId, SQLSelectStatement sqlStatement, SelectResultWriter<E> selectResultWriter) {

        throw new UnsupportedOperationException();
    }

    @Override
    public long executeDMLUpdateSQL(int sessionId, SQLDMLUpdatingStatement sqlStatement) throws DMLException {

        final DMLUpdatingEvaluatorParameter evaluatorParameter = dmlEvaluatorParameterAllocator.allocateDMLEvaluatorParameter();

        final Session session = getSession(sessionId);

        final Transaction transaction = transactions.getTransaction(session.getCurrentTransaction());

        evaluatorParameter.initializeForDMLOperation(transaction);

        final long numUpdatedRows;

        try {
            numUpdatedRows = session.executeDMUpdatingLStatement(sqlStatement, evaluatorParameter);
        }
        finally {

            dmlEvaluatorParameterAllocator.freeDMLEvaluatorParameter(evaluatorParameter);
        }

        return numUpdatedRows;
    }

    public String getName() {
        return name;
    }

    int prepareStatement(int sessionId, BaseSQLStatement sqlStatement, SQLString sqlString) {

        Checks.isSessionDescriptor(sessionId);
        Objects.requireNonNull(sqlStatement);
        Objects.requireNonNull(sqlString);

        return getSession(sessionId).prepareStatement(sqlStatement, sqlString);
    }

    <E extends Exception> long executePreparedStatement(int sessionId, int preparedStatementId, PreparedStatementParameters preparedStatementParameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Objects.requireNonNull(preparedStatementParameters);
        Objects.requireNonNull(resultWriter);

        final long result;

        final DMLUpdatingPreparedEvaluatorParameter evaluatorParameter = dmlPreparedStatementEvaluatorParameterAllocator.allocateDMLPreparedStatementEvaluatorParameter();

        try {
            result = getSession(sessionId).executePreparedStatement(preparedStatementId, preparedStatementParameters, evaluatorParameter, resultWriter);
        }
        finally {

            dmlPreparedStatementEvaluatorParameterAllocator.freeDMLPreparedStatementEvaluatorParameter(evaluatorParameter);
        }

        return result;
    }

    long createPreparedStatementLargeObjectPart(int sessionId, int preparedStatementId, long length) throws IOException {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);

        return getSession(sessionId).createPreparedStatementLargeObjectPart(preparedStatementId, length);
    }

    void storePreparedStatementLargeObjectPart(int sessionId, int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer, int offset, int length)
            throws IOException {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Checks.isLargeObjectRef(largeObjectRef);
        Objects.requireNonNull(byteBuffer);
        Checks.isOffset(offset);
        Checks.isNumBytes(length);

        getSession(sessionId).storePreparedStatementLargeObjectPart(preparedStatementId, largeObjectRef, isFinal, byteBuffer, offset, length);
    }

    void freePreparedStatement(int sessionId, int preparedStatementId) {

        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);

        getSession(sessionId).freePreparedStatement(preparedStatementId);
    }

    int getDatabaseId() {

        return getDescriptor();
    }

    private static abstract class ProcessParsedScratchObject {

        private StringManagement stringManagement;
        private StringResolver parserStringResolver;

        void initialize(StringManagement stringManagement, StringResolver parserStringResolver) {

            this.stringManagement = Objects.requireNonNull(stringManagement);
            this.parserStringResolver = Objects.requireNonNull(parserStringResolver);
        }

        final StringManagement getStringManagement() {
            return stringManagement;
        }

        final StringResolver getParserStringResolver() {
            return parserStringResolver;
        }

        final String getParsedString(long stringRef) {

            return stringManagement.getParsedString(parserStringResolver, stringRef);
        }
    }

    private static final class ProcessCreateTableScratchObject extends ProcessParsedScratchObject {

        private List<Column> columns;

        void initialize(StringManagement stringManagement, StringResolver parserStringResolver, List<Column> columns) {

            super.initialize(stringManagement, parserStringResolver);

            this.columns = Objects.requireNonNull(columns);
        }
    }

    private void processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringResolver parserStringResolver) throws TableAlreadyExistsException {

        synchronized (schemas) {

            final String tableName = stringManagement.getParsedString(parserStringResolver, sqlCreateTableStatement.getName());

            final DatabaseSchema currentSchema = schemas.getCurrentSchema();

            if (currentSchema.getTables().containsSchemaObjectName(tableName)) {

                throw new TableAlreadyExistsException();
            }

            final List<Column> columns = allocators.allocateList(sqlCreateTableStatement.getColumns().size());
            final ProcessCreateTableScratchObject processCreateTableScratchObject = processCreateTableScratchCache.allocate();

            final DatabaseSchema newDatabaseSchema;

            try {
                processCreateTableScratchObject.initialize(stringManagement, parserStringResolver, columns);

                convertColumns(sqlCreateTableStatement, processCreateTableScratchObject);

                final int tableId = schemas.allocateTableId();

                final Table table = new Table(tableName, tableId, columns);

                newDatabaseSchema = schemas.addTable(table);
            }
            finally {

                allocators.freeList(columns);
                processCreateTableScratchCache.free(processCreateTableScratchObject);
            }
        }
    }

    private static void convertColumns(SQLCreateTableStatement sqlCreateTableStatement, ProcessCreateTableScratchObject scratchObject) {

        final String tableName = scratchObject.getParsedString(sqlCreateTableStatement.getName());

        final ASTList<SQLTableColumnDefinition> sqlTableColumnDefinitions = sqlCreateTableStatement.getColumns();

        sqlTableColumnDefinitions.foreachWithIndexAndParameter(scratchObject, (c, i, s) -> {

            final Column column = convertToColumn(c, s.getStringManagement(), s.getParserStringResolver());

            s.columns.add(column);
        });
    }

    private static Column convertToColumn(SQLTableColumnDefinition sqlTableColumnDefinition, StringManagement stringManagement, StringResolver parserStringResolver) {

        final String columnName = stringManagement.getParsedString(parserStringResolver, sqlTableColumnDefinition.getName());

        final boolean nullable =    sqlTableColumnDefinition.getNotKeyword() != BaseASTElement.NO_KEYWORD
                                 && sqlTableColumnDefinition.getNullKeyword() != BaseASTElement.NO_KEYWORD;

        final SchemaDataType schemaDataType = convertDataType(sqlTableColumnDefinition, stringManagement, parserStringResolver);

        return new Column(columnName, schemaDataType,  nullable);
    }

    private static SchemaDataType convertDataType(SQLTableColumnDefinition sqlTableColumnDefinition, StringManagement stringManagement, StringResolver parserStringResolver) {

        final String typeName = stringManagement.getParsedString(parserStringResolver, sqlTableColumnDefinition.getTypeName());

        final SchemaDataType result;

        final String lowerCaseTypeName = stringManagement.getLowerCaseString(typeName);

        switch (lowerCaseTypeName) {

        case "integer":

            result = IntegerType.INSTANCE;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }

    private Session getSession(int sessionId) {

        return sessions.getSession(sessionId);
    }
}
