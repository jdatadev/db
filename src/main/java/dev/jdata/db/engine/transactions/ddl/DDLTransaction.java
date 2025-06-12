package dev.jdata.db.engine.transactions.ddl;

import java.io.IOException;
import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.ddl.DDLAlterTableSchemasHelper;
import dev.jdata.db.ddl.DDLCreateTableSchemasHelper;
import dev.jdata.db.ddl.DDLSchemasHelper.DDLSchemaCachedObjects;
import dev.jdata.db.engine.database.DatabaseStringManagement;
import dev.jdata.db.engine.database.SQLValidationException;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.database.TableAlreadyExistsException;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps.DiffSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.SchemaMapBuilders;
import dev.jdata.db.schema.storage.DatabaseSchemaStorage;
import dev.jdata.db.schema.storage.IDatabaseSchemaSerializer;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory.IDatabaseSchemaStorage;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.parse.SQLString;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

final class DDLTransaction extends Allocatable {

    static final class DDLTransactionCachedObjects {

        private final IndexListAllocator<DDLTransactionStatement> ddlTransactionStatementListAllocator;
        private final DiffSchemaMapsBuilderAllocator diffSchemaMapsBuilderAllocator;
        private final DDLSchemaCachedObjects ddlSchemaCachedObjects;

        private final NodeObjectCache<DDLTransactionStatement> ddlTransactionStatementCache;

        DDLTransactionCachedObjects(IndexListAllocator<DDLTransactionStatement> ddlTransactionStatementListAllocator,
                DiffSchemaMapsBuilderAllocator diffSchemaMapsBuilderAllocator, DDLSchemaCachedObjects ddlSchemaCachedObjects) {

            this.ddlTransactionStatementListAllocator = Objects.requireNonNull(ddlTransactionStatementListAllocator);
            this.diffSchemaMapsBuilderAllocator = Objects.requireNonNull(diffSchemaMapsBuilderAllocator);
            this.ddlSchemaCachedObjects = Objects.requireNonNull(ddlSchemaCachedObjects);

            this.ddlTransactionStatementCache = new NodeObjectCache<>(DDLTransactionStatement::new);
        }
    }

    private static final class DDLTransactionStatement extends ObjectCacheNode implements IResettable {

        private BaseSQLDDLOperationStatement sqlStatement;
        private SQLString sqlString;
        private StringResolver stringResolver;

        void initialize(BaseSQLDDLOperationStatement sqlStatement, SQLString sqlString, StringResolver stringResolver) {

            this.sqlStatement = Objects.requireNonNull(sqlStatement);
            this.sqlString = Objects.requireNonNull(sqlString);
            this.stringResolver = Objects.requireNonNull(stringResolver);
        }

        @Override
        public void reset() {

            this.sqlStatement = null;
            this.sqlString = null;
            this.stringResolver = null;
        }
    }

    private final SQLStatementAdapter<StringManagement, Void, SQLValidationException> ddlStatementVisitor
            = new SQLStatementAdapter<StringManagement, Void, SQLValidationException>() {

        @Override
        public Void onCreateTable(SQLCreateTableStatement createTableStatement, StringManagement parameter) throws TableAlreadyExistsException {

            DDLTransaction.this.processCreateTable(createTableStatement, parameter);

            return null;
        }

        @Override
        public Void onAlterTable(SQLAlterTableStatement alterTableStatement, StringManagement parameter) throws SQLValidationException {

            DDLTransaction.this.processAlterTable(alterTableStatement, parameter);

            return null;
        }
    };

    private DatabaseStringManagement databaseStringManagement;
    private DDLTransactionCachedObjects ddlTransactionCachedObjects;
    private DatabaseSchemaManager databaseSchemaManager;
    private IDatabaseSchemaSerializer databaseSchemaSerializer;

    private IEffectiveDatabaseSchema currentSchema;

    private SchemaMapBuilders schemaMapBuilders;

    private IndexList.Builder<DDLTransactionStatement> ddlTransactionStatements;
    private DiffSchemaMaps.Builder diffSchemaMapsBuilder;

    DDLTransaction(AllocationType allocationType) {
        super(allocationType);
    }

    void initialize(IEffectiveDatabaseSchema currentSchema, DatabaseStringManagement stringManagement, DDLTransactionCachedObjects ddlCachedObjects,
            DatabaseSchemaManager databaseSchemaManager, IDatabaseSchemaSerializer databaseSchemaSerializer) {

        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlCachedObjects);
        Objects.requireNonNull(databaseSchemaManager);
        Objects.requireNonNull(databaseSchemaSerializer);

        checkIsNotAllocated();

        this.currentSchema = currentSchema;
        this.databaseStringManagement = stringManagement;
        this.ddlTransactionCachedObjects = ddlCachedObjects;
        this.databaseSchemaManager = databaseSchemaManager;
        this.databaseSchemaSerializer = databaseSchemaSerializer;

        this.diffSchemaMapsBuilder = DiffSchemaMaps.createBuilder(ddlCachedObjects.diffSchemaMapsBuilderAllocator);

        this.schemaMapBuilders = ddlCachedObjects.ddlSchemaCachedObjects.allocateSchemaMapBuilders();

        this.ddlTransactionStatements = IndexList.createBuilder(ddlCachedObjects.ddlTransactionStatementListAllocator);
    }

    public void reset(DDLTransactionCachedObjects ddlCachedObjects) {

        Objects.requireNonNull(ddlCachedObjects);

        checkIsAllocated();

        this.currentSchema = null;
        this.databaseStringManagement = null;
        this.ddlTransactionCachedObjects = null;
        this.databaseSchemaManager = null;
        this.databaseSchemaSerializer = null;

        this.diffSchemaMapsBuilder = null;

        ddlCachedObjects.ddlSchemaCachedObjects.freeSchemaMapBuilders(schemaMapBuilders);
    }

    public void commit(DatabaseSchemaVersion databaseSchemaVersion, DatabaseSchemaStorage databaseSchemaStorage, ISQLOutputter<IOException> sqlOutputter) throws IOException {

        Objects.requireNonNull(databaseSchemaVersion);
        Objects.requireNonNull(databaseSchemaStorage);
        Objects.requireNonNull(sqlOutputter);

        final IDatabaseSchemaStorage<IOException> storage = databaseSchemaStorage.storeSchemaDiff(databaseSchemaVersion);

        try {
            final IIndexList<DDLTransactionStatement> ddlTransactionStatementsList = makeDDLTransactionStatementsList();

            final long numDDLTransactionStatements = ddlTransactionStatementsList.getNumElements();

            try {
                for (int i = 0; i < numDDLTransactionStatements; ++ i) {

                    final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatementsList.get(i);

                    storage.storeSchemaDiffStatement(ddlTransactionStatement.sqlStatement, ddlTransactionStatement.sqlString, ddlTransactionStatement.stringResolver);
                }
            }
            finally {

                clearTransactionStatements(ddlTransactionStatementsList);
            }

            storage.completeSchemaDiff(currentSchema, databaseSchemaSerializer, databaseStringManagement.getStringResolver(), sqlOutputter);
        }
        finally {

            storage.reset();
        }
    }

    void rollback() {

        final IIndexList<DDLTransactionStatement> ddlTransactionStatementsList = makeDDLTransactionStatementsList();

        clearTransactionStatements(ddlTransactionStatementsList);
    }

    private IIndexList<DDLTransactionStatement> makeDDLTransactionStatementsList() {

        final IIndexList<DDLTransactionStatement> result = ddlTransactionStatements.build();

        ddlTransactionCachedObjects.ddlTransactionStatementListAllocator.freeIndexListBuilder(ddlTransactionStatements);

        this.ddlTransactionStatements = null;

        return result;
    }

    private void clearTransactionStatements(IIndexList<DDLTransactionStatement> ddlTransactionStatementsList) {

        final long numDDLTransactionStatements = ddlTransactionStatementsList.getNumElements();

        for (int i = 0; i < numDDLTransactionStatements; ++ i) {

            final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatementsList.get(i);

            ddlTransactionStatement.reset();

            ddlTransactionCachedObjects.ddlTransactionStatementCache.free(ddlTransactionStatement);
        }
    }

    void addDDLStatement(BaseSQLDDLOperationStatement ddlStatement, SQLString sqlString, StringManagement stringManagement, StringResolver stringResolver)
            throws SQLValidationException {

        Objects.requireNonNull(ddlStatement);
        Objects.requireNonNull(sqlString);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(stringResolver);

        checkIsAllocated();

        ddlStatement.visit(ddlStatementVisitor, stringManagement);

        final DDLTransactionStatement ddlTransactionStatement = ddlTransactionCachedObjects.ddlTransactionStatementCache.allocate();

        ddlTransactionStatement.initialize(ddlStatement, sqlString, stringResolver);

        ddlTransactionStatements.addTail(ddlTransactionStatement);
    }

    private void processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringManagement stringManagement) throws TableAlreadyExistsException {

        final long parsedTableName = stringManagement.resolveParsedStringRef(sqlCreateTableStatement.getName());

        final DDLObjectType objectType = DDLObjectType.TABLE;

        if (currentSchema.containsSchemaObjectName(objectType, parsedTableName)) {

            throw new TableAlreadyExistsException();
        }

        DDLCreateTableSchemasHelper.processCreateTable(sqlCreateTableStatement, stringManagement, schemaMapBuilders, ddlTransactionCachedObjects.ddlSchemaCachedObjects,
                databaseSchemaManager, m -> m.allocateTableId());
    }

    private void processAlterTable(SQLAlterTableStatement sqlAlterTableStatement, StringManagement stringManagement) throws TableAlreadyExistsException {

        final long parsedTableName = stringManagement.resolveParsedStringRef(sqlAlterTableStatement.getName());

        final DDLObjectType objectType = DDLObjectType.TABLE;

        if (currentSchema.containsSchemaObjectName(objectType, parsedTableName)) {

            throw new TableAlreadyExistsException();
        }

        final long schemaTableName = stringManagement.getHashStringRef(parsedTableName);

        final Table existingTable = currentSchema.getTableMap().getSchemaObjectByName(schemaTableName);

        DDLAlterTableSchemasHelper.processAlterTable(sqlAlterTableStatement, existingTable, stringManagement, schemaMapBuilders, ddlTransactionCachedObjects.ddlSchemaCachedObjects);
    }
}
