package dev.jdata.db.schema.effective;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.effective.BaseAllEffectiveSchemaAllocators;
import dev.jdata.db.schema.allocators.effective.EffectiveSchemaAllocators;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.DiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListGetters;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.scalars.Integers;

public class EffectiveSchemaHelper {

    public static <
                    SCHEMA_OBJECT extends SchemaObject,
                    INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                    INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                    INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                    SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                    SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                    COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<SCHEMA_MAP>,
                    COMPLETE_SCHEMA_MAPS_BUILDER extends CompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

            COMPLETE_SCHEMA_MAPS buildSchemaMaps(CompleteDatabaseSchema initialSchema, IIndexListGetters<DiffDatabaseSchema> schemaDiffs,
                    DroppedSchemaObjects droppedSchemaObjects,
                    BaseAllEffectiveSchemaAllocators<
                            SCHEMA_OBJECT,
                            INDEX_LIST,
                            INDEX_LIST_BUILDER,
                            INDEX_LIST_ALLOCATOR,
                            SCHEMA_MAP,
                            SCHEMA_MAP_BUILDER,
                            COMPLETE_SCHEMA_MAPS,
                            COMPLETE_SCHEMA_MAPS_BUILDER> allocators) {

        Objects.requireNonNull(initialSchema);
        Objects.requireNonNull(schemaDiffs);
        Objects.requireNonNull(droppedSchemaObjects);

        final COMPLETE_SCHEMA_MAPS result;

        final EffectiveSchemaAllocators<
                SCHEMA_OBJECT,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER,
                COMPLETE_SCHEMA_MAPS,
                COMPLETE_SCHEMA_MAPS_BUILDER> effectiveSchemaAllocators = allocators;

        final ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator
                = effectiveSchemaAllocators.getCompleteSchemaMapsBuilderAllocator();

        final COMPLETE_SCHEMA_MAPS_BUILDER completeSchemaMapsBuilder = CompleteSchemaMaps.createBuilder(completeSchemaMapsBuilderAllocator);

        try {
            for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

                final INDEX_LIST_ALLOCATOR indexListAllocator = allocators.getIndexListAllocators().getIndexListAllocator(ddlObjectType);

                final SCHEMA_MAP schemaMap = buildSchemaMap(ddlObjectType, ddlObjectType.getCreateArray(), initialSchema, schemaDiffs, droppedSchemaObjects,
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

    private static <
                    SCHEMA_OBJECT extends SchemaObject,
                    INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                    INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                    INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                    SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                    SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                    COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<SCHEMA_MAP>,
                    COMPLETE_SCHEMA_MAPS_BUILDER extends CompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

    SCHEMA_MAP buildSchemaMap(DDLObjectType ddlObjectType, IntFunction<SCHEMA_OBJECT[]> createValuesArray, CompleteDatabaseSchema initialSchema,
            IIndexListGetters<DiffDatabaseSchema> schemaDiffs, DroppedSchemaObjects droppedSchemaObjects, INDEX_LIST_ALLOCATOR indexListAllocator,
            EffectiveSchemaAllocators<
                    SCHEMA_OBJECT,
                    INDEX_LIST,
                    INDEX_LIST_BUILDER,
                    INDEX_LIST_ALLOCATOR,
                    SCHEMA_MAP,
                    SCHEMA_MAP_BUILDER,
                    COMPLETE_SCHEMA_MAPS,
                    COMPLETE_SCHEMA_MAPS_BUILDER> allocators) {

        final SCHEMA_MAP result;

        final int initialCapacity = 1 + Integers.checkUnsignedLongToUnsignedInt(schemaDiffs.getNumElements());

        final SchemaMapBuilderAllocator<
                SCHEMA_OBJECT,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator = allocators.getSchemaMapBuilderAllocator();

        final SCHEMA_MAP_BUILDER schemaMapBuilder = SchemaMap.createBuilder(initialCapacity, schemaMapBuilderAllocator);

        final INDEX_LIST_BUILDER schemaObjectsBuilder = IndexList.createBuilder(indexListAllocator);

        final IMutableIntSetAllocator<MutableIntMaxDistanceNonBucketSet> intSetAllocator = allocators.getIntSetAllocator();

        final MutableIntMaxDistanceNonBucketSet addedSchemaObjects = intSetAllocator.allocateMutableIntSet(CapacityExponents.computeCapacityExponent(initialCapacity));

        try {
            final IIndexList<SCHEMA_OBJECT> schemaObjects = initialSchema.getSchemaObjects(ddlObjectType);

            final long numCompleteSchemas = schemaObjects.getNumElements();

            for (long completeSchemaIndex = 0L; completeSchemaIndex < numCompleteSchemas; ++ completeSchemaIndex) {

                final SCHEMA_OBJECT schemaObject = schemaObjects.get(completeSchemaIndex);

                addEffectiveSchemaObject(schemaObjectsBuilder, ddlObjectType, schemaObject, droppedSchemaObjects, allocators.getColumnIndexListAllocator(), null);
            }

            final long numSchemaDiffs = schemaDiffs.getNumElements();

            for (long schemaDiffIndex = 0L; schemaDiffIndex < numSchemaDiffs; ++ schemaDiffIndex) {

                final DiffDatabaseSchema diffDatabaseSchema = schemaDiffs.get(schemaDiffIndex);

                final IIndexList<SCHEMA_OBJECT> diffSchemaObjects = diffDatabaseSchema.getSchemaObjects(ddlObjectType);

                final long numDiffSchemaObjects = diffSchemaObjects.getNumElements();

                for (long i = 0L; i < numDiffSchemaObjects; ++ i) {

                    final SCHEMA_OBJECT diffSchemaObject = diffSchemaObjects.get(i);

                    final int diffSchemaObjectId = diffSchemaObject.getId();

                    if (!addedSchemaObjects.contains(diffSchemaObjectId)) {

                        addEffectiveSchemaObject(schemaObjectsBuilder, ddlObjectType, diffSchemaObject, droppedSchemaObjects, allocators.getColumnIndexListAllocator(),
                                addedSchemaObjects);
                    }
                }
            }

            final INDEX_LIST effectiveSchemaObjects = schemaObjectsBuilder.build();

            schemaMapBuilder.add(effectiveSchemaObjects);

            result = schemaMapBuilder.build();
        }
        finally {

            intSetAllocator.freeMutableIntSet(addedSchemaObjects);

            indexListAllocator.freeIndexListBuilder(schemaObjectsBuilder);

            schemaMapBuilderAllocator.freeSchemaMapBuilder(schemaMapBuilder);
        }

        return result;
    }

    private static <T extends SchemaObject, U extends IndexList<Column>, V extends IndexList.IndexListBuilder<Column, U, V>, W extends IndexListAllocator<Column, U, V, ?>>
    void addEffectiveSchemaObject(IndexList.IndexListBuilder<T, ?, ?> schemaObjectsBuilder, DDLObjectType ddlObjectType, T schemaObject, DroppedSchemaObjects droppedSchemaObjects,
            W columnIndexListAllocator, MutableIntMaxDistanceNonBucketSet addedSchemaObjects) {

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

    private static <T extends SchemaObject, U extends IndexList<Column>, V extends IndexList.IndexListBuilder<Column, U, V>, W extends IndexListAllocator<Column, U, V, ?>>
    T createEffectiveSchemaObject(DDLObjectType ddlObjectType, T schemaObject, DroppedSchemaObjects droppedSchemaObjects, W columnIndexListAllocator) {

        final T result;

        if (ddlObjectType.hasColumns()) {

            final ColumnsObject columnsObject = (ColumnsObject)schemaObject;

            final int numColumns = columnsObject.getNumColumns();

            final V columnsBuilder = IndexList.createBuilder(numColumns, columnIndexListAllocator);

            try {
                for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                    final Column column = columnsObject.getColumn(columnIndex);

                    if (!droppedSchemaObjects.isDroppedColumn(ddlObjectType, columnsObject, column)) {

                        columnsBuilder.addTail(column);
                    }
                }
            }
            finally {
                final U columns = columnsBuilder.build();

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
