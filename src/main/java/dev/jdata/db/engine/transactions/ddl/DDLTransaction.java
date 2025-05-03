package dev.jdata.db.engine.transactions.ddl;

import java.util.Arrays;
import java.util.Objects;

import org.jutils.ast.objects.BaseASTElement;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.SQLValidationException;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.database.TableAlreadyExistsException;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.diff.DiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps.DiffSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.IntegerType;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLTableColumnDefinition;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

final class DDLTransaction extends Allocatable implements IResettable {

    static final class DDLCachedObjects {

        private final IndexListAllocator<Column> columnListAllocator;
        private final DiffSchemaMapsBuilderAllocator diffSchemaMapsBuilderAllocator;
        private final SchemaMapBuilderAllocators schemaMapBuilderAllocators;

        private final NodeObjectCache<ProcessCreateTableScratchObject> processCreateTableScratchCache;

        DDLCachedObjects(IndexListAllocator<Column> columnListAllocator, DiffSchemaMapsBuilderAllocator diffSchemaMapsBuilderAllocator,
                SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

            this.columnListAllocator = Objects.requireNonNull(columnListAllocator);
            this.diffSchemaMapsBuilderAllocator = Objects.requireNonNull(diffSchemaMapsBuilderAllocator);
            this.schemaMapBuilderAllocators = Objects.requireNonNull(schemaMapBuilderAllocators);

            this.processCreateTableScratchCache = new NodeObjectCache<>(ProcessCreateTableScratchObject::new);
        }
    }
/*
    private static final class SchemaBuilders extends DDLObjectTypeInstances<SchemaMap.Builder<?>> {

        public SchemaBuilders(IntFunction<Builder<?>[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, Builder<?>>, Builder<?>> createElement) {
            super(SchemaMap.Builder[]::new, createElement);
        }
    }
*/
    private final SQLStatementAdapter<StringResolver, Void, SQLValidationException> ddlStatementVisitor
            = new SQLStatementAdapter<StringResolver, Void, SQLValidationException>() {

        @Override
        public Void onCreateTable(SQLCreateTableStatement createTableStatement, StringResolver parameter) throws TableAlreadyExistsException {

            DDLTransaction.this.processCreateTable(createTableStatement, parameter);

            return null;
        }
    };

    private final SchemaMap.Builder<?>[] schemaMapBuilders;

    private StringManagement stringManagement;
    private DDLCachedObjects ddlCachedObjects;
    private DatabaseSchemaManager databaseSchemaManager;

    private IEffectiveDatabaseSchema currentSchema;

//    private final IndexList.Builder<BaseSQLDDLOperationStatement> ddlStatements;

    private DiffSchemaMaps.Builder diffSchemaMapsBuilder;

    DDLTransaction() {

        this.schemaMapBuilders = new SchemaMap.Builder<?>[DDLObjectType.getNumObjectTypes()];
    }

    void initialize(IEffectiveDatabaseSchema currentSchema, StringManagement stringManagement, DDLCachedObjects ddlCachedObjects, DatabaseSchemaManager databaseSchemaManager) {

        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(stringManagement);
        Objects.requireNonNull(ddlCachedObjects);
        Objects.requireNonNull(databaseSchemaManager);

        checkIsNotAllocated();

        this.currentSchema = currentSchema;
        this.stringManagement = stringManagement;
        this.ddlCachedObjects = ddlCachedObjects;
        this.databaseSchemaManager = databaseSchemaManager;

        this.diffSchemaMapsBuilder = DiffSchemaMaps.createBuilder(ddlCachedObjects.diffSchemaMapsBuilderAllocator);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            schemaMapBuilders[ddlObjectType.ordinal()] = SchemaMap.createBuilder(0, ddlCachedObjects.schemaMapBuilderAllocators.getAllocator(ddlObjectType));
        }

//        this.ddlStatements = IndexList.createBuilder(BaseSQLDDLOperationStatement[]::new);
    }

    @Override
    public void reset() {

        checkIsAllocated();

        this.currentSchema = null;
        this.stringManagement = null;
        this.ddlCachedObjects = null;
        this.databaseSchemaManager = null;

        this.diffSchemaMapsBuilder = null;

        Arrays.fill(schemaMapBuilders, null);
    }

    void addDDLStatement(BaseSQLDDLOperationStatement ddlStatement, StringResolver parserStringResolver) throws SQLValidationException {

        Objects.requireNonNull(ddlStatement);
        Objects.requireNonNull(parserStringResolver);

        checkIsAllocated();

        ddlStatement.visit(ddlStatementVisitor, parserStringResolver);

//        ddlStatements.addTail(ddlStatement);
    }

    private void processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringResolver parserStringResolver) throws TableAlreadyExistsException {

        final long parsedTableName = stringManagement.resolveParsedStringRef(parserStringResolver, sqlCreateTableStatement.getName());

        final DDLObjectType objectType = DDLObjectType.TABLE;

        if (currentSchema.containsSchemaObjectName(objectType, parsedTableName)) {

            throw new TableAlreadyExistsException();
        }

        final IndexListAllocator<Column> columnIndexListAllocator = ddlCachedObjects.columnListAllocator;

        final IndexList.Builder<Column> columnsBuilder = IndexList.createBuilder(sqlCreateTableStatement.getColumns().size(), columnIndexListAllocator);

        final ProcessCreateTableScratchObject processCreateTableScratchObject = ddlCachedObjects.processCreateTableScratchCache.allocate();

        IIndexList<Column> columns = null;

        final DiffDatabaseSchema newDatabaseSchema;

        try {
            processCreateTableScratchObject.initialize(stringManagement, parserStringResolver, columnsBuilder);

            convertColumns(sqlCreateTableStatement, processCreateTableScratchObject);

            final int tableId = databaseSchemaManager.allocateTableId();

            columns = columnsBuilder.build();

            final long hashTableName = stringManagement.getHashStringRef(parsedTableName);

            final Table table = new Table(parsedTableName, hashTableName, tableId, columns);

            addToSchemaMapBuilder(DDLObjectType.TABLE, table);
        }
        finally {

            if (columns != null) {

                columnIndexListAllocator.freeIndexList(columns);
            }

            ddlCachedObjects.processCreateTableScratchCache.free(processCreateTableScratchObject);

            columnIndexListAllocator.freeIndexListBuilder(columnsBuilder);
        }
    }

    private <T extends SchemaObject> void addToSchemaMapBuilder(DDLObjectType ddlObjectType, T schemaObject) {

        @SuppressWarnings("unchecked")
        final SchemaMap.Builder<T> tableSchemaMapBuilder = (SchemaMap.Builder<T>)schemaMapBuilders[ddlObjectType.ordinal()];

        tableSchemaMapBuilder.add(schemaObject);
    }

    private static DiffSchemaMaps buildDiffSchemaMaps(DiffSchemaMaps.Builder diffSchemaMapsBuilder, SchemaMap.Builder<?>[] schemaMapBuilders) {

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final SchemaMap<?> schemaMap = schemaMapBuilders[ddlObjectType.ordinal()].build();

            diffSchemaMapsBuilder.setSchemaMap(ddlObjectType, schemaMap);
        }

        return diffSchemaMapsBuilder.build();
    }

    private static void convertColumns(SQLCreateTableStatement sqlCreateTableStatement, ProcessCreateTableScratchObject scratchObject) {

        final ASTList<SQLTableColumnDefinition> sqlTableColumnDefinitions = sqlCreateTableStatement.getColumns();

        sqlTableColumnDefinitions.foreachWithIndexAndParameter(scratchObject, (c, i, s) -> {

            final Column column = convertToColumn(c, s.allocateColumnId(), s.getStringManagement(), s.getParserStringResolver());

            s.columnsBuilder.addTail(column);
        });
    }

    private static Column convertToColumn(SQLTableColumnDefinition sqlTableColumnDefinition, int columnId, StringManagement stringManagement,
            StringResolver parserStringResolver) {

        final long columnName = stringManagement.resolveParsedStringRef(parserStringResolver, sqlTableColumnDefinition.getName());
        final long hashColumnName = stringManagement.getHashStringRef(columnName);

        final boolean nullable =    sqlTableColumnDefinition.getNotKeyword() != BaseASTElement.NO_KEYWORD
                                 && sqlTableColumnDefinition.getNullKeyword() != BaseASTElement.NO_KEYWORD;

        final SchemaDataType schemaDataType = convertDataType(sqlTableColumnDefinition, stringManagement, parserStringResolver);

        return new Column(columnName, hashColumnName, columnId, schemaDataType, nullable);
    }

    private static SchemaDataType convertDataType(SQLTableColumnDefinition sqlTableColumnDefinition, StringManagement stringManagement, StringResolver parserStringResolver) {

        final long typeName = stringManagement.resolveParsedStringRef(parserStringResolver, sqlTableColumnDefinition.getTypeName());

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

    private static abstract class ProcessParsedScratchObject extends ObjectCacheNode {

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

        final long getParsedStringRef(long stringRef) {

            return stringManagement.resolveParsedStringRef(parserStringResolver, stringRef);
        }
    }

    private static final class ProcessCreateTableScratchObject extends ProcessParsedScratchObject {

        private IndexList.Builder<Column> columnsBuilder;
        private int columnIdSequenceNo;

        void initialize(StringManagement stringManagement, StringResolver parserStringResolver, IndexList.Builder<Column> columnsBuilder) {

            super.initialize(stringManagement, parserStringResolver);

            this.columnsBuilder = Objects.requireNonNull(columnsBuilder);
            this.columnIdSequenceNo = DBConstants.INITIAL_COLUMN_ID;
        }

        int allocateColumnId() {

            return columnIdSequenceNo ++;
        }
    }
}
