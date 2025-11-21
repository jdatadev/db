package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

class DDLTransactionEffectiveSchemaHelper {

    static final class DDLComputeEffectiveDatabaseSchemaParameter extends ObjectCacheNode implements IResettable {

        private IIndexList<DDLTransactionObject> ddlTransactionObjects;
        private IHeapAllCompleteSchemaMapsBuilder completeSchemaMapsBuilder;
        private ToIntFunction<DDLObjectType> schemaObjectIdAllocator;

        private int scratchIndex;

        DDLComputeEffectiveDatabaseSchemaParameter(AllocationType allocationType) {
            super(allocationType);
        }

        void initialize(IIndexList<DDLTransactionObject> ddlTransactionObjects, IHeapAllCompleteSchemaMapsBuilder completeSchemaMapsBuilder,
                ToIntFunction<DDLObjectType> schemaObjectIdAllocator) {

            this.ddlTransactionObjects = Initializable.checkNotYetInitialized(this.ddlTransactionObjects, ddlTransactionObjects);
            this.completeSchemaMapsBuilder = Initializable.checkNotYetInitialized(this.completeSchemaMapsBuilder, completeSchemaMapsBuilder);
            this.schemaObjectIdAllocator = Initializable.checkNotYetInitialized(this.schemaObjectIdAllocator, schemaObjectIdAllocator);

            resetWorkerParameters();
        }

        @Override
        public void reset() {

            this.ddlTransactionObjects = Initializable.checkResettable(ddlTransactionObjects);
            this.completeSchemaMapsBuilder = Initializable.checkResettable(completeSchemaMapsBuilder);
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

            addSchemaObject(parameter, addedColumnsSchemaObject);

            return null;
        }

        @Override
        public Void onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            addSchemaObject(parameter, addedNonColumnsSchemaObject);

            return null;
        }

        @Override
        public Void onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            throw new UnsupportedOperationException();
        }

        @Override
        public Void onDroppedSchemaObject(DDLTransactionDroppedSchemaObject droppedSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            return null;
        }

        private void addSchemaObject(DDLComputeEffectiveDatabaseSchemaParameter parameter, DDLTransactionAddedSchemaObject<?> addedSchemaObject) {

            parameter.completeSchemaMapsBuilder.addSchemaObject(addedSchemaObject.getSchemaObject());
        }
    };

    private static void checkRecreateAndAdd(SchemaObject schemaObject, DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter) {

        final int index = ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex;

        if (ddlComputeEffectiveDatabaseSchemaParameter.ddlTransactionObjects.contains(index, ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex - index, schemaObject,
                (o, p) ->  p.equalsName(null, schemaObject, null, false))) {

            final int newSchemaObjectId = ddlComputeEffectiveDatabaseSchemaParameter.schemaObjectIdAllocator.applyAsInt(schemaObject.getDDLObjectType());
            final SchemaObject recreated = schemaObject.recreateWithNewShemaObjectId(newSchemaObjectId);

            ddlComputeEffectiveDatabaseSchemaParameter.completeSchemaMapsBuilder.addSchemaObject(recreated);
        }
    }

    static IEffectiveDatabaseSchema computeEffectiveDatabaseSchema(DatabaseId databaseId, DatabaseSchemaVersion version, IEffectiveDatabaseSchema currentSchema,
            DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(ddlComputeEffectiveDatabaseSchemaParameter);
        Checks.areEqual(databaseId, currentSchema.getDatabaseId());
        Checks.areEqual(version.getVersionNumber(), currentSchema.getVersion().getVersionNumber() + 1);

        final IIndexList<DDLTransactionObject> ddlTransactionObjects = ddlComputeEffectiveDatabaseSchemaParameter.ddlTransactionObjects;

        final IHeapAllCompleteSchemaMapsBuilder completeSchemaMapsBuilder = ddlComputeEffectiveDatabaseSchemaParameter.completeSchemaMapsBuilder;

        final int numDDLTransactionObjects = IOnlyElementsView.intNumElements(ddlTransactionObjects);

        for (int i = 0; i < numDDLTransactionObjects; ++ i) {

            final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

            ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex = i;

            ddlTransactionObject.visit(processTransactionObjectVisitor, ddlComputeEffectiveDatabaseSchemaParameter);
        }

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final IIndexList<SchemaObject> schemaObjects = currentSchema.getSchemaObjects(ddlObjectType);

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

                        completeSchemaMapsBuilder.addSchemaObject(schemaObject.makeCopyOrImmutable());
                    }
                }
            }
        }

        return completeSchemaMapsBuilder.isEmpty()
                ? IHeapEffectiveDatabaseSchema.empty(databaseId)
                : IHeapEffectiveDatabaseSchema.of(databaseId, version, completeSchemaMapsBuilder.buildHeapAllocatedOrEmpty());
    }
}
