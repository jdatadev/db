package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;
import java.util.function.BiFunction;

import dev.jdata.db.ddl.model.diff.ColumnsObjectDiff;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedList;
import dev.jdata.db.utils.adt.lists.IMutableDoublyLinkedListAllocator;
import dev.jdata.db.utils.adt.lists.Node;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

class DDLTransactionEffectiveSchemaHelper {

    private static final boolean ASSERT = AssertionContants.ASSERT_DDL_TRANSACTION_EFFECTIVE_SCHEMA_HELPER;

    static final class DDLComputeEffectiveDatabaseSchemaScratchObject<

                    T extends IMutableDoublyLinkedList<SchemaObject>,
                    U extends ICompleteSchemaMapBuilder<?, ? extends IHeapCompleteSchemaMap, U>>

            extends ObjectCacheNode implements IResettable {

        private IIndexList<DDLTransactionObject> ddlTransactionObjects;
        private DDLComputeEffectiveDatabaseSchemaParameter<T, U> ddlComputeEffectiveDatabaseSchemaParameter;

        private IMutableDoublyLinkedList<SchemaObject> schemaObjectMutableLinkedList;

        private int scratchIndex;
        private int scratchSchemaObjectId;

        DDLComputeEffectiveDatabaseSchemaScratchObject(AllocationType allocationType) {
            super(allocationType);
        }

        void initialize(IIndexList<DDLTransactionObject> ddlTransactionObjects, DDLComputeEffectiveDatabaseSchemaParameter<T, U> ddlComputeEffectiveDatabaseSchemaParameter) {

            this.ddlTransactionObjects = Initializable.checkNotYetInitialized(this.ddlTransactionObjects, ddlTransactionObjects);
            this.ddlComputeEffectiveDatabaseSchemaParameter = Initializable.checkNotYetInitialized(this.ddlComputeEffectiveDatabaseSchemaParameter,
                    ddlComputeEffectiveDatabaseSchemaParameter);

            resetWorkerParameters();
        }

        @Override
        public void reset() {

            this.ddlTransactionObjects = Initializable.checkResettable(ddlTransactionObjects);

            resetWorkerParameters();
        }

        private void resetWorkerParameters() {

            this.scratchIndex = -1;
        }
    }

    private static final DDLTransactionObjectVisitor<DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?>, Void> processTransactionObjectVisitor
            = new DDLTransactionObjectVisitor<DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?>, Void>() {

        @Override
        public Void onAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject addedColumnsSchemaObject,
                DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

            addSchemaObject(addedColumnsSchemaObject, ddlComputeEffectiveDatabaseSchemaScratchObject);

            return null;
        }

        @Override
        public Void onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject,
                DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

            addSchemaObject(addedNonColumnsSchemaObject, ddlComputeEffectiveDatabaseSchemaScratchObject);

            return null;
        }

        @Override
        public Void onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject,
                DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

            applyColumnsDiff(columnsDiffObject, ddlComputeEffectiveDatabaseSchemaScratchObject.schemaObjectMutableLinkedList,
                    ddlComputeEffectiveDatabaseSchemaScratchObject.ddlComputeEffectiveDatabaseSchemaParameter.getColumnsObjectDiffApplier());

            return null;
        }

        @Override
        public Void onDroppedSchemaObject(DDLTransactionDroppedSchemaObject droppedSchemaObject,
                DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

            applyDroppedSchemaObject(droppedSchemaObject, ddlComputeEffectiveDatabaseSchemaScratchObject.schemaObjectMutableLinkedList);

            return null;
        }

        private void addSchemaObject(DDLTransactionAddedSchemaObject<?> ddlTransactionAddedSchemaObject,
                DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

            final int transactionObjectIndex = ddlComputeEffectiveDatabaseSchemaScratchObject.scratchIndex;
            final IIndexList<DDLTransactionObject> ddlTransactionObjects = ddlComputeEffectiveDatabaseSchemaScratchObject.ddlTransactionObjects;

            final int numDDLTransationObjects = IOnlyElementsView.intNumElements(ddlTransactionObjects);

            if (ASSERT) {

                Assertions.isLessThan(transactionObjectIndex, numDDLTransationObjects);
                Assertions.areSameInstances(ddlTransactionAddedSchemaObject, ddlTransactionObjects.get(transactionObjectIndex));
            }

            final SchemaObject addedSchemaObject = ddlTransactionAddedSchemaObject.getSchemaObject();

            ddlComputeEffectiveDatabaseSchemaScratchObject.schemaObjectMutableLinkedList.addTail(addedSchemaObject);
        }

        private void applyColumnsDiff(DDLTransactionColumnsDiffObject columnsDiffObject, IMutableDoublyLinkedList<SchemaObject> schemaObjectMutableLinkedList,
                BiFunction<ColumnsObject, ColumnsObjectDiff, ColumnsObject> columnsObjectDiffApplier) {

            final ColumnsObjectDiff columnsObjectDiff = columnsDiffObject.getSchemaObjectDiff();

            final Node<SchemaObject> columnsObjectNode = schemaObjectMutableLinkedList.findExactlyOneNode(columnsObjectDiff, (o, d) -> o.getId() == d.getId());

            final ColumnsObject existingColumnsObject = (ColumnsObject)columnsObjectNode.getElement();

            final ColumnsObject updatedColumnsObject = columnsObjectDiffApplier.apply(existingColumnsObject, columnsObjectDiff);

            columnsObjectNode.setElement(updatedColumnsObject);
        }

        private void applyDroppedSchemaObject(DDLTransactionDroppedSchemaObject droppedSchemaObject, IMutableDoublyLinkedList<SchemaObject> schemaObjectMutableLinkedList) {

            schemaObjectMutableLinkedList.removeExactlyOneNode(droppedSchemaObject, (o, d) -> o.getId() == d.getSchemaObjectId());
        }
    };

    private static boolean matchDroppedSchemaObject(DDLTransactionObject ddlTransactionObject,
            DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

        return matchDroppedSchemaObject(ddlTransactionObject, ddlComputeEffectiveDatabaseSchemaScratchObject.scratchSchemaObjectId);
    }

    static boolean matchDroppedSchemaObject(DDLTransactionObject ddlTransactionObject, int schemaObjectId) {

        Checks.isSchemaObjectId(schemaObjectId);

        return ddlTransactionObject instanceof DDLTransactionDroppedSchemaObject
                ? ((DDLTransactionDroppedSchemaObject)ddlTransactionObject).getSchemaObjectId() == schemaObjectId
                : false;
    }

    static <T extends IMutableDoublyLinkedList<SchemaObject>, U extends ICompleteSchemaMapBuilder<?, ? extends IHeapCompleteSchemaMap, U>>
    IHeapEffectiveDatabaseSchema computeEffectiveDatabaseSchema(DatabaseId databaseId, DatabaseSchemaVersion version, IEffectiveDatabaseSchema currentSchema,
            DDLComputeEffectiveDatabaseSchemaScratchObject<T, U> ddlComputeEffectiveDatabaseSchemaScratchObject) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(ddlComputeEffectiveDatabaseSchemaScratchObject);
        Checks.areEqual(databaseId, currentSchema.getDatabaseId());
        Checks.areEqual(version.getVersionNumber(), currentSchema.getVersion().getVersionNumber() + 1);

        final IHeapEffectiveDatabaseSchema result;

        final IIndexList<DDLTransactionObject> ddlTransactionObjects = ddlComputeEffectiveDatabaseSchemaScratchObject.ddlTransactionObjects;

        final int numDDLTransactionObjects = IOnlyElementsView.intNumElements(ddlTransactionObjects);

        final DDLComputeEffectiveDatabaseSchemaParameter<T, U> ddlComputeEffectiveDatabaseSchemaParameter
                = ddlComputeEffectiveDatabaseSchemaScratchObject.ddlComputeEffectiveDatabaseSchemaParameter;

        final IMutableDoublyLinkedListAllocator<SchemaObject, T> schemaObjectMutableLinkedListAllocator
                = ddlComputeEffectiveDatabaseSchemaParameter.getSchemaObjectMutableLinkedListAllocator();

        final int totalNumSchemaObjects = currentSchema.getNumSchemaObjects() + numDDLTransactionObjects;

        final T schemaObjectMutableLinkedList = schemaObjectMutableLinkedListAllocator.createMutable(totalNumSchemaObjects);

        try {
            applyCurrentSchema(currentSchema, ddlTransactionObjects, schemaObjectMutableLinkedList);

            applyTransactionObjects(ddlTransactionObjects, ddlComputeEffectiveDatabaseSchemaScratchObject, schemaObjectMutableLinkedList);

            if (schemaObjectMutableLinkedList.isEmpty()) {

                result = IHeapEffectiveDatabaseSchema.empty(databaseId);
            }
            else {
                final ICompleteSchemaMapBuilderAllocator<?, ? extends IHeapCompleteSchemaMap, U> completeSchemaMapBuilderAllocator
                        = ddlComputeEffectiveDatabaseSchemaParameter.getCompleteSchemaMapBuilderAllocator();

                final U completeSchemaMapBuilder = completeSchemaMapBuilderAllocator.createBuilder();

                try {
                    schemaObjectMutableLinkedList.forEach(completeSchemaMapBuilder, (o, b) -> b.addSchemaObject(o.getDDLObjectType(), o));

                    result = IHeapEffectiveDatabaseSchema.of(databaseId, version, completeSchemaMapBuilder.buildHeapAllocatedNotEmpty());
                }
                finally {

                    completeSchemaMapBuilderAllocator.freeBuilder(completeSchemaMapBuilder);
                }
            }
        }
        finally {

            schemaObjectMutableLinkedListAllocator.freeMutable(schemaObjectMutableLinkedList);
        }

        return result;
    }

    private static void applyCurrentSchema(IEffectiveDatabaseSchema currentSchema, IIndexList<DDLTransactionObject> ddlTransactionObjects,
            IMutableDoublyLinkedList<SchemaObject> schemaObjectMutableIndexList) {

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final IIndexList<SchemaObject> schemaObjects = currentSchema.getSchemaObjectsList(ddlObjectType);

            if (schemaObjects != null) {

                final long numSchemaObjects = schemaObjects.getNumElements();

                for (int i = 0; i < numSchemaObjects; ++ i) {

                    final SchemaObject schemaObject = schemaObjects.get(i);

                    schemaObjectMutableIndexList.addTail(schemaObject.makeCopyOrImmutable());
                }
            }
        }
    }

    private static void checkRecreateAndAdd(SchemaObject schemaObject, DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject) {

        final int index = ddlComputeEffectiveDatabaseSchemaScratchObject.scratchIndex;

        if (ddlComputeEffectiveDatabaseSchemaScratchObject.ddlTransactionObjects.contains(index, ddlComputeEffectiveDatabaseSchemaScratchObject.scratchIndex - index, schemaObject,
                (o, p) ->  p.equalsName(null, schemaObject, null, false))) {

            final DDLComputeEffectiveDatabaseSchemaParameter<?, ?> ddlComputeEffectiveDatabaseSchemaParameter
                    = ddlComputeEffectiveDatabaseSchemaScratchObject.ddlComputeEffectiveDatabaseSchemaParameter;

            final int newSchemaObjectId = ddlComputeEffectiveDatabaseSchemaParameter.getSchemaObjectIdAllocator().applyAsInt(schemaObject.getDDLObjectType());
            final SchemaObject recreated = schemaObject.recreateWithNewShemaObjectId(newSchemaObjectId);

            ddlComputeEffectiveDatabaseSchemaScratchObject.schemaObjectMutableLinkedList.addTail(recreated);
        }
    }

    private static void applyTransactionObjects(IIndexList<DDLTransactionObject> ddlTransactionObjects,
            DDLComputeEffectiveDatabaseSchemaScratchObject<?, ?> ddlComputeEffectiveDatabaseSchemaScratchObject,
            IMutableDoublyLinkedList<SchemaObject> schemaObjectMutableLinkedList) {

        ddlComputeEffectiveDatabaseSchemaScratchObject.schemaObjectMutableLinkedList = schemaObjectMutableLinkedList;

        final int numDDLTransactionObjects = IOnlyElementsView.intNumElements(ddlTransactionObjects);

        for (int i = 0; i < numDDLTransactionObjects; ++ i) {

            final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

            ddlComputeEffectiveDatabaseSchemaScratchObject.scratchIndex = i;

            ddlTransactionObject.visit(processTransactionObjectVisitor, ddlComputeEffectiveDatabaseSchemaScratchObject);
        }
    }
}
