
package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.DDLAlterTableSchemasHelper;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.DDLCreateTableSchemasHelper;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.ISQLToSchemaStringManagement;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessAlterTableScratchObject;
import dev.jdata.db.ddl.helpers.sqltoschema.statements.scratchobjects.ProcessCreateTableScratchObject;
import dev.jdata.db.ddl.model.diff.TableDiff;
import dev.jdata.db.engine.database.strings.IStringWriter;
import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionStatement;
import dev.jdata.db.engine.transactions.ddl.DDLTransactionEffectiveSchemaHelper.DDLComputeEffectiveDatabaseSchemaScratchObject;
import dev.jdata.db.engine.validation.exceptions.SQLValidationException;
import dev.jdata.db.engine.validation.exceptions.SchemaObjectAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.TableAlreadyExistsException;
import dev.jdata.db.engine.validation.exceptions.TableDoesNotExistException;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.storage.IDatabaseSchemaSerializer;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory.IDatabaseSchemaStorage;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.ast.statements.SQLStatementAdapter;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.sql.strings.ISQLString;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.Node;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IIntSet;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.instances.Instances;

abstract class DDLTransaction<

                TRANSACTION_STATEMENT_INDEX_LIST extends IIndexList<DDLTransactionStatement>,
                TRANSACTION_STATEMENT_INDEX_LIST_BUILDER extends IIndexListBuilder<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?>,
                TRANSACTION_OBJECT_INDEX_LIST extends IIndexList<DDLTransactionObject>,
                TRANSACTION_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?>,
                TRANSACTION_OBJECT_MUTABLE_LINKED_LIST extends IMutableDoublyLinkedList<DDLTransactionObject>,
                SCHEMA_OBJECT_MUTABLE_LINKED_LIST extends IMutableDoublyLinkedList<SchemaObject>,
                HEAP_COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<?, ? extends IHeapCompleteSchemaMap, HEAP_COMPLETE_SCHEMA_MAP_BUILDER>,
                INT_SET extends IIntSet,
                INT_SET_BUILDER extends IIntSetBuilder<INT_SET, ? extends IHeapIntSet>,
                HEAP_COLUMN_INDEX_LIST extends IHeapIndexList<Column>,
                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, HEAP_COLUMN_INDEX_LIST>>

        extends Allocatable
        implements IResettable {

    public static final class DDLTransactionSerializationParameter<E extends Exception> extends ObjectCacheNode implements IResettable {

        private IDatabaseSchemaStorageFactory<E> databaseSchemaStorage;
        private IDatabaseSchemaSerializer databaseSchemaSerializer;
        private ISQLOutputter<E> sqlOutputter;

        public DDLTransactionSerializationParameter(AllocationType allocationType) {
            super(allocationType);
        }

        public void initialize(IDatabaseSchemaStorageFactory<E> databaseSchemaStorage, IDatabaseSchemaSerializer databaseSchemaSerializer, ISQLOutputter<E> sqlOutputter) {

            this.databaseSchemaStorage = Initializable.checkNotYetInitialized(this.databaseSchemaStorage, databaseSchemaStorage);
            this.databaseSchemaSerializer = Initializable.checkNotYetInitialized(this.databaseSchemaSerializer, databaseSchemaSerializer);
            this.sqlOutputter = Initializable.checkNotYetInitialized(this.sqlOutputter, sqlOutputter);
        }

        @Override
        public void reset() {

            this.databaseSchemaStorage = Initializable.checkResettable(databaseSchemaStorage);
            this.databaseSchemaSerializer = Initializable.checkResettable(databaseSchemaSerializer);
            this.sqlOutputter = Initializable.checkResettable(sqlOutputter);
        }
    }

    static final class DDLTransactionStatement extends ObjectCacheNode implements IResettable {

        private BaseSQLDDLOperationStatement sqlStatement;
        private StringResolver sqlStatementStringResolver;
        private ISQLString sqlString;

        DDLTransactionStatement(AllocationType allocationType) {
            super(allocationType);
        }

        void initialize(BaseSQLDDLOperationStatement sqlStatement, StringResolver sqlStatementStringResolver, ISQLString sqlString) {

            this.sqlStatement = Initializable.checkNotYetInitialized(this.sqlStatement, sqlStatement);
            this.sqlStatementStringResolver = Initializable.checkNotYetInitialized(this.sqlStatementStringResolver, sqlStatementStringResolver);
            this.sqlString = Initializable.checkNotYetInitialized(this.sqlString, sqlString);
        }

        @Override
        public void reset() {

            this.sqlStatement = Initializable.checkResettable(sqlStatement);
            this.sqlStatementStringResolver = Initializable.checkResettable(sqlStatementStringResolver);
            this.sqlString = Initializable.checkResettable(sqlString);
        }
    }

    private final SQLStatementAdapter<ISQLToSchemaStringManagement, DDLTransactionObject, SQLValidationException> ddlStatementVisitor
            = new SQLStatementAdapter<ISQLToSchemaStringManagement, DDLTransactionObject, SQLValidationException>() {

        @Override
        public DDLTransactionObject onCreateTable(SQLCreateTableStatement createTableStatement, ISQLToSchemaStringManagement parameter) throws TableAlreadyExistsException {

            return DDLTransaction.this.processCreateTable(createTableStatement, parameter);
        }

        @Override
        public DDLTransactionObject onAlterTable(SQLAlterTableStatement alterTableStatement, ISQLToSchemaStringManagement parameter) throws SQLValidationException {

            return DDLTransaction.this.processAlterTable(alterTableStatement, parameter);
        }

        @Override
        public DDLTransactionObject onDropTable(SQLDropTableStatement dropTableStatement, ISQLToSchemaStringManagement parameter) throws SQLValidationException {

            return DDLTransaction.this.processDropTable(dropTableStatement, parameter);
        }
    };

    private final DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject;

    private IStringWriter schemaStringWriter;

    private DDLTransactionCachedObjects<

                    TRANSACTION_STATEMENT_INDEX_LIST,
                    TRANSACTION_STATEMENT_INDEX_LIST_BUILDER,
                    TRANSACTION_OBJECT_INDEX_LIST,
                    TRANSACTION_OBJECT_INDEX_LIST_BUILDER,
                    TRANSACTION_OBJECT_MUTABLE_LINKED_LIST,
                    SCHEMA_OBJECT_MUTABLE_LINKED_LIST,
                    HEAP_COMPLETE_SCHEMA_MAP_BUILDER,
                    INT_SET,
                    INT_SET_BUILDER,
                    HEAP_COLUMN_INDEX_LIST,
                    COLUMN_INDEX_LIST_BUILDER> ddlTransactionCachedObjects;

    private IEffectiveDatabaseSchema currentSchema;

    private TRANSACTION_STATEMENT_INDEX_LIST_BUILDER ddlTransactionStatementsBuilder;
    private TRANSACTION_OBJECT_INDEX_LIST_BUILDER ddlTransactionObjectsBuilder;

    private TRANSACTION_OBJECT_MUTABLE_LINKED_LIST ddlTransactionObjectsList;

    private long scratchName;
    private int scratchSchemObjectId;

    DDLTransaction(AllocationType allocationType) {
        super(allocationType);

        this.ddlComputeEffectiveDatabaseSchemaScratchObject = new DDLComputeEffectiveDatabaseSchemaScratchObject<>(allocationType);
    }

    final void initialize(IEffectiveDatabaseSchema currentSchema, IStringWriter schemaStringWriter,
            DDLTransactionCachedObjects<

                            TRANSACTION_STATEMENT_INDEX_LIST,
                            TRANSACTION_STATEMENT_INDEX_LIST_BUILDER,
                            TRANSACTION_OBJECT_INDEX_LIST,
                            TRANSACTION_OBJECT_INDEX_LIST_BUILDER,
                            TRANSACTION_OBJECT_MUTABLE_LINKED_LIST,
                            SCHEMA_OBJECT_MUTABLE_LINKED_LIST,
                            HEAP_COMPLETE_SCHEMA_MAP_BUILDER,
                            INT_SET,
                            INT_SET_BUILDER,
                            HEAP_COLUMN_INDEX_LIST,
                            COLUMN_INDEX_LIST_BUILDER> ddlCachedObjects) {

        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(schemaStringWriter);
        Objects.requireNonNull(ddlCachedObjects);

        checkIsAllocatedRenamed();

        this.currentSchema = Initializable.checkNotYetInitialized(this.currentSchema, currentSchema);
        this.schemaStringWriter = Initializable.checkNotYetInitialized(this.schemaStringWriter, schemaStringWriter);
        this.ddlTransactionCachedObjects = Initializable.checkNotYetInitialized(this.ddlTransactionCachedObjects, ddlCachedObjects);

        this.ddlTransactionStatementsBuilder = Initializable.checkNotYetInitialized(this.ddlTransactionStatementsBuilder,
                ddlCachedObjects.getDDLTransactionStatementListAllocator().createBuilder());

        this.ddlTransactionObjectsBuilder = Initializable.checkNotYetInitialized(this.ddlTransactionObjectsBuilder,
                ddlCachedObjects.getDDLTransactionObjectListAllocator().createBuilder());

        this.ddlTransactionObjectsList = Initializable.checkNotYetInitialized(this.ddlTransactionObjectsList,
                ddlCachedObjects.getDDLMutableTransactionObjectListAllocator().createMutable(0L));

        ddlTransactionCachedObjects.getSchemaObjectIdAllocators().initialize(currentSchema);
    }

    @Override
    public final void reset() {

        checkIsAllocatedRenamed();

        ddlTransactionCachedObjects.getDDLTransactionStatementListAllocator().freeBuilder(ddlTransactionStatementsBuilder);
        ddlTransactionCachedObjects.getDDLTransactionObjectListAllocator().freeBuilder(ddlTransactionObjectsBuilder);

        this.currentSchema = Initializable.checkResettable(currentSchema);
        this.schemaStringWriter = Initializable.checkResettable(schemaStringWriter);
        this.ddlTransactionCachedObjects = Initializable.checkResettable(ddlTransactionCachedObjects);

        this.ddlTransactionStatementsBuilder = Initializable.checkResettable(ddlTransactionStatementsBuilder);
        this.ddlTransactionObjectsBuilder = Initializable.checkResettable(ddlTransactionObjectsBuilder);
        this.ddlTransactionObjectsList = Initializable.checkResettable(ddlTransactionObjectsList);
    }

    public final <T extends IMutableDoublyLinkedList<SchemaObject>, U extends ICompleteSchemaMapBuilder<?, ? extends IHeapCompleteSchemaMap, U>, E extends Exception>
    IHeapEffectiveDatabaseSchema commit(DatabaseSchemaVersion databaseSchemaVersion, DDLTransactionSerializationParameter<E> ddlTransactionSerializationParameter,
            DDLComputeEffectiveDatabaseSchemaParameter<T, U> ddlComputeEffectiveDatabaseSchemaParameter) throws E {

        Objects.requireNonNull(databaseSchemaVersion);
        Objects.requireNonNull(ddlTransactionSerializationParameter);

        final IHeapEffectiveDatabaseSchema result;

        final IDatabaseSchemaStorage<E> storage = ddlTransactionSerializationParameter.databaseSchemaStorage.createSchemaDiffStorage(databaseSchemaVersion);

        final TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements = ddlTransactionStatementsBuilder.buildOrNull();
        final TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects = ddlTransactionObjectsBuilder.buildOrNull();

        try {
            if (Instances.areBothNotNullOrBothNullOrThrowException(ddlTransactionStatements, ddlTransactionObjects)) {

                final long numDDLTransactionStatements = ddlTransactionStatements.getNumElements();

                for (int i = 0; i < numDDLTransactionStatements; ++ i) {

                    final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatements.get(i);

                    storage.storeSchemaDiffStatement(ddlTransactionStatement.sqlStatement, ddlTransactionStatement.sqlStatementStringResolver,
                            ddlTransactionStatement.sqlString);
                }

                final DDLComputeEffectiveDatabaseSchemaScratchObject<T, U> ddlComputeEffectiveDatabaseSchemaScratchObject = getDDLComputeEffectiveDatabaseSchemaScratchObject();

                try {
                    ddlComputeEffectiveDatabaseSchemaScratchObject.initialize(ddlTransactionObjects, ddlComputeEffectiveDatabaseSchemaParameter);

                    result = DDLTransactionEffectiveSchemaHelper.computeEffectiveDatabaseSchema(getDatabaseId(), databaseSchemaVersion, currentSchema,
                            ddlComputeEffectiveDatabaseSchemaScratchObject);

                    storage.completeSchemaDiff(result, ddlTransactionSerializationParameter.databaseSchemaSerializer, schemaStringWriter,
                            ddlTransactionSerializationParameter.sqlOutputter);
                }
                finally {

//                    ddlTransactionCachedObjects.freeDDLComputeEffectiveDatabaseSchemaParameter(ddlComputeEffectiveDatabaseSchemaParameter);
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
            TRANSACTION_OBJECT_MUTABLE_LINKED_LIST ddlTransactionObjectsList) {

        clearAndFreeTransactionStatements(ddlTransactionStatements);
        clearAndFreeTransactionObjects(ddlTransactionObjects, ddlTransactionObjectsList);
    }

    private void clearAndFreeTransactionStatements(TRANSACTION_STATEMENT_INDEX_LIST ddlTransactionStatements) {

        if (ddlTransactionStatements != null) {

            final long numDDLTransactionStatements = ddlTransactionStatements.getNumElements();

            for (int i = 0; i < numDDLTransactionStatements; ++ i) {

                final DDLTransactionStatement ddlTransactionStatement = ddlTransactionStatements.get(i);

                ddlTransactionStatement.reset();

                ddlTransactionCachedObjects.freeDDLTransactionStatement(ddlTransactionStatement);
            }

            ddlTransactionCachedObjects.getDDLTransactionStatementListAllocator().freeImmutable(ddlTransactionStatements);
        }
    }

    private static final DDLTransactionObjectVisitor<DDLTransactionCachedObjects<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, Void> freeDDLTransactionObjectVisitor
            = new DDLTransactionObjectVisitor<DDLTransactionCachedObjects<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, Void>() {

        @Override
        public Void onAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject addedColumnsSchemaObject,
                DDLTransactionCachedObjects<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> parameter) {

            parameter.freeDDLTransactionAddedColumnsSchemaObject(addedColumnsSchemaObject);

            return null;
        }

        @Override
        public Void onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject,
                DDLTransactionCachedObjects<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> parameter) {

            parameter.freeDDLTransactionAddedNonColumnsSchemaObject(addedNonColumnsSchemaObject);

            return null;
        }

        @Override
        public Void onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject, DDLTransactionCachedObjects<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> parameter) {

            parameter.freeDDLTransactionColumnsDiffObject(columnsDiffObject);

            return null;
        }

        @Override
        public Void onDroppedSchemaObject(DDLTransactionDroppedSchemaObject droppedSchemaObject, DDLTransactionCachedObjects<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> parameter) {

            parameter.freeDDLTransactionDroppedSchemaObject(droppedSchemaObject);

            return null;
        }
    };

    private void clearAndFreeTransactionObjects(TRANSACTION_OBJECT_INDEX_LIST ddlTransactionObjects, TRANSACTION_OBJECT_MUTABLE_LINKED_LIST ddlTransactionObjectsList) {

        if (ddlTransactionObjects != null) {

            final long numDDLTransactionObjects = ddlTransactionObjects.getNumElements();

            for (int i = 0; i < numDDLTransactionObjects; ++ i) {

                final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

                ddlTransactionObject.reset();

                ddlTransactionObject.visit(freeDDLTransactionObjectVisitor, ddlTransactionCachedObjects);
            }

            ddlTransactionCachedObjects.getDDLTransactionObjectListAllocator().freeImmutable(ddlTransactionObjects);
        }

        ddlTransactionCachedObjects.getDDLMutableTransactionObjectListAllocator().freeMutable(ddlTransactionObjectsList);
    }

    final void addDDLStatement(BaseSQLDDLOperationStatement ddlStatement, StringResolver sqlStatementStringResolver, ISQLString sqlString,
            ISQLToSchemaStringManagement sqlToSchemaStringManagement) throws SQLValidationException {

        Objects.requireNonNull(ddlStatement);
        Objects.requireNonNull(sqlStatementStringResolver);
        Objects.requireNonNull(sqlString);
        Objects.requireNonNull(sqlToSchemaStringManagement);

        checkIsAllocatedRenamed();

        final DDLTransactionObject ddlTransactionObject = ddlStatement.visit(ddlStatementVisitor, sqlToSchemaStringManagement);

        ddlTransactionObjectsBuilder.addTail(ddlTransactionObject);
        ddlTransactionObjectsList.addTail(ddlTransactionObject);

        final DDLTransactionStatement ddlTransactionStatement = ddlTransactionCachedObjects.allocateDDLTransactionStatement();

        ddlTransactionStatement.initialize(ddlStatement, sqlStatementStringResolver, sqlString);

        ddlTransactionStatementsBuilder.addTail(ddlTransactionStatement);
    }

    private DDLTransactionAddedColumnsSchemaObject processCreateTable(SQLCreateTableStatement sqlCreateTableStatement, ISQLToSchemaStringManagement sqlToSchemaStringManagement)
            throws TableAlreadyExistsException {

        final DDLTransactionAddedColumnsSchemaObject result;

        final long storedTableName = sqlToSchemaStringManagement.storeParsedStringRef(sqlCreateTableStatement.getName());

        checkSchemaObjectAlreadyExists(DDLObjectType.TABLE, storedTableName, TableAlreadyExistsException::new);

        final DDLSchemaScratchObjects<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> ddlSchemaScratchObjects = ddlTransactionCachedObjects.getDDLSchemaScratchObjects();

        final ProcessCreateTableScratchObject processCreateTableScratchObject = ddlSchemaScratchObjects.allocateProcessCreateTableScratchObject();

        try {
            final Table table = DDLCreateTableSchemasHelper.processCreateTable(sqlCreateTableStatement, sqlToSchemaStringManagement,
                    ddlTransactionCachedObjects.getColumnIndexListAllocator(), processCreateTableScratchObject, ddlTransactionCachedObjects.getSchemaObjectIdAllocators(),
                    a -> a.allocate(DDLObjectType.TABLE));

            result = ddlTransactionCachedObjects.allocateDDLTransactionAddedColumnsSchemaObject();

            result.initialize(table);
        }
        finally {

            ddlSchemaScratchObjects.freeProcessCreateTableScratchObject(processCreateTableScratchObject);
        }

        return result;
    }

    private DDLTransactionColumnsDiffObject processAlterTable(SQLAlterTableStatement sqlAlterTableStatement, ISQLToSchemaStringManagement sqlToSchemaStringManagement)
            throws SQLValidationException {

        final DDLTransactionColumnsDiffObject result;

        final long parsedTableName = sqlAlterTableStatement.getName();
        final long storedSQLTableName = sqlToSchemaStringManagement.storeParsedStringRef(parsedTableName);

        final DDLObjectType ddlObjectType = DDLObjectType.TABLE;

        final DatabaseId databaseId = getDatabaseId();

        final long storedHashTableName = sqlToSchemaStringManagement.storeHashStringRefFromStoredSQLStringRef(storedSQLTableName);

        final Table existingTable = currentSchema.getSchemaObjectByName(ddlObjectType, storedHashTableName);

        if (existingTable == null) {

            throw new TableDoesNotExistException(databaseId, parsedTableName);
        }

        final DDLSchemaScratchObjects<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> ddlSchemaScratchObjects = ddlTransactionCachedObjects.getDDLSchemaScratchObjects();

        final ProcessAlterTableScratchObject<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> processAlterTableScratchObject
                = ddlSchemaScratchObjects.allocateProcessAlterTableScratchObject();

        try {
            final TableDiff tableDiff = DDLAlterTableSchemasHelper.processAlterTable(sqlAlterTableStatement, databaseId, existingTable, sqlToSchemaStringManagement,
                    ddlTransactionCachedObjects.getIntSetAllocator(), ddlTransactionCachedObjects.getColumnIndexListAllocator(), processAlterTableScratchObject);

            result = ddlTransactionCachedObjects.allocateDDLTransactionColumnsDiffObject();

            result.initialize(tableDiff);
        }
        finally {

            ddlSchemaScratchObjects.freeProcessAlterTableScratchObject(processAlterTableScratchObject);
        }

        return result;
    }

    private DDLTransactionDroppedSchemaObject processDropTable(SQLDropTableStatement sqlDropTableStatement, ISQLToSchemaStringManagement sqlToSchemaStringManagement)
            throws SQLValidationException {

        return processDroppedSchemaObject(DDLObjectType.TABLE, sqlDropTableStatement, sqlToSchemaStringManagement);
    }

    private DDLTransactionDroppedSchemaObject processDroppedSchemaObject(DDLObjectType ddlObjectType, BaseSQLDDLOperationStatement sqlDDLOperationStatement,
            ISQLToSchemaStringManagement sqlToSchemaStringManagement) throws SQLValidationException {

        final long storedSchemaObjectName = sqlToSchemaStringManagement.storeParsedStringRef(sqlDDLOperationStatement.getName());

        final DatabaseId databaseId = getDatabaseId();

        if (!schemaObjectAlreadyExists(ddlObjectType, storedSchemaObjectName)) {

            throw new TableDoesNotExistException(databaseId, storedSchemaObjectName);
        }

        final long hashSchemaObjectName = sqlToSchemaStringManagement.storeHashStringRefFromStoredSQLStringRef(storedSchemaObjectName);

        SchemaObject existingSchemaObject = currentSchema.getSchemaObjectByName(ddlObjectType, hashSchemaObjectName);

        if (existingSchemaObject == null) {

            this.scratchName = hashSchemaObjectName;

            final Node<DDLTransactionObject> ddlTransactionObjectNode
                    = (Node<DDLTransactionObject>)ddlTransactionObjectsList.findAtMostOneNode(this, DDLTransaction::matchAddedSchemaObject);

            if (ddlTransactionObjectNode != null) {

                existingSchemaObject = ((DDLTransactionAddedSchemaObject<?>)ddlTransactionObjectNode.getElement()).getSchemaObject();

                ddlTransactionObjectsList.removeNode(ddlTransactionObjectNode);
            }
            else {
                throw new TableDoesNotExistException(databaseId, hashSchemaObjectName);
            }
        }

        final DDLTransactionDroppedSchemaObject result = ddlTransactionCachedObjects.allocateDDLTransactionDroppedSchemaObject();

        result.initialize(ddlObjectType, existingSchemaObject.getId());

        return result;
    }

    @FunctionalInterface
    private interface SchemaObjectAlreadyExistsExceptionFactory<E extends SchemaObjectAlreadyExistsException> {

        E create(DatabaseId databaseId, long storedSchemaObjectName);
    }

    private <E extends SchemaObjectAlreadyExistsException> void checkSchemaObjectAlreadyExists(DDLObjectType ddlObjectType, long storedSchemaObjectName,
            SchemaObjectAlreadyExistsExceptionFactory<E> schemaObjectAlreadyExistsExceptionFactory) throws E {

        if (schemaObjectAlreadyExists(ddlObjectType, storedSchemaObjectName)) {

            throw schemaObjectAlreadyExistsExceptionFactory.create(getDatabaseId(), storedSchemaObjectName);
        }
    }

    private boolean schemaObjectAlreadyExists(DDLObjectType ddlObjectType, long storedSchemaObjectName) {

        final boolean result;

        final SchemaObject schemaObjectFromCurrentSchema = currentSchema.getSchemaObjectByName(ddlObjectType, storedSchemaObjectName);

        if (schemaObjectFromCurrentSchema != null) {

            result = ddlTransactionObjectsList.isEmpty()
                    ? true
                    : transactionContainsDroppedSchemaObject(ddlTransactionObjectsList.getHeadNode(), schemaObjectFromCurrentSchema);
        }
        else {
            this.scratchName = storedSchemaObjectName;

            final Node<DDLTransactionObject> addedSchemaObjectNode = ddlTransactionObjectsList.findAtMostOneNode(this, DDLTransaction::matchAddedSchemaObject);

            if (addedSchemaObjectNode != null) {

                final DDLTransactionAddedSchemaObject<?> addedSchemaObject = (DDLTransactionAddedSchemaObject<?>)addedSchemaObjectNode.getElement();

                result = transactionContainsDroppedSchemaObject(addedSchemaObjectNode, addedSchemaObject.getSchemaObject());
            }
            else {
                result = false;
            }
        }

        return result;
    }

    private boolean transactionContainsDroppedSchemaObject(Node<DDLTransactionObject> startNode, SchemaObject addedSchemaObject) {

        this.scratchSchemObjectId = addedSchemaObject.getId();

        return !ddlTransactionObjectsList.contains(startNode, this, (o, i) -> DDLTransactionEffectiveSchemaHelper.matchDroppedSchemaObject(o, i.scratchSchemObjectId));
    }

    private SchemaObject getSchemaObjectOrNullFromCurrentSchemaOrTransactionObjectsList(DDLObjectType ddlObjectType, long storedSchemaObjectName) {

        SchemaObject result;

        result = currentSchema.getSchemaObjectByName(ddlObjectType, storedSchemaObjectName);

        if (result == null) {

            this.scratchName = storedSchemaObjectName;

            final DDLTransactionAddedSchemaObject<?> ddlTransactionObject
                    = (DDLTransactionAddedSchemaObject<?>)ddlTransactionObjectsList.findAtMostOne(this, DDLTransaction::matchAddedSchemaObject);

            if (ddlTransactionObject != null) {

                result = ddlTransactionObject.getSchemaObject();
            }
        }

        return result;
    }

    private static boolean matchAddedSchemaObject(DDLTransactionObject ddlTransactionObject, DDLTransaction<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> ddlTransaction) {

        return ddlTransactionObject instanceof DDLTransactionAddedColumnsSchemaObject
                ? ((DDLTransactionAddedColumnsSchemaObject)ddlTransactionObject).getSchemaObject().getStoredName() == ddlTransaction.scratchName
                : false;
    }

    private DatabaseId getDatabaseId() {

        return currentSchema.getDatabaseId();
    }


    @SuppressWarnings("unchecked")
    private <T extends IMutableDoublyLinkedList<SchemaObject>, U extends ICompleteSchemaMapBuilder<?, ? extends IHeapCompleteSchemaMap, U>>
    DDLComputeEffectiveDatabaseSchemaScratchObject<T, U> getDDLComputeEffectiveDatabaseSchemaScratchObject() {
        return (DDLComputeEffectiveDatabaseSchemaScratchObject<T, U>)ddlComputeEffectiveDatabaseSchemaScratchObject;
    }
}
