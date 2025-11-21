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
import dev.jdata.db.engine.validation.exceptions.TableDoesNotExistException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.storage.IDatabaseSchemaSerializer;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory.IDatabaseSchemaStorage;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IMutableIndexList;
import dev.jdata.db.utils.adt.lists.IMutableIndexListAllocator;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.instances.Instances;

class DDLTransaction<

                TRANSACTION_STATEMENT_INDEX_LIST extends IIndexList<DDLTransactionStatement>,
                TRANSACTION_STATEMENT_INDEX_LIST_BUILDER extends IIndexListBuilder<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?>,
                TRANSACTION_OBJECT_INDEX_LIST extends IIndexList<DDLTransactionObject>,
                TRANSACTION_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?>,
                TRANSACTION_OBJECT_MUTABLE_INDEX_LIST extends IMutableIndexList<DDLTransactionObject>>

        extends Allocatable
        implements IResettable {

    static class DDLTransactionCachedObjects<

                    TRANSACTION_STATEMENT_INDEX_LIST extends IIndexList<DDLTransactionStatement>,
                    TRANSACTION_STATEMENT_INDEX_LIST_BUILDER extends IIndexListBuilder<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?>,
                    TRANSACTION_OBJECT_INDEX_LIST extends IIndexList<DDLTransactionObject>,
                    TRANSACTION_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?>,
                    TRANSACTION_OBJECT_MUTABLE_INDEX_LIST extends IMutableIndexList<DDLTransactionObject>> {

        private final IIndexListAllocator<

                        DDLTransactionStatement,
                        TRANSACTION_STATEMENT_INDEX_LIST,
                        ?,
                        TRANSACTION_STATEMENT_INDEX_LIST_BUILDER> ddlTransactionStatementListAllocator;

        private final IIndexListAllocator<

                        DDLTransactionObject,
                        TRANSACTION_OBJECT_INDEX_LIST,
                        ?,
                        TRANSACTION_OBJECT_INDEX_LIST_BUILDER> ddlTransactionObjectListAllocator;

        private final IMutableIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_INDEX_LIST> ddlTransactionObjectMutableListAllocator;

        private final DDLSchemaScratchObjects ddlSchemaScratchObjects;

        private final SchemaObjectIdAllocators schemaObjectIdAllocators;

        private final NodeObjectCache<DDLTransactionStatement> ddlTransactionStatementCache;

        private final NodeObjectCache<DDLTransactionAddedColumnsSchemaObject> ddlTransactionAddedColumnsSchemaObjectCache;
        private final NodeObjectCache<DDLTransactionAddedNonColumnsSchemaObject> ddlTransactionAddedNonColumnsSchemaObjectCache;
        private final NodeObjectCache<DDLTransactionColumnsDiffObject> ddlTransactionColumnsDiffObjectCache;
        private final NodeObjectCache<DDLTransactionDroppedSchemaObject> ddlTransactionDroppedSchemaObjectCache;

        private final NodeObjectCache<DDLComputeEffectiveDatabaseSchemaParameter> ddlComputeEffectiveDatabaseSchemaParameterCache;

        DDLTransactionCachedObjects(
                IIndexListAllocator<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?, TRANSACTION_STATEMENT_INDEX_LIST_BUILDER> ddlTransactionStatementListAllocator,
                IIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?, TRANSACTION_OBJECT_INDEX_LIST_BUILDER> ddlTransactionObjectListAllocator,
                IMutableIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_INDEX_LIST> ddlTransactionObjectMutableListAllocator,
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
            this.ddlTransactionDroppedSchemaObjectCache = new NodeObjectCache<>(DDLTransactionDroppedSchemaObject::new);

            this.ddlComputeEffectiveDatabaseSchemaParameterCache = new NodeObjectCache<>(DDLComputeEffectiveDatabaseSchemaParameter::new);
        }
    }

    static final class DDLTransactionStatement extends ObjectCacheNode implements IResettable {

        private BaseSQLDDLOperationStatement sqlStatement;
        private ISQLString sqlString;
        private StringResolver parserStringResolver;

        DDLTransactionStatement(AllocationType allocationType) {
            super(allocationType);
        }

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

        @Override
        public DDLTransactionObject onDropTable(SQLDropTableStatement dropTableStatement, StringManagement parameter) throws SQLValidationException {

            return DDLTransaction.this.processDropTable(dropTableStatement, parameter);
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

        checkIsAllocatedRenamed();

        this.currentSchema = Initializable.checkNotYetInitialized(this.currentSchema, currentSchema);
        this.schemaStringResolver = Initializable.checkNotYetInitialized(this.schemaStringResolver, schemaStringResolver);
        this.ddlTransactionCachedObjects = Initializable.checkNotYetInitialized(this.ddlTransactionCachedObjects, ddlCachedObjects);

        this.ddlTransactionStatementsBuilder = Initializable.checkNotYetInitialized(this.ddlTransactionStatementsBuilder,
                ddlCachedObjects.ddlTransactionStatementListAllocator.createBuilder());

        this.ddlTransactionObjectsBuilder = Initializable.checkNotYetInitialized(this.ddlTransactionObjectsBuilder,
                ddlCachedObjects.ddlTransactionObjectListAllocator.createBuilder());

        this.ddlTransactionObjectsList = Initializable.checkNotYetInitialized(this.ddlTransactionObjectsList,
                ddlCachedObjects.ddlTransactionObjectMutableListAllocator.createMutable(0L));

        ddlTransactionCachedObjects.schemaObjectIdAllocators.initialize(currentSchema);
    }

    @Override
    public final void reset() {

        checkIsAllocatedRenamed();

        ddlTransactionCachedObjects.ddlTransactionStatementListAllocator.freeBuilder(ddlTransactionStatementsBuilder);
        ddlTransactionCachedObjects.ddlTransactionObjectListAllocator.freeBuilder(ddlTransactionObjectsBuilder);

        this.currentSchema = Initializable.checkResettable(currentSchema);
        this.schemaStringResolver = Initializable.checkResettable(schemaStringResolver);
        this.ddlTransactionCachedObjects = Initializable.checkResettable(ddlTransactionCachedObjects);

        this.ddlTransactionStatementsBuilder = Initializable.checkResettable(ddlTransactionStatementsBuilder);
        this.ddlTransactionObjectsBuilder = Initializable.checkResettable(ddlTransactionObjectsBuilder);
        this.ddlTransactionObjectsList = Initializable.checkResettable(ddlTransactionObjectsList);
    }

    public final <E extends Exception> IHeapEffectiveDatabaseSchema commit(DatabaseSchemaVersion databaseSchemaVersion,
            IDatabaseSchemaStorageFactory<E> databaseSchemaStorage, IDatabaseSchemaSerializer databaseSchemaSerializer,
            ISQLOutputter<E> sqlOutputter, ICompleteSchemaMapsBuilder<SchemaObject, ?, IHeapAllCompleteSchemaMaps, ?> completeSchemaMapsBuilder,
            ToIntFunction<DDLObjectType> schemaObjectIdAllocator) throws E {

        Objects.requireNonNull(databaseSchemaVersion);
        Objects.requireNonNull(databaseSchemaStorage);
        Objects.requireNonNull(databaseSchemaSerializer);
        Objects.requireNonNull(sqlOutputter);
        Objects.requireNonNull(completeSchemaMapsBuilder);
        Objects.requireNonNull(schemaObjectIdAllocator);

        final IHeapEffectiveDatabaseSchema result;

        final IDatabaseSchemaStorage<E> storage = databaseSchemaStorage.createSchemaDiffStorage(databaseSchemaVersion);

        final TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements = ddlTransactionStatementsBuilder.buildOrNull();
        final TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects = ddlTransactionObjectsBuilder.buildOrNull();

        try {
            if (Instances.areBothNotNullOrBothNullOrThrowException(ddlTransactionStatements, ddlTransactionObjects)) {

                final long numDDLTransactionStatements = ddlTransactionStatements.getNumElements();

                for (int i = 0; i < numDDLTransactionStatements; ++ i) {

                    final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatements.get(i);

                    storage.storeSchemaDiffStatement(ddlTransactionStatement.sqlStatement, ddlTransactionStatement.sqlString, ddlTransactionStatement.parserStringResolver);
                }

                final DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter
                        = ddlTransactionCachedObjects.ddlComputeEffectiveDatabaseSchemaParameterCache.allocate();

                try {
                    ddlComputeEffectiveDatabaseSchemaParameter.initialize(ddlTransactionObjects, completeSchemaMapsBuilder, schemaObjectIdAllocator);

                    result = DDLTransactionEffectiveSchemaHelper.computeEffectiveDatabaseSchema(getDatabaseId(), databaseSchemaVersion, currentSchema,
                            ddlComputeEffectiveDatabaseSchemaParameter);

                    storage.completeSchemaDiff(result, databaseSchemaSerializer, schemaStringResolver, sqlOutputter);
                }
                finally {

                    ddlTransactionCachedObjects.ddlComputeEffectiveDatabaseSchemaParameterCache.free(ddlComputeEffectiveDatabaseSchemaParameter);
                }
            }
            else {
                result = null;
            }
        }
        finally {

            clearAndFreeTransactionLists(ddlTransactionStatements, ddlTransactionObjects, ddlTransactionObjectsList);

            storage.reset();
        }

        return result;
    }

    final void rollback() {

        final TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements = ddlTransactionStatementsBuilder.buildOrNull();
        final TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects = ddlTransactionObjectsBuilder.buildOrNull();

        clearAndFreeTransactionLists(ddlTransactionStatements, ddlTransactionObjects, ddlTransactionObjectsList);
    }

    private void clearAndFreeTransactionLists(TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements, TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects,
            TRANSACTION_OBJECT_MUTABLE_INDEX_LIST ddlTransactionObjectsList) {

        clearAndFreeTransactionStatements(ddlTransactionStatements);
        clearAndFreeTransactionObjects(ddlTransactionObjects, ddlTransactionObjectsList);
    }

    private void clearAndFreeTransactionStatements(TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements) {

        if (ddlTransactionStatements != null) {

            final long numDDLTransactionStatements = ddlTransactionStatements.getNumElements();

            for (int i = 0; i < numDDLTransactionStatements; ++ i) {

                final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatements.get(i);

                ddlTransactionStatement.reset();

                ddlTransactionCachedObjects.ddlTransactionStatementCache.free(ddlTransactionStatement);
            }

            ddlTransactionCachedObjects.ddlTransactionStatementListAllocator.freeImmutable(ddlTransactionStatements);
        }
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

        @Override
        public Void onDroppedSchemaObject(DDLTransactionDroppedSchemaObject droppedSchemaObject, DDLTransactionCachedObjects<?, ?, ?, ?, ?> parameter) {

            parameter.ddlTransactionDroppedSchemaObjectCache.free(droppedSchemaObject);

            return null;
        }
    };

    private void clearAndFreeTransactionObjects(TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects, TRANSACTION_OBJECT_MUTABLE_INDEX_LIST ddlTransactionObjectsList) {

        if (ddlTransactionObjects != null) {

            final long numDDLTransactionObjects = ddlTransactionObjects.getNumElements();

            for (int i = 0; i < numDDLTransactionObjects; ++ i) {

                final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

                ddlTransactionObject.reset();

                ddlTransactionObject.visit(freeDDLTransactionObjectVisitor, ddlTransactionCachedObjects);
            }

            ddlTransactionCachedObjects.ddlTransactionObjectListAllocator.freeImmutable(ddlTransactionObjects);
        }

        ddlTransactionCachedObjects.ddlTransactionObjectMutableListAllocator.freeMutable(ddlTransactionObjectsList);
    }

    final void addDDLStatement(BaseSQLDDLOperationStatement ddlStatement, ISQLString sqlString, StringResolver parserStringResolver, StringManagement stringManagement)
            throws SQLValidationException {

        Objects.requireNonNull(ddlStatement);
        Objects.requireNonNull(sqlString);
        Objects.requireNonNull(parserStringResolver);
        Objects.requireNonNull(stringManagement);

        checkIsAllocatedRenamed();

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

    private DDLTransactionDroppedSchemaObject processDropTable(SQLDropTableStatement sqlDropTableStatement, StringManagement stringManagement) throws SQLValidationException {

        return processDroppedSchemaObject(DDLObjectType.TABLE, sqlDropTableStatement, stringManagement);
    }

    private DDLTransactionDroppedSchemaObject processDroppedSchemaObject(DDLObjectType ddlObjectType, BaseSQLDDLOperationStatement sqlDDLOperationStatement,
            StringManagement stringManagement) throws SQLValidationException {

        final long parsedSchemaObjectName = stringManagement.storeParsedStringRef(sqlDDLOperationStatement.getName());

        final DatabaseId databaseId = getDatabaseId();

        if (!currentSchema.containsSchemaObjectName(ddlObjectType, parsedSchemaObjectName)) {

            throw new TableDoesNotExistException(databaseId, parsedSchemaObjectName);
        }

        final long schemaSchemaObjectName = stringManagement.getHashStringRef(parsedSchemaObjectName);

        final SchemaObject existingSchemaObject = currentSchema.getSchemaMap(ddlObjectType).getSchemaObjectByName(schemaSchemaObjectName);

        final DDLTransactionDroppedSchemaObject result = ddlTransactionCachedObjects.ddlTransactionDroppedSchemaObjectCache.allocate();

        result.initialize(ddlObjectType, existingSchemaObject.getId());

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
