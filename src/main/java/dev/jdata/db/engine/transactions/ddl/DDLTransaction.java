package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;
import java.util.function.ToIntFunction;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.ddl.allocators.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.DDLAlterTableSchemasHelper;
import dev.jdata.db.ddl.helpers.DDLCreateTableSchemasHelper;
import dev.jdata.db.ddl.helpers.SchemaObjectIdAllocators;
import dev.jdata.db.ddl.model.diff.TableDiff;
import dev.jdata.db.engine.database.StringManagement;
import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionStatement;
import dev.jdata.db.engine.transactions.ddl.DDLTransactionEffectiveSchemaHelper.DDLComputeEffectiveDatabaseSchemaParameter;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.engine.validation.exceptions.SchemaObjectAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.TableAlreadyExistsException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.EffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.storage.IDatabaseSchemaSerializer;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory.IDatabaseSchemaStorage;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;
import dev.jdata.db.utils.adt.lists.MutableIndexList;
import dev.jdata.db.utils.adt.lists.MutableIndexList.MutableIndexListAllocator;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

class DDLTransaction<

                TRANSACTION_STATEMENT_INDEX_LIST extends IndexList<DDLTransactionStatement>,
                TRANSACTION_STATEMENT_INDEX_LIST_BUILDER extends IndexListBuilder<
                                DDLTransactionStatement,
                                TRANSACTION_STATEMENT_INDEX_LIST,
                                TRANSACTION_STATEMENT_INDEX_LIST_BUILDER>,
                TRANSACTION_OBJECT_INDEX_LIST extends IndexList<DDLTransactionObject>,
                TRANSACTION_OBJECT_INDEX_LIST_BUILDER extends IndexListBuilder<
                                DDLTransactionObject,
                                TRANSACTION_OBJECT_INDEX_LIST,
                                TRANSACTION_OBJECT_INDEX_LIST_BUILDER>,
                TRANSACTION_OBJECT_MUTABLE_INDEX_LIST extends MutableIndexList<DDLTransactionObject>>

        extends Allocatable
        implements IResettable {

    static class DDLTransactionCachedObjects<

                    TRANSACTION_STATEMENT_INDEX_LIST extends IndexList<DDLTransactionStatement>,
                    TRANSACTION_STATEMENT_INDEX_LIST_BUILDER extends IndexListBuilder<
                                    DDLTransactionStatement,
                                    TRANSACTION_STATEMENT_INDEX_LIST,
                                    TRANSACTION_STATEMENT_INDEX_LIST_BUILDER>,
                    TRANSACTION_OBJECT_INDEX_LIST extends IndexList<DDLTransactionObject>,
                    TRANSACTION_OBJECT_INDEX_LIST_BUILDER extends IndexListBuilder<
                                    DDLTransactionObject,
                                    TRANSACTION_OBJECT_INDEX_LIST,
                                    TRANSACTION_OBJECT_INDEX_LIST_BUILDER>,
                    TRANSACTION_OBJECT_MUTABLE_INDEX_LIST extends MutableIndexList<DDLTransactionObject>> {

        private final IndexListAllocator<
                        DDLTransactionStatement,
                        TRANSACTION_STATEMENT_INDEX_LIST,
                        TRANSACTION_STATEMENT_INDEX_LIST_BUILDER,
                        ?> ddlTransactionStatementListAllocator;

        private final IndexListAllocator<
                        DDLTransactionObject,
                        TRANSACTION_OBJECT_INDEX_LIST,
                        TRANSACTION_OBJECT_INDEX_LIST_BUILDER,
                        ?> ddlTransactionObjectListAllocator;

        private final MutableIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_INDEX_LIST> ddlTransactionObjectMutableListAllocator;

        private final DDLSchemaScratchObjects ddlSchemaScratchObjects;

        private final SchemaObjectIdAllocators schemaObjectIdAllocators;

        private final NodeObjectCache<DDLTransactionStatement> ddlTransactionStatementCache;

        private final NodeObjectCache<DDLTransactionAddedColumnsSchemaObject> ddlTransactionAddedColumnsSchemaObjectCache;
        private final NodeObjectCache<DDLTransactionAddedNonColumnsSchemaObject> ddlTransactionAddedNonColumnsSchemaObjectCache;
        private final NodeObjectCache<DDLTransactionColumnsDiffObject> ddlTransactionColumnsDiffObjectCache;

        DDLTransactionCachedObjects(
                IndexListAllocator<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, TRANSACTION_STATEMENT_INDEX_LIST_BUILDER, ?> ddlTransactionStatementListAllocator,
                IndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, TRANSACTION_OBJECT_INDEX_LIST_BUILDER, ?> ddlTransactionObjectListAllocator,
                MutableIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_INDEX_LIST> ddlTransactionObjectMutableListAllocator,
                DDLSchemaScratchObjects ddlSchemaScratchObjects) {

            this.ddlTransactionStatementListAllocator = Objects.requireNonNull(ddlTransactionStatementListAllocator);
            this.ddlTransactionObjectListAllocator = Objects.requireNonNull(ddlTransactionObjectListAllocator);
            this.ddlTransactionObjectMutableListAllocator = Objects.requireNonNull(ddlTransactionObjectMutableListAllocator);

            this.ddlSchemaScratchObjects = Objects.requireNonNull(ddlSchemaScratchObjects);

            this.schemaObjectIdAllocators = SchemaObjectIdAllocators.ofInitial();

            this.ddlTransactionStatementCache = new NodeObjectCache<>(DDLTransactionStatement::new);

            this.ddlTransactionAddedColumnsSchemaObjectCache = new NodeObjectCache<>(DDLTransactionAddedColumnsSchemaObject::new);
            this.ddlTransactionAddedNonColumnsSchemaObjectCache = new NodeObjectCache<>(DDLTransactionAddedNonColumnsSchemaObject::new);
            this.ddlTransactionColumnsDiffObjectCache = new NodeObjectCache<>(DDLTransactionColumnsDiffObject::new);
        }
    }

    static final class DDLTransactionStatement extends ObjectCacheNode implements IResettable {

        private BaseSQLDDLOperationStatement sqlStatement;
        private ISQLString sqlString;
        private StringResolver parserStringResolver;

        void initialize(BaseSQLDDLOperationStatement sqlStatement, ISQLString sqlString, StringResolver parserStringResolver) {

            this.sqlStatement = Initializable.checkNotYetInitialized(this.sqlStatement, sqlStatement);
            this.sqlString = Initializable.checkNotYetInitialized(this.sqlString, sqlString);
            this.parserStringResolver = Initializable.checkNotYetInitialized(this.parserStringResolver, parserStringResolver);
        }

        @Override
        public void reset() {

            this.sqlStatement = Initializable.checkResettable(sqlStatement);
            this.sqlString = Initializable.checkResettable(sqlString);
            this.parserStringResolver = Initializable.checkResettable(parserStringResolver);
        }
    }

    private final SQLStatementAdapter<StringManagement, DDLTransactionObject, SQLValidationException> ddlStatementVisitor
            = new SQLStatementAdapter<StringManagement, DDLTransactionObject, SQLValidationException>() {

        @Override
        public DDLTransactionObject onCreateTable(SQLCreateTableStatement createTableStatement, StringManagement parameter) throws TableAlreadyExistsException {

            return DDLTransaction.this.processCreateTable(createTableStatement, parameter);
        }

        @Override
        public DDLTransactionObject onAlterTable(SQLAlterTableStatement alterTableStatement, StringManagement parameter) throws SQLValidationException {

            return DDLTransaction.this.processAlterTable(alterTableStatement, parameter);
        }
    };

    private StringResolver schemaStringResolver;

    private DDLTransactionCachedObjects<
                    TRANSACTION_STATEMENT_INDEX_LIST,
                    TRANSACTION_STATEMENT_INDEX_LIST_BUILDER,
                    TRANSACTION_OBJECT_INDEX_LIST,
                    TRANSACTION_OBJECT_INDEX_LIST_BUILDER,
                    TRANSACTION_OBJECT_MUTABLE_INDEX_LIST> ddlTransactionCachedObjects;

    private IEffectiveDatabaseSchema currentSchema;

    private TRANSACTION_STATEMENT_INDEX_LIST_BUILDER ddlTransactionStatementsBuilder;
    private TRANSACTION_OBJECT_INDEX_LIST_BUILDER ddlTransactionObjectsBuilder;

    private TRANSACTION_OBJECT_MUTABLE_INDEX_LIST ddlTransactionObjectsList;

    private long scratchName;

    DDLTransaction(AllocationType allocationType) {
        super(allocationType);
    }

    final void initialize(IEffectiveDatabaseSchema currentSchema, StringResolver schemaStringResolver,
            DDLTransactionCachedObjects<
                            TRANSACTION_STATEMENT_INDEX_LIST,
                            TRANSACTION_STATEMENT_INDEX_LIST_BUILDER,
                            TRANSACTION_OBJECT_INDEX_LIST,
                            TRANSACTION_OBJECT_INDEX_LIST_BUILDER,
                            TRANSACTION_OBJECT_MUTABLE_INDEX_LIST>
                            ddlCachedObjects) {

        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(schemaStringResolver);
        Objects.requireNonNull(ddlCachedObjects);

        checkIsAllocated();

        this.currentSchema = Initializable.checkNotYetInitialized(this.currentSchema, currentSchema);
        this.schemaStringResolver = Initializable.checkNotYetInitialized(this.schemaStringResolver, schemaStringResolver);
        this.ddlTransactionCachedObjects = Initializable.checkNotYetInitialized(this.ddlTransactionCachedObjects, ddlCachedObjects);

        this.ddlTransactionStatementsBuilder = Initializable.checkNotYetInitialized(this.ddlTransactionStatementsBuilder,
                IndexList.createBuilder(ddlCachedObjects.ddlTransactionStatementListAllocator));

        this.ddlTransactionObjectsBuilder = Initializable.checkNotYetInitialized(this.ddlTransactionObjectsBuilder,
                IndexList.createBuilder(ddlCachedObjects.ddlTransactionObjectListAllocator));

        this.ddlTransactionObjectsList = Initializable.checkNotYetInitialized(this.ddlTransactionObjectsList,
                MutableIndexList.create(0, ddlCachedObjects.ddlTransactionObjectMutableListAllocator));

        ddlTransactionCachedObjects.schemaObjectIdAllocators.initialize(currentSchema);
    }

    @Override
    public final void reset() {

        checkIsAllocated();

        ddlTransactionCachedObjects.ddlTransactionStatementListAllocator.freeIndexListBuilder(ddlTransactionStatementsBuilder);
        ddlTransactionCachedObjects.ddlTransactionObjectListAllocator.freeIndexListBuilder(ddlTransactionObjectsBuilder);

        this.currentSchema = Initializable.checkResettable(currentSchema);
        this.schemaStringResolver = Initializable.checkResettable(schemaStringResolver);
        this.ddlTransactionCachedObjects = Initializable.checkResettable(ddlTransactionCachedObjects);

        this.ddlTransactionStatementsBuilder = Initializable.checkResettable(ddlTransactionStatementsBuilder);
        this.ddlTransactionObjectsBuilder = Initializable.checkResettable(ddlTransactionObjectsBuilder);
        this.ddlTransactionObjectsList = Initializable.checkResettable(ddlTransactionObjectsList);
    }

    public final <E extends Exception> EffectiveDatabaseSchema commit(DatabaseSchemaVersion databaseSchemaVersion,
            IDatabaseSchemaStorageFactory<E> databaseSchemaStorage, IDatabaseSchemaSerializer databaseSchemaSerializer,
            ISQLOutputter<E> sqlOutputter, ICompleteSchemaMapsBuilder<SchemaObject, ?, HeapAllCompleteSchemaMaps> completeSchemaMapsBuilder,
            ToIntFunction<DDLObjectType> schemaObjectIdAllocator) throws E {

        Objects.requireNonNull(databaseSchemaVersion);
        Objects.requireNonNull(databaseSchemaStorage);
        Objects.requireNonNull(databaseSchemaSerializer);
        Objects.requireNonNull(sqlOutputter);
        Objects.requireNonNull(completeSchemaMapsBuilder);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final EffectiveDatabaseSchema result;

        final IDatabaseSchemaStorage<E> storage = databaseSchemaStorage.createSchemaDiffStorage(databaseSchemaVersion);

        final TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements = ddlTransactionStatementsBuilder.build();
        final TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects = ddlTransactionObjectsBuilder.build();

        try {
            final long numDDLTransactionStatements = ddlTransactionStatements.getNumElements();

            try {
                for (int i = 0; i < numDDLTransactionStatements; ++ i) {

                    final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatements.get(i);

                    storage.storeSchemaDiffStatement(ddlTransactionStatement.sqlStatement, ddlTransactionStatement.sqlString, ddlTransactionStatement.parserStringResolver);
                }
            }
            finally {

                clearAndFreeTransactionStatements(ddlTransactionStatements);
            }

            final DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter = new DDLComputeEffectiveDatabaseSchemaParameter();

            ddlComputeEffectiveDatabaseSchemaParameter.initialize(ddlTransactionObjects, completeSchemaMapsBuilder, schemaObjectIdAllocator);

            result = DDLTransactionEffectiveSchemaHelper.computeEffectiveDatabaseSchema(getDatabaseId(), databaseSchemaVersion, currentSchema,
                    ddlComputeEffectiveDatabaseSchemaParameter);

            storage.completeSchemaDiff(result, databaseSchemaSerializer, schemaStringResolver, sqlOutputter);
        }
        finally {

            clearAndFreeTransactionLists(ddlTransactionStatements, ddlTransactionObjects, ddlTransactionObjectsList);

            storage.reset();
        }

        return result;
    }

    final void rollback() {

        final TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements = ddlTransactionStatementsBuilder.build();
        final TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects = ddlTransactionObjectsBuilder.build();

        clearAndFreeTransactionLists(ddlTransactionStatements, ddlTransactionObjects, ddlTransactionObjectsList);
    }

    private void clearAndFreeTransactionLists(TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements, TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects,
            TRANSACTION_OBJECT_MUTABLE_INDEX_LIST ddlTransactionObjectsList) {

        clearAndFreeTransactionStatements(ddlTransactionStatements);
        clearAndFreeTransactionObjects(ddlTransactionObjects, ddlTransactionObjectsList);
    }

    private void clearAndFreeTransactionStatements(TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements) {

        final long numDDLTransactionStatements = ddlTransactionStatements.getNumElements();

        for (int i = 0; i < numDDLTransactionStatements; ++ i) {

            final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatements.get(i);

            ddlTransactionStatement.reset();

            ddlTransactionCachedObjects.ddlTransactionStatementCache.free(ddlTransactionStatement);
        }

        ddlTransactionCachedObjects.ddlTransactionStatementListAllocator.freeIndexList(ddlTransactionStatements);
    }

    private static final DDLTransactionObjectVisitor<DDLTransactionCachedObjects<?, ?, ?, ?, ?>, Void> freeDDLTransactionObjectVisitor
            = new DDLTransactionObjectVisitor<DDLTransactionCachedObjects<?, ?, ?, ?, ?>, Void>() {

        @Override
        public Void onAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject addedColumnsSchemaObject, DDLTransactionCachedObjects<?, ?, ?, ?, ?> parameter) {

            parameter.ddlTransactionAddedColumnsSchemaObjectCache.free(addedColumnsSchemaObject);

            return null;
        }

        @Override
        public Void onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject, DDLTransactionCachedObjects<?, ?, ?, ?, ?> parameter) {

            parameter.ddlTransactionAddedNonColumnsSchemaObjectCache.free(addedNonColumnsSchemaObject);

            return null;
        }

        @Override
        public Void onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject, DDLTransactionCachedObjects<?, ?, ?, ?, ?> parameter) {

            parameter.ddlTransactionColumnsDiffObjectCache.free(columnsDiffObject);

            return null;
        }
    };

    private void clearAndFreeTransactionObjects(TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects, TRANSACTION_OBJECT_MUTABLE_INDEX_LIST ddlTransactionObjectsList) {

        final long numDDLTransactionObjects = ddlTransactionObjects.getNumElements();

        for (int i = 0; i < numDDLTransactionObjects; ++ i) {

            final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

            ddlTransactionObject.reset();

            ddlTransactionObject.visit(freeDDLTransactionObjectVisitor, ddlTransactionCachedObjects);
        }

        ddlTransactionCachedObjects.ddlTransactionObjectListAllocator.freeIndexList(ddlTransactionObjects);
        ddlTransactionCachedObjects.ddlTransactionObjectMutableListAllocator.freeMutableIndexList(ddlTransactionObjectsList);
    }

    final void addDDLStatement(BaseSQLDDLOperationStatement ddlStatement, ISQLString sqlString, StringResolver parserStringResolver, StringManagement stringManagement)
            throws SQLValidationException {

        Objects.requireNonNull(ddlStatement);
        Objects.requireNonNull(sqlString);
        Objects.requireNonNull(parserStringResolver);
        Objects.requireNonNull(stringManagement);

        checkIsAllocated();

        final DDLTransactionObject ddlTransactionObject = ddlStatement.visit(ddlStatementVisitor, stringManagement);

        ddlTransactionObjectsBuilder.addTail(ddlTransactionObject);
        ddlTransactionObjectsList.addTail(ddlTransactionObject);

        final DDLTransactionStatement ddlTransactionStatement = ddlTransactionCachedObjects.ddlTransactionStatementCache.allocate();

        ddlTransactionStatement.initialize(ddlStatement, sqlString, parserStringResolver);

        ddlTransactionStatementsBuilder.addTail(ddlTransactionStatement);
    }

    private DDLTransactionAddedColumnsSchemaObject processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, StringManagement stringManagement)
            throws TableAlreadyExistsException {

        final long storedTableName = stringManagement.storeParsedStringRef(sqlCreateTableStatement.getName());

        checkSchemaObjectAlreadyExists(DDLObjectType.TABLE, storedTableName, TableAlreadyExistsException::new);

        final Table table = DDLCreateTableSchemasHelper.processCreateTable(sqlCreateTableStatement, stringManagement, ddlTransactionCachedObjects.ddlSchemaScratchObjects,
                ddlTransactionCachedObjects.schemaObjectIdAllocators, a -> a.allocate(DDLObjectType.TABLE));

        final DDLTransactionAddedColumnsSchemaObject result = ddlTransactionCachedObjects.ddlTransactionAddedColumnsSchemaObjectCache.allocate();

        result.initialize(table);

        return result;
    }

    private DDLTransactionColumnsDiffObject processAlterTable(SQLAlterTableStatement sqlAlterTableStatement, StringManagement stringManagement) throws SQLValidationException {

        final long parsedTableName = stringManagement.storeParsedStringRef(sqlAlterTableStatement.getName());

        final DDLObjectType ddlObjectType = DDLObjectType.TABLE;

        final DatabaseId databaseId = getDatabaseId();

        if (currentSchema.containsSchemaObjectName(ddlObjectType, parsedTableName)) {

            throw new TableAlreadyExistsException(databaseId, parsedTableName);
        }

        final long schemaTableName = stringManagement.getHashStringRef(parsedTableName);

        final Table existingTable = currentSchema.getTableMap().getSchemaObjectByName(schemaTableName);

        final TableDiff tableDiff = DDLAlterTableSchemasHelper.processAlterTable(sqlAlterTableStatement, databaseId, existingTable, stringManagement,
                ddlTransactionCachedObjects.ddlSchemaScratchObjects);

        final DDLTransactionColumnsDiffObject result = ddlTransactionCachedObjects.ddlTransactionColumnsDiffObjectCache.allocate();

        result.initialize(tableDiff);

        return result;
    }

    @FunctionalInterface
    private interface SchemaObjectAlreadyExistsExceptionFactory<E extends SchemaObjectAlreadyExistsException> {

        E create(DatabaseId databaseId, long storedSchemaObjectName);
    }

    private <E extends SchemaObjectAlreadyExistsException> void checkSchemaObjectAlreadyExists(DDLObjectType ddlObjectType, long storedSchemaObjectName,
            SchemaObjectAlreadyExistsExceptionFactory<E> schemaObjectAlreadyExistsExceptionFactory) throws E {

        if (currentSchema.containsSchemaObjectName(ddlObjectType, storedSchemaObjectName)) {

            throw schemaObjectAlreadyExistsExceptionFactory.create(getDatabaseId(), storedSchemaObjectName);
        }

        this.scratchName = storedSchemaObjectName;

        if (ddlTransactionObjectsList.contains(this,
                (o, i) -> o instanceof DDLTransactionAddedColumnsSchemaObject
                        ? ((DDLTransactionAddedColumnsSchemaObject)o).getSchemaObject().getStoredName() == i.scratchName
                        : false)) {

            throw schemaObjectAlreadyExistsExceptionFactory.create(getDatabaseId(), storedSchemaObjectName);
        }
    }

    private DatabaseId getDatabaseId() {

        return currentSchema.getDatabaseId();
    }
}
