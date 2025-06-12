package dev.jdata.db.schema;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.DiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListGetters;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IIntSetAllocator;
import dev.jdata.db.utils.scalars.Integers;

class EffectiveSchemaHelper {

    static final class AllEffectiveSchemaAllocators extends EffectiveSchemaAllocators<SchemaObject> {

        private final SchemaObjectIndexListAllocators indexListAllocators;

        AllEffectiveSchemaAllocators(IndexListAllocator<Column> columnIndexListAllocator, IIntSetAllocator intSetAllocator,
                CompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator, SchemaMapBuilderAllocator<SchemaObject> schemaMapBuilderAllocator,
                SchemaObjectIndexListAllocators indexListAllocators) {
            super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator);

            this.indexListAllocators = Objects.requireNonNull(indexListAllocators);
        }
    }

    static class EffectiveSchemaAllocators<T extends SchemaObject> implements IAllocators {

        private final IndexListAllocator<Column> columnIndexListAllocator;
        private final IIntSetAllocator intSetAllocator;
        private final CompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator;
        private final SchemaMapBuilderAllocator<T> schemaMapBuilderAllocator;

        EffectiveSchemaAllocators(IndexListAllocator<Column> columnIndexListAllocator, IIntSetAllocator intSetAllocator,
                CompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator, SchemaMapBuilderAllocator<T> schemaMapBuilderAllocator) {

            this.columnIndexListAllocator = Objects.requireNonNull(columnIndexListAllocator);
            this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
            this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
            this.schemaMapBuilderAllocator = Objects.requireNonNull(schemaMapBuilderAllocator);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addInstanceAllocator("columnIndexListAllocator", RefType.PASSED, Column.class, columnIndexListAllocator);

        }
    }

    static CompleteSchemaMaps buildSchemaMaps(CompleteDatabaseSchema initialSchema, IIndexListGetters<DiffDatabaseSchema> schemaDiffs,
            DroppedSchemaObjects droppedSchemaObjects, AllEffectiveSchemaAllocators allocators) {

        Objects.requireNonNull(initialSchema);
        Objects.requireNonNull(schemaDiffs);
        Objects.requireNonNull(droppedSchemaObjects);

        final CompleteSchemaMaps result;

        final EffectiveSchemaAllocators<SchemaObject> effectiveSchemaAllocators = allocators;

        final CompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator = effectiveSchemaAllocators.completeSchemaMapsBuilderAllocator;
        final CompleteSchemaMaps.Builder completeSchemaMapsBuilder = CompleteSchemaMaps.createBuilder(completeSchemaMapsBuilderAllocator);

        try {
            for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

                final IndexListAllocator<? extends SchemaObject> indexListAllocator = allocators.indexListAllocators.getIndexListAllocator(ddlObjectType);

                final SchemaMap<SchemaObject> schemaMap = buildSchemaMap(ddlObjectType, ddlObjectType.getCreateArray(), initialSchema, schemaDiffs, droppedSchemaObjects,
                        indexListAllocator, allocators);

                completeSchemaMapsBuilder.setSchemaMap(ddlObjectType, schemaMap);
            }

            result = completeSchemaMapsBuilder.build();
        }
        finally {

            completeSchemaMapsBuilderAllocator.freeCompleteSchemaMapsBuilder(completeSchemaMapsBuilder);
        }

        return result;
    }

    private static <T extends SchemaObject> SchemaMap<T> buildSchemaMap(DDLObjectType ddlObjectType, IntFunction<T[]> createValuesArray, CompleteDatabaseSchema initialSchema,
            IIndexListGetters<DiffDatabaseSchema> schemaDiffs, DroppedSchemaObjects droppedSchemaObjects, IndexListAllocator<? extends SchemaObject> untypedIndexListAllocator,
            EffectiveSchemaAllocators<T> allocators) {

        final SchemaMap<T> result;

        @SuppressWarnings("unchecked")
        final IndexListAllocator<T> indexListAllocator = (IndexListAllocator<T>)untypedIndexListAllocator;

        final int initialCapacity = 1 + Integers.checkUnsignedLongToUnsignedInt(schemaDiffs.getNumElements());

        final SchemaMapBuilderAllocator<T> schemaMapBuilderAllocator = allocators.schemaMapBuilderAllocator;

        final SchemaMap.Builder<T> schemaMapBuilder = SchemaMap.createBuilder(initialCapacity, schemaMapBuilderAllocator);
        final IndexList.Builder<T> schemaObjectsBuilder = IndexList.createBuilder(indexListAllocator);

        final IIntSetAllocator intSetAllocator = allocators.intSetAllocator;

        final MutableIntBucketSet addedSchemaObjects = intSetAllocator.allocateIntSet(CapacityExponents.computeCapacityExponent(initialCapacity));

        try {
            final IIndexList<T> schemaObjects = initialSchema.getSchemaObjects(ddlObjectType);

            final long numCompleteSchemas = schemaObjects.getNumElements();

            for (long completeSchemaIndex = 0L; completeSchemaIndex < numCompleteSchemas; ++ completeSchemaIndex) {

                final T schemaObject = schemaObjects.get(completeSchemaIndex);

                addEffectiveSchemaObject(schemaObjectsBuilder, ddlObjectType, schemaObject, droppedSchemaObjects, allocators.columnIndexListAllocator, null);
            }

            final long numSchemaDiffs = schemaDiffs.getNumElements();

            for (long schemaDiffIndex = 0L; schemaDiffIndex < numSchemaDiffs; ++ schemaDiffIndex) {

                final DiffDatabaseSchema diffDatabaseSchema = schemaDiffs.get(schemaDiffIndex);

                final IIndexList<T> diffSchemaObjects = diffDatabaseSchema.getSchemaObjects(ddlObjectType);

                final long numDiffSchemaObjects = diffSchemaObjects.getNumElements();

                for (long i = 0L; i < numDiffSchemaObjects; ++ i) {

                    final T diffSchemaObject = diffSchemaObjects.get(i);

                    final int diffSchemaObjectId = diffSchemaObject.getId();

                    if (!addedSchemaObjects.contains(diffSchemaObjectId)) {

                        addEffectiveSchemaObject(schemaObjectsBuilder, ddlObjectType, diffSchemaObject, droppedSchemaObjects, allocators.columnIndexListAllocator,
                                addedSchemaObjects);
                    }
                }
            }

            final IIndexList<T> effectiveSchemaObjects = schemaObjectsBuilder.build();

            schemaMapBuilder.add(effectiveSchemaObjects);

            result = schemaMapBuilder.build();
        }
        finally {

            intSetAllocator.freeIntSet(addedSchemaObjects);

            indexListAllocator.freeIndexListBuilder(schemaObjectsBuilder);

            schemaMapBuilderAllocator.freeSchemaMapBuilder(schemaMapBuilder);
        }

        return result;
    }

    private static <T extends SchemaObject> void addEffectiveSchemaObject(IndexList.Builder<T> schemaObjectsBuilder, DDLObjectType ddlObjectType, T schemaObject,
            DroppedSchemaObjects droppedSchemaObjects, IndexListAllocator<Column> columnIndexListAllocator, MutableIntBucketSet addedSchemaObjects) {

        final T effectiveSchemaObject = createEffectiveSchemaObject(ddlObjectType, schemaObject, droppedSchemaObjects, columnIndexListAllocator);

        if (effectiveSchemaObject != null) {

            if (effectiveSchemaObject == schemaObject) {

                throw new UnsupportedOperationException();
            }
            else {
                addedSchemaObjects.add(effectiveSchemaObject.getId());

                schemaObjectsBuilder.addTail(effectiveSchemaObject);
            }
        }
    }

    private static <T extends SchemaObject> T createEffectiveSchemaObject(DDLObjectType ddlObjectType, T schemaObject, DroppedSchemaObjects droppedSchemaObjects,
            IndexListAllocator<Column> columnIndexListAllocator) {

        final T result;

        if (ddlObjectType.hasColumns()) {

            final ColumnsObject columnsObject = (ColumnsObject)schemaObject;

            final int numColumns = columnsObject.getNumColumns();

            final IndexList.Builder<Column> columnsBuilder = IndexList.createBuilder(numColumns, columnIndexListAllocator);

            try {
                for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                    final Column column = columnsObject.getColumn(columnIndex);

                    if (!droppedSchemaObjects.isDroppedColumn(ddlObjectType, columnsObject, column)) {

                        columnsBuilder.addTail(column);
                    }
                }
            }
            finally {
                final IIndexList<Column> columns = columnsBuilder.build();

                if (columns.isEmpty()) {

                    columnIndexListAllocator.freeIndexList(columns);

                    result = null;
                }
                else {
                    if (columns.getNumElements() == numColumns) {

                        result = schemaObject;
                    }
                    else {
                        @SuppressWarnings("unchecked")
                        final T schemaObjectCopy = (T)columnsObject.makeCopy(columns);

                        result = schemaObjectCopy;
                    }
                }
            }
        }
        else {
            result = schemaObject;
        }

        return result;
    }
}
