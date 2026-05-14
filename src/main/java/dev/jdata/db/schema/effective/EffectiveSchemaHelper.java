package dev.jdata.db.schema.effective;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.databaseschema.IDiffDatabaseSchema;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMap;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilderAllocator;
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
                    SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                    SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, ?>,
                    NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                    NON_DIFF_SCHEMA_MAP_BUILDER extends INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, ?, NON_DIFF_SCHEMA_MAP_BUILDER>,
                    NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR extends INonDiffSchemaMapBuilderAllocator<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>>

            NON_DIFF_SCHEMA_MAP buildSchemaMaps(CompleteDatabaseSchema initialSchema, IIndexListView<IDiffDatabaseSchema> schemaDiffs,
                    SchemaMapEffectiveSchemaAllocators<

                            COLUMN_INDEX_LIST_BUILDER,
                            MUTABLE_INT_SET,
                            SCHEMA_OBJECT,
                            SCHEMA_OBJECT_INDEX_LIST,
                            SCHEMA_OBJECT_INDEX_LIST_BUILDER,
                            SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR,
                            SCHEMA_OBJECTS,
                            SCHEMA_OBJECTS_BUILDER,
                            NON_DIFF_SCHEMA_MAP,
                            ?,
                            NON_DIFF_SCHEMA_MAP_BUILDER,
                            NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR> allocators) {

        Objects.requireNonNull(initialSchema);
        Checks.isNotEmpty(schemaDiffs);

        final NON_DIFF_SCHEMA_MAP result;

        final INonDiffSchemaMapBuilderAllocator<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator
                = allocators.getSchemaMapBuilderAllocator();

        final NON_DIFF_SCHEMA_MAP_BUILDER completeSchemaMapsBuilder = schemaMapBuilderAllocator.createBuilder();

        try {
            for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

                final SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR indexListAllocator = allocators.getSchemaObjectIndexListAllocators().getIndexListAllocator(ddlObjectType);

                final SCHEMA_OBJECTS schemaObjects = buildSchemaMap(ddlObjectType, ddlObjectType.getCreateArray(), initialSchema, schemaDiffs, indexListAllocator, allocators);

                completeSchemaMapsBuilder.setSchemaObjects(ddlObjectType, schemaObjects);
            }

            result = completeSchemaMapsBuilder.buildOrEmpty();
        }
        finally {

            schemaMapBuilderAllocator.freeBuilder(completeSchemaMapsBuilder);
        }

        return result;
    }

    private static <
                    COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>,
                    MUTABLE_INT_SET extends IMutableIntSet,
                    SCHEMA_OBJECT extends SchemaObject,
                    SCHEMA_OBJECT_INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                    SCHEMA_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?>,
                    SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?, SCHEMA_OBJECT_INDEX_LIST_BUILDER>,
                    SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                    SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, ?>>

    SCHEMA_OBJECTS buildSchemaMap(DDLObjectType ddlObjectType, IntFunction<SCHEMA_OBJECT[]> createSchemaObjectArray, CompleteDatabaseSchema initialSchema,
            IIndexListView<IDiffDatabaseSchema> schemaDiffs, SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR indexListAllocator,
            EffectiveSchemaAllocators<

                            COLUMN_INDEX_LIST_BUILDER,
                            MUTABLE_INT_SET,
                            SCHEMA_OBJECT,
                            SCHEMA_OBJECTS,
                            SCHEMA_OBJECTS_BUILDER,
                            ?,
                            ?,
                            ?,
                            ?> allocators) {

        final SCHEMA_OBJECTS result;

        final int initialCapacity = 1 + IOnlyElementsView.intNumElements(schemaDiffs);

        final ISchemaObjectsBuilderAllocator<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER> schemaObjectsBuilderAllocator = allocators.getSchemaObjectsBuilderAllocator();

        final SCHEMA_OBJECTS_BUILDER effectiveSchemaObjectsBuilder = schemaObjectsBuilderAllocator.createBuilder(initialCapacity);
        final SCHEMA_OBJECT_INDEX_LIST_BUILDER effectiveSchemaObjectsListBuilder = indexListAllocator.createBuilder();

        final IMutableIntSetAllocator<MUTABLE_INT_SET> mutableIntSetAllocator = allocators.getMutableIntSetAllocator();

        final MUTABLE_INT_SET addedSchemaObjects = mutableIntSetAllocator.createMutable(initialCapacity);

        final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator = allocators.getColumnIndexListAllocator();

        try {
            final IIndexList<SCHEMA_OBJECT> initialSchemaObjects = initialSchema.getSchemaObjectsList(ddlObjectType);

            final long numInitialSchemaObjects = initialSchemaObjects.getNumElements();

            for (int initialSchemaObjectIndex = 0; initialSchemaObjectIndex < numInitialSchemaObjects; ++ initialSchemaObjectIndex) {

                final SCHEMA_OBJECT initialSchemaObject = initialSchemaObjects.get(initialSchemaObjectIndex);

                if (!isSchemaDroppedObject(initialSchemaObject, schemaDiffs, 0)) {

                    addEffectiveSchemaObject(effectiveSchemaObjectsListBuilder, ddlObjectType, initialSchemaObject, schemaDiffs, 0, columnIndexListAllocator, addedSchemaObjects);
                }
            }

            final long numSchemaDiffs = schemaDiffs.getNumElements();

            for (int schemaDiffIndex = 0; schemaDiffIndex < numSchemaDiffs; ++ schemaDiffIndex) {

                final IDiffDatabaseSchema diffDatabaseSchema = schemaDiffs.get(schemaDiffIndex);
                final IIndexList<SCHEMA_OBJECT> diffSchemaObjects = diffDatabaseSchema.getSchemaObjectsList(ddlObjectType);
                final long numDiffSchemaObjects = diffSchemaObjects.getNumElements();

                final int nextSchemaDiffIndex = schemaDiffIndex + 1;

                for (int diffSchemaObjectIndex = 0; diffSchemaObjectIndex < numDiffSchemaObjects; ++ diffSchemaObjectIndex) {

                    final SCHEMA_OBJECT diffSchemaObject = diffSchemaObjects.get(diffSchemaObjectIndex);

                    final int diffSchemaObjectId = diffSchemaObject.getId();

                    if (!addedSchemaObjects.contains(diffSchemaObjectId) && !isSchemaDroppedObject(diffSchemaObject, schemaDiffs, nextSchemaDiffIndex)) {

                        addEffectiveSchemaObject(effectiveSchemaObjectsListBuilder, ddlObjectType, diffSchemaObject, schemaDiffs, nextSchemaDiffIndex, columnIndexListAllocator,
                                addedSchemaObjects);
                    }
                }
            }

            final SCHEMA_OBJECT_INDEX_LIST effectiveSchemaObjects = effectiveSchemaObjectsListBuilder.buildOrNull();

            if (effectiveSchemaObjects != null) {

                effectiveSchemaObjectsBuilder.addUnordered(effectiveSchemaObjects);
            }

            result = effectiveSchemaObjectsBuilder.buildOrEmpty();
        }
        finally {

            mutableIntSetAllocator.freeMutable(addedSchemaObjects);

            indexListAllocator.freeBuilder(effectiveSchemaObjectsListBuilder);

            schemaObjectsBuilderAllocator.freeBuilder(effectiveSchemaObjectsBuilder);
        }

        return result;
    }

    private static <T extends SchemaObject, U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>, V extends IIndexListAllocator<Column, ?, ?, U>>
    void addEffectiveSchemaObject(IIndexListBuilder<T, ?, ?> schemaObjectsBuilder, DDLObjectType ddlObjectType, T schemaObject, IIndexListView<IDiffDatabaseSchema> diffSchemas,
            int diffSchemasStartIndex, V columnIndexListAllocator, IMutableIntSet addedSchemaObjects) {

        final T effectiveSchemaObject = createEffectiveSchemaObject(ddlObjectType, schemaObject, diffSchemas, diffSchemasStartIndex, columnIndexListAllocator);

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

    private static <T extends SchemaObject> boolean isSchemaDroppedObject(T schemaObject, IIndexListView<IDiffDatabaseSchema> diffSchemas, int diffSchemasStartIndex) {

        return diffSchemas.contains(diffSchemasStartIndex, diffSchemas.getNumElements() - diffSchemasStartIndex, schemaObject,
                (s, o) -> s.getSchemaDroppedElements().isDroppedObject(o.getDDLObjectType(), o));
    }

    private static <T extends SchemaObject, U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>, V extends IIndexListAllocator<Column, ?, ?, U>>
    T createEffectiveSchemaObject(DDLObjectType ddlObjectType, T schemaObject, IIndexListView<IDiffDatabaseSchema> diffSchemas, int diffSchemasStartIndex,
            V columnIndexListAllocator) {

        final T result;

        if (ddlObjectType.hasColumns()) {

            final ColumnsObject columnsObject = (ColumnsObject)schemaObject;

            final int numColumns = columnsObject.getNumColumns();

            final U columnsBuilder = columnIndexListAllocator.createBuilder(numColumns);

            try {
                final long numDiffSchemas = diffSchemas.getNumElements();

                for (int columnIndex = 0; columnIndex < numColumns; ++ columnIndex) {

                    final Column column = columnsObject.getColumn(columnIndex);

                    boolean isDroppedColumn = false;

                    for (int diffSchemaIndex = diffSchemasStartIndex; diffSchemaIndex < numDiffSchemas; ++ diffSchemaIndex) {

                        if (diffSchemas.get(diffSchemaIndex).getSchemaDroppedElements().isDroppedColumn(ddlObjectType, columnsObject, column)) {

                            isDroppedColumn = true;
                            break;
                        }
                    }

                    if (!isDroppedColumn) {

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
