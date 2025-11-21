package dev.jdata.db.schema.effective;

import java.util.Objects;

import dev.jdata.db.schema.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;

public abstract class SchemaMapEffectiveSchemaAllocators<

                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                MUTABLE_INT_SET extends IMutableIntSet,
                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECT_INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                SCHEMA_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?>,
                SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?, SCHEMA_OBJECT_INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>,
                COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>,
                COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends EffectiveSchemaAllocators<

                        COLUMN_INDEX_LIST_BUILDER,
                        MUTABLE_INT_SET,
                        SCHEMA_OBJECT,
                        SCHEMA_MAP,
                        SCHEMA_MAP_BUILDER,
                        COMPLETE_SCHEMA_MAPS,
                        HEAP_COMPLETE_SCHEMA_MAPS,
                        COMPLETE_SCHEMA_MAPS_BUILDER,
                        COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR> {

    private final SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> indexListAllocators;

    SchemaMapEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator,
            IMutableIntSetAllocator<MUTABLE_INT_SET> intSetAllocator, COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR completeSchemaMapsBuilderAllocator,
            ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator);

        this.indexListAllocators = Objects.requireNonNull(indexListAllocators);
    }

    public final SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> getIndexListAllocators() {
        return indexListAllocators;
    }
}
