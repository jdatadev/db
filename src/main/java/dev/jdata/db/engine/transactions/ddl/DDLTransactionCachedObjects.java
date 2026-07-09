package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;

import dev.jdata.db.ddl.helpers.SchemaObjectIdAllocators;
import dev.jdata.db.ddl.helpers.sqltoschema.complete.scratchobjects.DDLSchemaScratchObjects;
import dev.jdata.db.engine.transactions.ddl.DDLTransaction.DDLTransactionStatement;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedListAllocator;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IIntSet;
import dev.jdata.db.utils.adt.sets.IIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class DDLTransactionCachedObjects<

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

            extends ObjectCacheNode
            implements IResettable {

    private final DDLSchemaScratchObjects<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> ddlSchemaScratchObjects;

    private final SchemaObjectIdAllocators schemaObjectIdAllocators;

    private final NodeObjectCache<DDLTransactionStatement> ddlTransactionStatementCache;

    private final NodeObjectCache<DDLTransactionAddedColumnsSchemaObject> ddlTransactionAddedColumnsSchemaObjectCache;
    private final NodeObjectCache<DDLTransactionAddedNonColumnsSchemaObject> ddlTransactionAddedNonColumnsSchemaObjectCache;
    private final NodeObjectCache<DDLTransactionColumnsDiffObject> ddlTransactionColumnsDiffObjectCache;
    private final NodeObjectCache<DDLTransactionDroppedSchemaObject> ddlTransactionDroppedSchemaObjectCache;

    private final NodeObjectCache<
                    DDLComputeEffectiveDatabaseSchemaParameter<SCHEMA_OBJECT_MUTABLE_LINKED_LIST, HEAP_COMPLETE_SCHEMA_MAP_BUILDER>
                    > ddlComputeEffectiveDatabaseSchemaParameterCache;

    private IIndexListAllocator<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?, TRANSACTION_STATEMENT_INDEX_LIST_BUILDER> ddlTransactionStatementListAllocator;
    private IIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?, TRANSACTION_OBJECT_INDEX_LIST_BUILDER> ddlTransactionObjectListAllocator;
    private IMutableDoublyLinkedListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_LINKED_LIST> ddlMutableTransactionObjectListAllocator;

    private IIntSetAllocator<INT_SET, ?, INT_SET_BUILDER> intSetAllocator;
    private IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator;

    DDLTransactionCachedObjects(AllocationType allocationType) {
        super(allocationType);

        this.ddlSchemaScratchObjects = new DDLSchemaScratchObjects<>();

        this.schemaObjectIdAllocators = SchemaObjectIdAllocators.ofInitial();

        this.ddlTransactionStatementCache = new NodeObjectCache<>(DDLTransactionStatement::new);

        this.ddlTransactionAddedColumnsSchemaObjectCache = new NodeObjectCache<>(DDLTransactionAddedColumnsSchemaObject::new);
        this.ddlTransactionAddedNonColumnsSchemaObjectCache = new NodeObjectCache<>(DDLTransactionAddedNonColumnsSchemaObject::new);
        this.ddlTransactionColumnsDiffObjectCache = new NodeObjectCache<>(DDLTransactionColumnsDiffObject::new);
        this.ddlTransactionDroppedSchemaObjectCache = new NodeObjectCache<>(DDLTransactionDroppedSchemaObject::new);

        this.ddlComputeEffectiveDatabaseSchemaParameterCache = new NodeObjectCache<>(DDLComputeEffectiveDatabaseSchemaParameter::new);
    }

    final void initialize(
            IIndexListAllocator<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?, TRANSACTION_STATEMENT_INDEX_LIST_BUILDER> ddlTransactionStatementListAllocator,
            IIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?, TRANSACTION_OBJECT_INDEX_LIST_BUILDER> ddlTransactionObjectListAllocator,
            IMutableDoublyLinkedListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_LINKED_LIST> ddlMutableTransactionObjectListAllocator,
            IIntSetAllocator<INT_SET, ?, INT_SET_BUILDER> intSetAllocator, IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator) {

        this.ddlTransactionStatementListAllocator = Initializable.checkNotYetInitialized(this.ddlTransactionStatementListAllocator, ddlTransactionStatementListAllocator);
        this.ddlTransactionObjectListAllocator = Initializable.checkNotYetInitialized(this.ddlTransactionObjectListAllocator, ddlTransactionObjectListAllocator);
        this.ddlMutableTransactionObjectListAllocator = Initializable.checkNotYetInitialized(this.ddlMutableTransactionObjectListAllocator,
                ddlMutableTransactionObjectListAllocator);
        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.columnIndexListAllocator = Objects.requireNonNull(columnIndexListAllocator);
    }

    @Override
    public final void reset() {

        this.ddlTransactionStatementListAllocator = Initializable.checkResettable(ddlTransactionStatementListAllocator);
        this.ddlTransactionObjectListAllocator = Initializable.checkResettable(ddlTransactionObjectListAllocator);
        this.ddlMutableTransactionObjectListAllocator = Initializable.checkResettable(ddlMutableTransactionObjectListAllocator);
        this.intSetAllocator = Initializable.checkResettable(intSetAllocator);
        this.columnIndexListAllocator = Initializable.checkResettable(columnIndexListAllocator);
    }

    final DDLTransactionStatement allocateDDLTransactionStatement() {

        return ddlTransactionStatementCache.allocate();
    }

    final void freeDDLTransactionStatement(DDLTransactionStatement ddlTransactionStatement) {

        Objects.requireNonNull(ddlTransactionStatement);

        ddlTransactionStatementCache.free(ddlTransactionStatement);
    }

    final DDLTransactionAddedColumnsSchemaObject allocateDDLTransactionAddedColumnsSchemaObject() {

        return ddlTransactionAddedColumnsSchemaObjectCache.allocate();
    }

    final void freeDDLTransactionAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject ddlTransactionAddedColumnsSchemaObject) {

        Objects.requireNonNull(ddlTransactionAddedColumnsSchemaObject);

        ddlTransactionAddedColumnsSchemaObjectCache.free(ddlTransactionAddedColumnsSchemaObject);
    }

    final DDLTransactionAddedNonColumnsSchemaObject allocateDDLTransactionAddedNonColumnsSchemaObject() {

        return ddlTransactionAddedNonColumnsSchemaObjectCache.allocate();
    }

    final void freeDDLTransactionAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject ddlTransactionAddedNonColumnsSchemaObject) {

        Objects.requireNonNull(ddlTransactionAddedNonColumnsSchemaObject);

        ddlTransactionAddedNonColumnsSchemaObjectCache.free(ddlTransactionAddedNonColumnsSchemaObject);
    }

    final DDLTransactionColumnsDiffObject allocateDDLTransactionColumnsDiffObject() {

        return ddlTransactionColumnsDiffObjectCache.allocate();
    }

    final void freeDDLTransactionColumnsDiffObject(DDLTransactionColumnsDiffObject ddlTransactionColumnsDiffObject) {

        Objects.requireNonNull(ddlTransactionColumnsDiffObject);

        ddlTransactionColumnsDiffObjectCache.free(ddlTransactionColumnsDiffObject);
    }

    final DDLTransactionDroppedSchemaObject allocateDDLTransactionDroppedSchemaObject() {

        return ddlTransactionDroppedSchemaObjectCache.allocate();
    }

    final void freeDDLTransactionDroppedSchemaObject(DDLTransactionDroppedSchemaObject ddlTransactionDroppedSchemaObject) {

        Objects.requireNonNull(ddlTransactionDroppedSchemaObject);

        ddlTransactionDroppedSchemaObjectCache.free(ddlTransactionDroppedSchemaObject);
    }

    final DDLComputeEffectiveDatabaseSchemaParameter<SCHEMA_OBJECT_MUTABLE_LINKED_LIST, HEAP_COMPLETE_SCHEMA_MAP_BUILDER> allocateDDLComputeEffectiveDatabaseSchemaParameter() {

        return ddlComputeEffectiveDatabaseSchemaParameterCache.allocate();
    }

    final void freeDDLComputeEffectiveDatabaseSchemaParameter(
            DDLComputeEffectiveDatabaseSchemaParameter<SCHEMA_OBJECT_MUTABLE_LINKED_LIST, HEAP_COMPLETE_SCHEMA_MAP_BUILDER> ddlComputeEffectiveDatabaseSchemaParameter) {

        Objects.requireNonNull(ddlComputeEffectiveDatabaseSchemaParameter);

        ddlComputeEffectiveDatabaseSchemaParameterCache.free(ddlComputeEffectiveDatabaseSchemaParameter);
    }

    DDLSchemaScratchObjects<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> getDDLSchemaScratchObjects() {
        return ddlSchemaScratchObjects;
    }

    final SchemaObjectIdAllocators getSchemaObjectIdAllocators() {
        return schemaObjectIdAllocators;
    }

    final IIndexListAllocator<DDLTransactionStatement, TRANSACTION_STATEMENT_INDEX_LIST, ?, TRANSACTION_STATEMENT_INDEX_LIST_BUILDER> getDDLTransactionStatementListAllocator() {
        return ddlTransactionStatementListAllocator;
    }

    final IIndexListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_INDEX_LIST, ?, TRANSACTION_OBJECT_INDEX_LIST_BUILDER> getDDLTransactionObjectListAllocator() {
        return ddlTransactionObjectListAllocator;
    }

    final IMutableDoublyLinkedListAllocator<DDLTransactionObject, TRANSACTION_OBJECT_MUTABLE_LINKED_LIST> getDDLMutableTransactionObjectListAllocator() {
        return ddlMutableTransactionObjectListAllocator;
    }

    final IIntSetAllocator<INT_SET, ?, INT_SET_BUILDER> getIntSetAllocator() {
        return intSetAllocator;
    }

    final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }
}
