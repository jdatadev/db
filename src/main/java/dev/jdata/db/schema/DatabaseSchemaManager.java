package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.allocators.databases.heap.HeapDatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.effective.HeapAllEffectiveSchemaAllocators;
import dev.jdata.db.schema.model.databaseschema.ICompleteDatabaseSchema;
import dev.jdata.db.schema.model.databaseschema.IHeapGenericCompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.IDiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.IInitialDiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.ISchemaDroppedElements;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IHeapEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapMutableIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseSchemaManager extends BaseDatabaseSchemas implements ISchemaObjectIdAllocator, IFreeing<HeapDatabasesSchemaManagerAllocator> {

    private final IHeapGenericCompleteDatabaseSchema initialCompleteSchema;
    private final IHeapMutableIndexList<IDiffDatabaseSchema> diffSchemas;

    private final HeapDatabasesSchemaManagerAllocator databasesSchemaManagerAllocator;

    private final int[] objectIdAllocators;
//    private final SchemaDroppedElements<IMutableIntSet, IMutableIntToObjectWithRemoveStaticMap<T>> droppedElementsSchemaObjects;

    private int schemaVersionNumber;

    public static DatabaseSchemaManager of(DatabaseId databaseId, IHeapGenericCompleteDatabaseSchema completeDatabaseSchema,
            HeapDatabasesSchemaManagerAllocator databasesSchemaManagerAllocator) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(completeDatabaseSchema);
        Objects.requireNonNull(databasesSchemaManagerAllocator);

        return new DatabaseSchemaManager(AllocationType.HEAP, databaseId, completeDatabaseSchema, IHeapIndexList.empty(), databasesSchemaManagerAllocator);
    }

    private DatabaseSchemaManager(AllocationType allocationType, DatabaseId databaseId, IHeapGenericCompleteDatabaseSchema initialCompleteSchema,
            IIndexList<IInitialDiffDatabaseSchema> initialDiffSchemas, HeapDatabasesSchemaManagerAllocator databasesSchemaManagerAllocator) {
        super(allocationType, databaseId);

        Objects.requireNonNull(initialCompleteSchema);
        Checks.areEqual(databaseId, initialCompleteSchema.getDatabaseId());
        Checks.areElements(initialDiffSchemas, databaseId, (e, n) -> e.getDatabaseId().equals(n));
        Objects.requireNonNull(databasesSchemaManagerAllocator);

        final DatabaseSchemaVersion completeSchemaVersion = initialCompleteSchema.getVersion();

        if (!completeSchemaVersion.isInitialVersion()) {

            throw new IllegalArgumentException();
        }

        if (!initialDiffSchemas.isEmpty() && initialDiffSchemas.get(0).getVersion().getVersionNumber() != completeSchemaVersion.getVersionNumber() + 1) {

            throw new IllegalArgumentException();
        }

        checkVersionNumber(initialDiffSchemas);

        this.initialCompleteSchema = initialCompleteSchema;
        this.diffSchemas = IHeapMutableIndexList.copyOf(IDiffDatabaseSchema[]::new, initialDiffSchemas, s -> (IDiffDatabaseSchema)s);
        this.databasesSchemaManagerAllocator = databasesSchemaManagerAllocator;

        this.objectIdAllocators = computeObjectIdAllocators(initialCompleteSchema, initialDiffSchemas);

        this.schemaVersionNumber = initialDiffSchemas.isEmpty() ? DatabaseSchemaVersion.NO_VERSION : initialDiffSchemas.getTail().getVersion().getVersionNumber();

//        this.droppedElementsSchemaObjects = databasesSchemaManagerAllocator.allocateDroppedElementsSchemaObjects();

        initialDiffSchemas.forEach(this, (s, i) -> {

            i.addDiffSchemaDroppedObjects(s);
        });
    }

    private static int[] computeObjectIdAllocators(ICompleteDatabaseSchema initialCompleteSchema, IIndexList<IInitialDiffDatabaseSchema> initialDiffSchemas) {

        final DDLObjectType[] ddlObjectTypes = DDLObjectType.values();
        final int numDDLObjectTypes = ddlObjectTypes.length;

        final int[] objectIdAllocators = new int[numDDLObjectTypes];

        final int defaultValue = -1;

        for (int i = 0; i < numDDLObjectTypes; ++ i) {

            final DDLObjectType ddlObjectType = ddlObjectTypes[i];

            final int completeMaxId = initialCompleteSchema.computeMaxId(ddlObjectType, defaultValue);
            final int diffMaxId = computeMaxObjectId(ddlObjectType, defaultValue, initialDiffSchemas);

            objectIdAllocators[i] = Math.max(completeMaxId, diffMaxId);
        }

        return objectIdAllocators;
    }

    @Override
    public synchronized void free(HeapDatabasesSchemaManagerAllocator instance) {

        Objects.requireNonNull(instance);

        Checks.areSame(instance, databasesSchemaManagerAllocator);

        checkIsAllocatedRenamed();

//        instance.freeDroppedElementsSchemaObjects(droppedElementsSchemaObjects);
    }

    private synchronized void addDiffSchema(IInitialDiffDatabaseSchema initialDiffSchema) {

        Objects.requireNonNull(initialDiffSchema);

        checkIsAllocatedRenamed();

        diffSchemas.addTail(initialDiffSchema);

        addDiffSchemaDroppedObjects(initialDiffSchema);
    }

    private void addDiffSchemaDroppedObjects(IDiffDatabaseSchema diffDatabaseSchema) {

        final ISchemaDroppedElements dropped = diffDatabaseSchema.getDroppedSchemaObjects();

//        droppedElementsSchemaObjects.add(dropped, databasesSchemaManagerAllocator.getDroppedSchemaObjectsAllocator());
    }

    @Override
    public synchronized int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        checkIsAllocatedRenamed();

        return ++ objectIdAllocators[ddlObjectType.ordinal()];
    }

    @Override
    public synchronized void rollbackSchemaObjectIdAllocation(DDLObjectType ddlObjectType, int schemaObjectId) {

        Objects.requireNonNull(ddlObjectType);
        Checks.isSchemaObjectId(schemaObjectId);

        checkIsAllocatedRenamed();

        throw new UnsupportedOperationException();
    }

    public synchronized IEffectiveDatabaseSchema buildEffectiveDatabaseSchema(HeapAllEffectiveSchemaAllocators allocators) {

        Objects.requireNonNull(allocators);

        final IHeapAllCompleteSchemaMaps schemaMaps = null; // EffectiveSchemaHelper.buildSchemaMaps(initialCompleteSchema, diffSchemas, droppedSchemaObjects, null);

        final DatabaseSchemaVersion latestVersion = diffSchemas.isEmpty() ? initialCompleteSchema.getVersion() : diffSchemas.getTail().getVersion();

        return IHeapEffectiveDatabaseSchema.of(getDatabaseId(), latestVersion, schemaMaps);
    }

    private synchronized IDiffDatabaseSchema getCurrentSchema() {

        return diffSchemas.getTail();
    }

    private synchronized DatabaseSchemaVersion allocateSchemaVersion() {

        return DatabaseSchemaVersion.of(++ schemaVersionNumber);
    }

    private static void checkVersionNumber(IIndexList<? extends IDiffDatabaseSchema> schemas) {

        final long numSchemas = schemas.getNumElements();

        int previousVersionNumber = -1;

        for (int i = 0; i < numSchemas; ++ i) {

            final int versionNumber = schemas.get(i).getVersion().getVersionNumber();

            if (i > 0 && versionNumber != previousVersionNumber + 1) {

                throw new IllegalArgumentException();
            }

            previousVersionNumber = versionNumber;
        }
    }

    private static int computeMaxObjectId(DDLObjectType ddlObjectType, int defaultValue, IIndexList<? extends IDiffDatabaseSchema> schemas) {

        int maxOjectId = defaultValue;

        final long numSchemas = schemas.getNumElements();

        for (int i = 0; i < numSchemas; ++ i) {

            final IDiffDatabaseSchema databaseSchema = schemas.get(i);

            final int schemaMaxTableId = databaseSchema.computeMaxId(ddlObjectType, defaultValue);

            if (schemaMaxTableId != defaultValue && schemaMaxTableId > maxOjectId) {

                maxOjectId = schemaMaxTableId;
            }
        }

        return maxOjectId;
    }
}
