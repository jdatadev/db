package dev.jdata.db.schema.effective;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.IDiffDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.ISchemaDroppedElements;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexListView;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.checks.Checks;

public class EffectiveSchemaHelper {

    public static <
                    COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>,
                    MUTABLE_INT_SET extends IMutableIntSet,
                    SCHEMA_OBJECT extends SchemaObject,
                    SCHEMA_OBJECT_INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                    SCHEMA_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?>,
                    SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?, SCHEMA_OBJECT_INDEX_LIST_BUILDER>,
                    SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                    SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>,
                    COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                    COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, ?, COMPLETE_SCHEMA_MAPS_BUILDER>,
                    COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

            COMPLETE_SCHEMA_MAPS buildSchemaMaps(CompleteDatabaseSchema initialSchema, IIndexListView<IDiffDatabaseSchema> schemaDiffs,
                    SchemaMapEffectiveSchemaAllocators<

                            COLUMN_INDEX_LIST_BUILDER,
                            MUTABLE_INT_SET,
                            SCHEMA_OBJECT,
                            SCHEMA_OBJECT_INDEX_LIST,
                            SCHEMA_OBJECT_INDEX_LIST_BUILDER,
                            SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR,
                            SCHEMA_MAP,
                            SCHEMA_MAP_BUILDER,
                            COMPLETE_SCHEMA_MAPS,
                            ?,
                            COMPLETE_SCHEMA_MAPS_BUILDER,
                            COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR> allocators) {

        Objects.requireNonNull(initialSchema);
        Checks.isNotEmpty(schemaDiffs);

        final COMPLETE_SCHEMA_MAPS result;

        final ICompleteSchemaMapsBuilderAllocator<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator
                = allocators.getCompleteSchemaMapsBuilderAllocator();

        final COMPLETE_SCHEMA_MAPS_BUILDER completeSchemaMapsBuilder = completeSchemaMapsBuilderAllocator.createBuilder();

        try {
            for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

                final SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR indexListAllocator = allocators.getIndexListAllocators().getIndexListAllocator(ddlObjectType);

                final SCHEMA_MAP schemaMap = buildSchemaMap(ddlObjectType, ddlObjectType.getCreateArray(), initialSchema, schemaDiffs, indexListAllocator, allocators);

                completeSchemaMapsBuilder.setSchemaMap(ddlObjectType, schemaMap);
            }

            result = completeSchemaMapsBuilder.buildOrEmpty();
        }
        finally {

            completeSchemaMapsBuilderAllocator.freeBuilder(completeSchemaMapsBuilder);
        }

        return result;
    }

    private static <
                    COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>,
                    MUTABLE_INT_SET extends IMutableIntSet,
                    SCHEMA_OBJECT extends SchemaObject,
                    INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                    INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, ?>,
                    INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                    SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                    SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>>

    SCHEMA_MAP buildSchemaMap(DDLObjectType ddlObjectType, IntFunction<SCHEMA_OBJECT[]> createValuesArray, CompleteDatabaseSchema initialSchema,
            IIndexListView<IDiffDatabaseSchema> schemaDiffs, INDEX_LIST_ALLOCATOR indexListAllocator,
            EffectiveSchemaAllocators<

                            COLUMN_INDEX_LIST_BUILDER,
                            MUTABLE_INT_SET,
                            SCHEMA_OBJECT,
                            INDEX_LIST,
                            INDEX_LIST_BUILDER,
                            INDEX_LIST_ALLOCATOR,
                            SCHEMA_MAP,
                            SCHEMA_MAP_BUILDER,
                            ?,
                            ?,
                            ?,
                            ?> allocators) {

        final SCHEMA_MAP result;

        final int initialCapacity = 1 + IOnlyElementsView.intNumElements(schemaDiffs);

        final ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator = allocators.getSchemaMapBuilderAllocator();

        final SCHEMA_MAP_BUILDER effectiveSchemaMapBuilder = schemaMapBuilderAllocator.createBuilder(initialCapacity);
        final INDEX_LIST_BUILDER effectiveSchemaObjectsBuilder = indexListAllocator.createBuilder();

        final IMutableIntSetAllocator<MUTABLE_INT_SET> intSetAllocator = allocators.getIntSetAllocator();

        final MUTABLE_INT_SET addedSchemaObjects = intSetAllocator.createMutable(CapacityExponents.computeIntCapacityExponent(initialCapacity));

        try {
            final IIndexList<SCHEMA_OBJECT> initialSchemaObjects = initialSchema.getSchemaObjects(ddlObjectType);

            final long numInitialSchemaObjects = initialSchemaObjects.getNumElements();

            for (long initialSchemaObjectIndex = 0L; initialSchemaObjectIndex < numInitialSchemaObjects; ++ initialSchemaObjectIndex) {

                final SCHEMA_OBJECT initialSchemaObject = initialSchemaObjects.get(initialSchemaObjectIndex);

                addEffectiveSchemaObject(effectiveSchemaObjectsBuilder, ddlObjectType, initialSchemaObject, allocators.getColumnIndexListAllocator(), null);
            }

            final long numSchemaDiffs = schemaDiffs.getNumElements();

            for (long schemaDiffIndex = 0L; schemaDiffIndex < numSchemaDiffs; ++ schemaDiffIndex) {

                final IDiffDatabaseSchema diffDatabaseSchema = schemaDiffs.get(schemaDiffIndex);

                final IIndexList<SCHEMA_OBJECT> diffSchemaObjects = diffDatabaseSchema.getSchemaObjects(ddlObjectType);

                final long numDiffSchemaObjects = diffSchemaObjects.getNumElements();

                for (long i = 0L; i < numDiffSchemaObjects; ++ i) {

                    final SCHEMA_OBJECT diffSchemaObject = diffSchemaObjects.get(i);

                    final int diffSchemaObjectId = diffSchemaObject.getId();

                    if (!addedSchemaObjects.contains(diffSchemaObjectId)) {

                        addEffectiveSchemaObject(effectiveSchemaObjectsBuilder, ddlObjectType, diffSchemaObject, schemaDroppedElements, allocators.getColumnIndexListAllocator(),
                                addedSchemaObjects);
                    }
                }
            }

            final INDEX_LIST effectiveSchemaObjects = effectiveSchemaObjectsBuilder.buildOrNull();

            if (effectiveSchemaObjects != null) {

                effectiveSchemaMapBuilder.addUnordered(effectiveSchemaObjects);
            }

            result = effectiveSchemaMapBuilder.buildOrEmpty();
        }
        finally {

            intSetAllocator.freeMutableInstance(addedSchemaObjects);

            indexListAllocator.freeBuilder(effectiveSchemaObjectsBuilder);

            schemaMapBuilderAllocator.freeBuilder(effectiveSchemaMapBuilder);
        }

        return result;
    }

    private static <T extends SchemaObject, U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>, V extends IIndexListAllocator<Column, ?, ?, U>>
    void addEffectiveSchemaObject(IIndexListBuilder<T, ?, ?> schemaObjectsBuilder, DDLObjectType ddlObjectType, T schemaObject, V columnIndexListAllocator,
            IMutableIntSet addedSchemaObjects) {

        final T effectiveSchemaObject = createEffectiveSchemaObject(ddlObjectType, schemaObject, columnIndexListAllocator);

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

    private static <T extends SchemaObject, U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>, V extends IIndexListAllocator<Column, ?, ?, U>>
    T createEffectiveSchemaObject(DDLObjectType ddlObjectType, T schemaObject, ISchemaDroppedElements schemaDroppedElements, V columnIndexListAllocator) {

        final T result;

        if (ddlObjectType.hasColumns()) {

            final ColumnsObject columnsObject = (ColumnsObject)schemaObject;

            final int numColumns = columnsObject.getNumColumns();

            final U columnsBuilder = columnIndexListAllocator.createBuilder(numColumns);

            try {
                for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                    final Column column = columnsObject.getColumn(columnIndex);

                    if (!schemaDroppedElements.isDroppedColumn(ddlObjectType, columnsObject, column)) {

                        columnsBuilder.addTail(column);
                    }
                }
            }
            finally {

                if (columnsBuilder.isEmpty()) {

                    result = null;
                }
                else {
                    final IHeapIndexList<Column> columns = columnsBuilder.buildHeapAllocatedOrEmpty();

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

            columnIndexListAllocator.freeBuilder(columnsBuilder);
        }
        else {
            result = schemaObject;
        }

        return result;
    }
}
