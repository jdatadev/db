package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.EffectiveSchemaHelper.AllEffectiveSchemaAllocators;
import dev.jdata.db.schema.model.databaseschema.BaseDatabaseSchema;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.DiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.diff.dropped.IDroppedSchemaObjects;
import dev.jdata.db.schema.model.effective.EffectiveDatabaseSchema;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IMutableIndexList;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseSchemaManager extends BaseDatabaseSchemas implements ISchemaObjectIdAllocator, IFreeing<DatabasesSchemaManagerAllocator> {

    private final CompleteDatabaseSchema initialCompleteSchema;
    private final IMutableIndexList<DiffDatabaseSchema> diffSchemas;

    private final DatabasesSchemaManagerAllocator databasesSchemaManagerAllocator;

    private final int[] objectIdAllocators;
    private final DroppedSchemaObjects droppedSchemaObjects;

    private int schemaVersionNumber;

    public static DatabaseSchemaManager of(DatabaseId databaseId, CompleteDatabaseSchema completeDatabaseSchema,
            DatabasesSchemaManagerAllocator databasesSchemaManagerAllocator) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(completeDatabaseSchema);
        Objects.requireNonNull(databasesSchemaManagerAllocator);

        return new DatabaseSchemaManager(AllocationType.HEAP, databaseId, completeDatabaseSchema, IIndexList.empty(), databasesSchemaManagerAllocator);
    }

    DatabaseSchemaManager(AllocationType allocationType, DatabaseId databaseId, CompleteDatabaseSchema initialCompleteSchema, IIndexList<DiffDatabaseSchema> diffSchemas,
            DatabasesSchemaManagerAllocator databasesSchemaManagerAllocator) {
        super(allocationType, databaseId);

        Objects.requireNonNull(initialCompleteSchema);
        Checks.areNotEqual(databaseId, initialCompleteSchema.getDatabaseId());
        Checks.areElements(diffSchemas, databaseId, (e, n) -> e.getDatabaseId().equals(n));
        Objects.requireNonNull(databasesSchemaManagerAllocator);

        final DatabaseSchemaVersion completeSchemaVersion = initialCompleteSchema.getVersion();

        if (!completeSchemaVersion.isInitialVersion()) {

            throw new IllegalArgumentException();
        }

        if (diffSchemas.get(0).getVersion().getVersionNumber() != completeSchemaVersion.getVersionNumber() + 1) {

            throw new IllegalArgumentException();
        }

        checkVersionNumber(diffSchemas);

        this.initialCompleteSchema = initialCompleteSchema;
        this.diffSchemas = diffSchemas.copyToMutable();
        this.databasesSchemaManagerAllocator = databasesSchemaManagerAllocator;

        this.objectIdAllocators = computeObjectIdAllocators(initialCompleteSchema, diffSchemas);

        this.schemaVersionNumber = diffSchemas.getTail().getVersion().getVersionNumber();

        this.droppedSchemaObjects = databasesSchemaManagerAllocator.allocateDroppedSchemaObjects();

        diffSchemas.forEach(this, (s, i) -> {

            i.addDiffSchemaDroppedObjects(s);
        });
    }

    private static int[] computeObjectIdAllocators(CompleteDatabaseSchema initialCompleteSchema, IIndexList<DiffDatabaseSchema> diffSchemas) {

        final DDLObjectType[] ddlObjectTypes = DDLObjectType.values();
        final int numDDLObjectTypes = ddlObjectTypes.length;

        final int[] objectIdAllocators = new int[numDDLObjectTypes];

        final int defaultValue = -1;

        for (int i = 0; i < numDDLObjectTypes; ++ i) {

            final DDLObjectType objectType = ddlObjectTypes[i];

            final int completeMaxId = initialCompleteSchema.computeMaxId(objectType, defaultValue);
            final int diffMaxId = computeMaxObjectId(objectType, defaultValue, diffSchemas);

            objectIdAllocators[i] = Math.max(completeMaxId, diffMaxId);
        }

        return objectIdAllocators;
    }

    @Override
    public synchronized void free(DatabasesSchemaManagerAllocator instance) {

        Objects.requireNonNull(instance);

        if (instance != databasesSchemaManagerAllocator) {

            throw new IllegalArgumentException();
        }

        checkIsAllocated();

        instance.freeDroppedSchemaObjects(droppedSchemaObjects);
    }

    public synchronized void addDiffSchema(DiffDatabaseSchema diffDatabaseSchema) {

        Objects.requireNonNull(diffDatabaseSchema);

        checkIsAllocated();

        diffSchemas.addTail(diffDatabaseSchema);

        addDiffSchemaDroppedObjects(diffDatabaseSchema);
    }

    private void addDiffSchemaDroppedObjects(DiffDatabaseSchema diffDatabaseSchema) {

        final IDroppedSchemaObjects dropped = diffDatabaseSchema.getDroppedSchemaObjects();

        droppedSchemaObjects.add(dropped, databasesSchemaManagerAllocator.getDroppedSchemaObjectsAllocator());
    }
/*
    private void dropSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObject);

        if (!ddlObjectType.getSchemaObjectType().equals(schemaObject.getClass())) {

            throw new IllegalArgumentException();
        }

        droppedSchemaObjects.addDroppedSchemaObject(ddlObjectType, schemaObject, databasesSchemaManagerAllocator.getDroppedSchemaObjectsAllocator());
    }

    private void dropColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(columnsObject);

        if (!ddlObjectType.hasColumns()) {

            throw new IllegalArgumentException();
        }

        if (!ddlObjectType.getSchemaObjectType().equals(columnsObject.getClass())) {

            throw new IllegalArgumentException();
        }

        final IDroppedElementsAllocator droppedElementsAllocator = ;
        final IIntSetAllocator intSetAllocator;

        Objects.requireNonNull(column);

        droppedSchemaObjects.addDroppedColumn(ddlObjectType, columnsObject, column);
    }
*/
    @Override
    public synchronized int allocateSchemaObjectId(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        checkIsAllocated();

        return ++ objectIdAllocators[ddlObjectType.ordinal()];
    }

    @Override
    public synchronized void rollbackSchemaObjectIdAllocation(DDLObjectType ddlObjectType, int schemaObjectId) {

        Objects.requireNonNull(ddlObjectType);
        Checks.isSchemaObjectId(schemaObjectId);

        checkIsAllocated();

        throw new UnsupportedOperationException();
    }

    public synchronized IEffectiveDatabaseSchema buildEffectiveDatabaseSchema(AllEffectiveSchemaAllocators allocators) {

        Objects.requireNonNull(allocators);

        final CompleteSchemaMaps schemaMaps = EffectiveSchemaHelper.buildSchemaMaps(initialCompleteSchema, diffSchemas, droppedSchemaObjects, allocators);

        final DatabaseSchemaVersion latestVersion = diffSchemas.isEmpty()
                ? initialCompleteSchema.getVersion()
                : diffSchemas.getTail().getVersion();

        return EffectiveDatabaseSchema.of(getDatabaseId(), latestVersion, schemaMaps);
    }

    private synchronized DiffDatabaseSchema getCurrentSchema() {

        return diffSchemas.getTail();
    }

    private synchronized DatabaseSchemaVersion allocateSchemaVersion() {

        return DatabaseSchemaVersion.of(++ schemaVersionNumber);
    }

    private static void checkVersionNumber(IIndexList<? extends BaseDatabaseSchema<?>> schemas) {

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

    private static int computeMaxObjectId(DDLObjectType objectType, int defaultValue, IIndexList<? extends BaseDatabaseSchema<?>> schemas) {

        int maxOjectId = defaultValue;

        final long numSchemas = schemas.getNumElements();

        for (int i = 0; i < numSchemas; ++ i) {

            final BaseDatabaseSchema<?> databaseSchema = schemas.get(i);

            final int schemaMaxTableId = databaseSchema.computeMaxId(objectType, defaultValue);

            if (schemaMaxTableId != defaultValue && schemaMaxTableId > maxOjectId) {

                maxOjectId = schemaMaxTableId;
            }
        }

        return maxOjectId;
    }
}
