package dev.jdata.db.schema.allocators.effective;

import dev.jdata.db.schema.allocators.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;

public abstract class AllEffectiveSchemaAllocators<

                INDEX_LIST extends IndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<?>,
                COMPLETE_SCHEMA_MAPS_BUILDER extends CompleteSchemaMapsBuilder<?, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends BaseAllEffectiveSchemaAllocators<

                SchemaObject,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER,
                COMPLETE_SCHEMA_MAPS,
                COMPLETE_SCHEMA_MAPS_BUILDER> {

    protected AllEffectiveSchemaAllocators(IndexListAllocator<Column, ?, ?, ?> columnIndexListAllocator, IMutableIntSetAllocator intSetAllocator,
            ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator,
            SchemaMapBuilderAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator, indexListAllocators);
    }
}
