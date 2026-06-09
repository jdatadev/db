package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilder;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

class DDLTransactionEffectiveSchemaHelper {

    private static final boolean ASSERT = AssertionContants.ASSERT_DDL_TRANSACTION_EFFECTIVE_SCHEMA_HELPER;

    static final class DDLComputeEffectiveDatabaseSchemaParameter extends ObjectCacheNode implements IResettable {

        private IIndexList<DDLTransactionObject> ddlTransactionObjects;
        private IHeapCompleteSchemaMapBuilder completeSchemaMapBuilder;
        private ToIntFunction<DDLObjectType> schemaObjectIdAllocator;

        private int scratchIndex;
        private int scratchSchemaObjectId;

        DDLComputeEffectiveDatabaseSchemaParameter(AllocationType allocationType) {
            super(allocationType);
        }

        void initialize(IIndexList<DDLTransactionObject> ddlTransactionObjects, IHeapCompleteSchemaMapBuilder completeSchemaMapBuilder,
                ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

            this.ddlTransactionObjects = Initializable.checkNotYetInitialized(this.ddlTransactionObjects, ddlTransactionObjects);
            this.completeSchemaMapBuilder = Initializable.checkNotYetInitialized(this.completeSchemaMapBuilder, completeSchemaMapBuilder);
            this.schemaObjectIdAllocator = Initializable.checkNotYetInitialized(this.schemaObjectIdAllocator, schemaObjectIdAllocator);

            resetWorkerParameters();
        }

        @Override
        public void reset() {

            this.ddlTransactionObjects = Initializable.checkResettable(ddlTransactionObjects);
            this.completeSchemaMapBuilder = Initializable.checkResettable(completeSchemaMapBuilder);
            this.schemaObjectIdAllocator = Initializable.checkResettable(schemaObjectIdAllocator);

            resetWorkerParameters();
        }

        private void resetWorkerParameters() {

            this.scratchIndex = -1;
        }
    }

    private static final DDLTransactionObjectVisitor<DDLComputeEffectiveDatabaseSchemaParameter, Void> processTransactionObjectVisitor
            = new DDLTransactionObjectVisitor<DDLComputeEffectiveDatabaseSchemaParameter, Void>() {

        @Override
        public Void onAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject addedColumnsSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            addSchemaObjectIfNotDroppedLaterInSameTransaction(addedColumnsSchemaObject, parameter);

            return null;
        }

        @Override
        public Void onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            addSchemaObjectIfNotDroppedLaterInSameTransaction(addedNonColumnsSchemaObject, parameter);

            return null;
        }

        @Override
        public Void onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            applyColumnsDiff(columnsDiffObject);

            return null;
        }

        private void applyColumnsDiff(DDLTransactionColumnsDiffObject columnsDiffObject) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Void onDroppedSchemaObject(DDLTransactionDroppedSchemaObject droppedSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            return null;
        }

        private void addSchemaObjectIfNotDroppedLaterInSameTransaction(DDLTransactionAddedSchemaObject<?> ddlTransactionAddedSchemaObject,
                DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            final int transactionObjectIndex = parameter.scratchIndex;
            final IIndexList<DDLTransactionObject> ddlTransactionObjects = parameter.ddlTransactionObjects;

            final int numDDLTransationObjects = IOnlyElementsView.intNumElements(ddlTransactionObjects);

            if (ASSERT) {

                Assertions.isLessThan(transactionObjectIndex, numDDLTransationObjects);
                Assertions.areSameInstances(ddlTransactionAddedSchemaObject, ddlTransactionObjects.get(transactionObjectIndex));
            }

            final int nextIndex = transactionObjectIndex;
            final int numRemaining = numDDLTransationObjects - nextIndex;

            final SchemaObject addedSchemaObject = ddlTransactionAddedSchemaObject.getSchemaObject();

            parameter.scratchSchemaObjectId = ddlTransactionAddedSchemaObject.getSchemaObject().getId();

            if (numRemaining == 0 || !ddlTransactionObjects.contains(nextIndex, numRemaining, parameter, DDLTransactionEffectiveSchemaHelper::matchDroppedSchemaObject)) {

                parameter.completeSchemaMapBuilder.addSchemaObject(addedSchemaObject);
            }
        }
    };

    private static boolean matchDroppedSchemaObject(DDLTransactionObject ddlTransactionObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

        return matchDroppedSchemaObject(ddlTransactionObject, parameter.scratchSchemaObjectId);
    }

    static boolean matchDroppedSchemaObject(DDLTransactionObject ddlTransactionObject, int schemaObjectId) {

        Checks.isSchemaObjectId(schemaObjectId);

        return ddlTransactionObject instanceof DDLTransactionDroppedSchemaObject
                ? ((DDLTransactionDroppedSchemaObject)ddlTransactionObject).getSchemaObjectId() == schemaObjectId
                : false;
    }

    private static void checkRecreateAndAdd(SchemaObject schemaObject, DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter) {

        final int index = ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex;

        if (ddlComputeEffectiveDatabaseSchemaParameter.ddlTransactionObjects.contains(index, ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex - index, schemaObject,
                (o, p) ->  p.equalsName(null, schemaObject, null, false))) {

            final int newSchemaObjectId = ddlComputeEffectiveDatabaseSchemaParameter.schemaObjectIdAllocator.applyAsInt(schemaObject.getDDLObjectType());
            final SchemaObject recreated = schemaObject.recreateWithNewShemaObjectId(newSchemaObjectId);

            ddlComputeEffectiveDatabaseSchemaParameter.completeSchemaMapBuilder.addSchemaObject(recreated);
        }
    }

    static IHeapEffectiveDatabaseSchema computeEffectiveDatabaseSchema(DatabaseId databaseId, DatabaseSchemaVersion version, IEffectiveDatabaseSchema currentSchema,
            DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(ddlComputeEffectiveDatabaseSchemaParameter);
        Checks.areEqual(databaseId, currentSchema.getDatabaseId());
        Checks.areEqual(version.getVersionNumber(), currentSchema.getVersion().getVersionNumber() + 1);

        final IIndexList<DDLTransactionObject> ddlTransactionObjects = ddlComputeEffectiveDatabaseSchemaParameter.ddlTransactionObjects;

        final IHeapCompleteSchemaMapBuilder completeSchemaMapBuilder = ddlComputeEffectiveDatabaseSchemaParameter.completeSchemaMapBuilder;

        final int numDDLTransactionObjects = IOnlyElementsView.intNumElements(ddlTransactionObjects);

        for (int i = 0; i < numDDLTransactionObjects; ++ i) {

            final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

            ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex = i;

            ddlTransactionObject.visit(processTransactionObjectVisitor, ddlComputeEffectiveDatabaseSchemaParameter);
        }

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final IIndexList<SchemaObject> schemaObjects = currentSchema.getSchemaObjectsList(ddlObjectType);

            if (schemaObjects != null) {

                final long numSchemaObjects = schemaObjects.getNumElements();

                for (int i = 0; i < numSchemaObjects; ++ i) {

                    final SchemaObject schemaObject = schemaObjects.get(i);

                    final boolean shouldBeAddedFromCurrentSchema = ddlTransactionObjects.contains(schemaObject, (e, o) -> {

                        final boolean keepFromCurrentSchema;

                        if (e instanceof DDLTransactionDroppedSchemaObject) {

                            final DDLTransactionDroppedSchemaObject ddlTransactionDroppedSchemaObject = (DDLTransactionDroppedSchemaObject)e;

                            keepFromCurrentSchema = ddlTransactionDroppedSchemaObject.getSchemaObjectId() != o.getId();
                        }
                        else {
                            keepFromCurrentSchema = true;
                        }

                        return keepFromCurrentSchema;
                    });

                    if (shouldBeAddedFromCurrentSchema) {

                        completeSchemaMapBuilder.addSchemaObject(schemaObject.makeCopyOrImmutable());
                    }
                }
            }
        }

        return completeSchemaMapBuilder.isEmpty()
                ? IHeapEffectiveDatabaseSchema.empty(databaseId)
                : IHeapEffectiveDatabaseSchema.of(databaseId, version, completeSchemaMapBuilder.buildHeapAllocatedOrEmpty());
    }
}
