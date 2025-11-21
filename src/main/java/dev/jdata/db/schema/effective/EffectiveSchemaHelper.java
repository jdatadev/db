package dev.jdata.db.schema.effective;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.effective.EffectiveSchemaAllocators;
import dev.jdata.db.schema.allocators.effective.SchemaMapEffectiveSchemaAllocators;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.DiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.DroppedElementsSchemaObjects;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.elements.IElementsView;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IObjectIndexListView;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;

public class EffectiveSchemaHelper {

    public static <
                    MUTABLE_INT_SET extends IMutableIntSet,
                    SCHEMA_OBJECT extends SchemaObject,
                    INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                    INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST>,
                    INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                    SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>,
                    SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                    COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<SCHEMA_MAP>,
                    COMPLETE_SCHEMA_MAPS_BUILDER extends SimpleCompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>,
                    COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

            COMPLETE_SCHEMA_MAPS buildSchemaMaps(CompleteDatabaseSchema initialSchema, IObjectIndexListView<DiffDatabaseSchema> schemaDiffs,
                    DroppedElementsSchemaObjects droppedSchemaObjects,
                    SchemaMapEffectiveSchemaAllocators<
                            SCHEMA_OBJECT,
                            INDEX_LIST,
                            INDEX_LIST_BUILDER,
                            INDEX_LIST_ALLOCATOR,
                            SCHEMA_MAP,
                            SCHEMA_MAP_BUILDER,
                            COMPLETE_SCHEMA_MAPS,
                            COMPLETE_SCHEMA_MAPS_BUILDER,
                            COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR> allocators) {

        Objects.requireNonNull(initialSchema);
        Objects.requireNonNull(schemaDiffs);
        Objects.requireNonNull(droppedSchemaObjects);

        final COMPLETE_SCHEMA_MAPS result;

        final ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator
                = allocators.getCompleteSchemaMapsBuilderAllocator();

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
                    INDEX_LIST extends IBaseIndexList<SCHEMA_OBJECT>,
                    INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST>,
                    INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                    SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>,
                    SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                    COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<SCHEMA_MAP>,
                    COMPLETE_SCHEMA_MAPS_BUILDER extends SimpleCompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>,
                    COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

    SCHEMA_MAP buildSchemaMap(DDLObjectType ddlObjectType, IntFunction<SCHEMA_OBJECT[]> createValuesArray, CompleteDatabaseSchema initialSchema,
            IObjectIndexListView<DiffDatabaseSchema> schemaDiffs, DroppedElementsSchemaObjects droppedSchemaObjects, INDEX_LIST_ALLOCATOR indexListAllocator,
            EffectiveSchemaAllocators<
                    SCHEMA_OBJECT,
                    INDEX_LIST,
                    INDEX_LIST_BUILDER,
                    INDEX_LIST_ALLOCATOR,
                    SCHEMA_MAP,
                    SCHEMA_MAP_BUILDER,
                    COMPLETE_SCHEMA_MAPS,
                    COMPLETE_SCHEMA_MAPS_BUILDER,
                    COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR> allocators) {

        final SCHEMA_MAP result;

        final int initialCapacity = 1 + IElementsView.intNumElements(schemaDiffs.getNumElements());

        final SchemaMapBuilderAllocator<
                SCHEMA_OBJECT,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator = allocators.getSchemaMapBuilderAllocator();

        final SCHEMA_MAP_BUILDER schemaMapBuilder = schemaMapBuilderAllocator.createBuilder(initialCapacity);

        final INDEX_LIST_BUILDER schemaObjectsBuilder = indexListAllocator.createBuilder();

        final IBaseMutableIntSetAllocator intSetAllocator = allocators.getIntSetAllocator();

        final IMutableIntSet addedSchemaObjects = intSetAllocator.allocateMutableIntSet(CapacityExponents.computeCapacityExponent(initialCapacity));

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

            final INDEX_LIST effectiveSchemaObjects = schemaObjectsBuilder.buildOrNull();

            if (effectiveSchemaObjects != null) {

                schemaMapBuilder.addUnordered(effectiveSchemaObjects);
            }

            result = schemaMapBuilder.buildOrEmpty();
        }
        finally {

            intSetAllocator.freeMutableIntSet(addedSchemaObjects);

            indexListAllocator.freeBuilder(schemaObjectsBuilder);

            schemaMapBuilderAllocator.freeBuilder(schemaMapBuilder);
        }

        return result;
    }

    private static <
                    SCHEMA_OBJECT extends SchemaObject,
                    COLUMN_INDEX_LIST extends IBaseIndexList<Column>,
                    COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, COLUMN_INDEX_LIST>,
                    COLUMN_INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<Column, COLUMN_INDEX_LIST, COLUMN_INDEX_LIST_BUILDER>>
    void addEffectiveSchemaObject(IIndexListBuilder<SCHEMA_OBJECT, ?> schemaObjectsBuilder, DDLObjectType ddlObjectType, SCHEMA_OBJECT schemaObject,
            DroppedElementsSchemaObjects droppedSchemaObjects, COLUMN_INDEX_LIST_ALLOCATOR columnIndexListAllocator, IMutableIntSet addedSchemaObjects) {

        final SCHEMA_OBJECT effectiveSchemaObject = createEffectiveSchemaObject(ddlObjectType, schemaObject, droppedSchemaObjects, columnIndexListAllocator);

        if (effectiveSchemaObject != null) {

            if (effectiveSchemaObject == schemaObject) {

                throw new UnsupportedOperationException();
            }
            else {
                addedSchemaObjects.addUnordered(effectiveSchemaObject.getId());

                schemaObjectsBuilder.addTail(effectiveSchemaObject);
            }
        }
    }

    private static <
                    SCHEMA_OBJECT extends SchemaObject,
                    COLUMN_INDEX_LIST extends IBaseIndexList<Column>,
                    COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, COLUMN_INDEX_LIST>,
                    COLUMN_INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<Column, COLUMN_INDEX_LIST, COLUMN_INDEX_LIST_BUILDER>>
    SCHEMA_OBJECT createEffectiveSchemaObject(DDLObjectType ddlObjectType, SCHEMA_OBJECT schemaObject, DroppedElementsSchemaObjects droppedSchemaObjects,
            COLUMN_INDEX_LIST_ALLOCATOR columnIndexListAllocator) {

        final SCHEMA_OBJECT result;

        if (ddlObjectType.hasColumns()) {

            final ColumnsObject columnsObject = (ColumnsObject)schemaObject;

            final int numColumns = columnsObject.getNumColumns();

            final COLUMN_INDEX_LIST_BUILDER columnsBuilder = columnIndexListAllocator.createBuilder(numColumns);

            try {
                for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                    final Column column = columnsObject.getColumn(columnIndex);

                    if (!droppedSchemaObjects.isDroppedColumn(ddlObjectType, columnsObject, column)) {

                        columnsBuilder.addTail(column);
                    }
                }
            }
            finally {

                if (columnsBuilder.isEmpty()) {

                    result = null;
                }
                else {
                    final IHeapIndexList<Column> columns = columnsBuilder.buildHeapAllocated();

                    if (columns.getNumElements() == numColumns) {

                        result = schemaObject;
                    }
                    else {
                        @SuppressWarnings("unchecked")
                        final SCHEMA_OBJECT schemaObjectCopy = (SCHEMA_OBJECT)columnsObject.makeCopy(columns);

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
