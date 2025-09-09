package dev.jdata.db.engine.transactions.ddl;

import java.util.Objects;
import java.util.function.ToIntFunction;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.EffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

class DDLTransactionEffectiveSchemaHelper {

    static final class DDLComputeEffectiveDatabaseSchemaParameter implements IResettable {

        private IndexList<DDLTransactionObject> ddlTransactionObjects;
        private ICompleteSchemaMapsBuilder<SchemaObject, ?, HeapAllCompleteSchemaMaps> completeSchemaMapsBuilder;
        private ToIntFunction<DDLObjectType> schemaObjectIdAllocator;

        private int scratchIndex;
        private int scratchNumDDLTransactionObjects;

        void initialize(IndexList<DDLTransactionObject> ddlTransactionObjects, ICompleteSchemaMapsBuilder<SchemaObject, ?, HeapAllCompleteSchemaMaps> completeSchemaMapsBuilder,
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
            this.scratchNumDDLTransactionObjects = -1;
        }
    }

    private static final DDLTransactionObjectVisitor<DDLComputeEffectiveDatabaseSchemaParameter, Void> processTransactionObjectVisitor
            = new DDLTransactionObjectVisitor<DDLComputeEffectiveDatabaseSchemaParameter, Void>() {

        @Override
        public Void onAddedColumnsSchemaObject(DDLTransactionAddedColumnsSchemaObject addedColumnsSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            final SchemaObject schemaObject = addedColumnsSchemaObject.getSchemaObject();

            return null;
        }

        @Override
        public Void onAddedNonColumnsSchemaObject(DDLTransactionAddedNonColumnsSchemaObject addedNonColumnsSchemaObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            parameter.completeSchemaMapsBuilder.addSchemaObject(addedNonColumnsSchemaObject.getSchemaObject());

            return null;
        }

        @Override
        public Void onColumnsDiffObject(DDLTransactionColumnsDiffObject columnsDiffObject, DDLComputeEffectiveDatabaseSchemaParameter parameter) {

            throw new UnsupportedOperationException();
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

    static EffectiveDatabaseSchema computeEffectiveDatabaseSchema(DatabaseId databaseId, DatabaseSchemaVersion version, IEffectiveDatabaseSchema currentSchema,
            DDLComputeEffectiveDatabaseSchemaParameter ddlComputeEffectiveDatabaseSchemaParameter) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(currentSchema);
        Objects.requireNonNull(ddlComputeEffectiveDatabaseSchemaParameter);
        Checks.areEqual(databaseId, currentSchema.getDatabaseId());
        Checks.areEqual(version.getVersionNumber(), currentSchema.getVersion().getVersionNumber() + 1);

        final IndexList<DDLTransactionObject> ddlTransactionObjects = ddlComputeEffectiveDatabaseSchemaParameter.ddlTransactionObjects;

        final int numDDLTransactionObjects = Integers.checkUnsignedLongToUnsignedInt(ddlTransactionObjects.getNumElements());

        ddlComputeEffectiveDatabaseSchemaParameter.scratchNumDDLTransactionObjects = numDDLTransactionObjects;

        for (int i = 0; i < numDDLTransactionObjects; ++ i) {

            final DDLTransactionObject ddlTransactionObject = ddlTransactionObjects.get(i);

            ddlComputeEffectiveDatabaseSchemaParameter.scratchIndex = i;

            ddlTransactionObject.visit(processTransactionObjectVisitor, ddlComputeEffectiveDatabaseSchemaParameter);
        }

        return EffectiveDatabaseSchema.of(databaseId, version, ddlComputeEffectiveDatabaseSchemaParameter.completeSchemaMapsBuilder.buildHeapAllocated());
    }
}
