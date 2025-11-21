package dev.jdata.db.schema.effective;

import dev.jdata.db.schema.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;

public abstract class AllEffectiveSchemaAllocators<

                MUTABLE_INT_SET extends IMutableIntSet,
                SCHEMA_OBJECT_INDEX_LIST extends IIndexList<SchemaObject>,
                SCHEMA_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<SchemaObject, SCHEMA_OBJECT_INDEX_LIST, ?>,
                SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SchemaObject, SCHEMA_OBJECT_INDEX_LIST, ?, SCHEMA_OBJECT_INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SchemaObject>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SchemaObject, SCHEMA_MAP, ?>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends IAllCompleteSchemaMapsBuilder<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends AllSchemaMapEffectiveSchemaAllocators<

                MUTABLE_INT_SET,
                SCHEMA_OBJECT_INDEX_LIST,
                SCHEMA_OBJECT_INDEX_LIST_BUILDER,
                SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER,
                COMPLETE_SCHEMA_MAPS,
                HEAP_COMPLETE_SCHEMA_MAPS,
                COMPLETE_SCHEMA_MAPS_BUILDER> {

    protected AllEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, ?> columnIndexListAllocator,
            IMutableIntSetAllocator<MUTABLE_INT_SET> intSetAllocator,
            IAllCompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator,
            ISchemaMapBuilderAllocator<SchemaObject, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator, indexListAllocators);
    }
}
