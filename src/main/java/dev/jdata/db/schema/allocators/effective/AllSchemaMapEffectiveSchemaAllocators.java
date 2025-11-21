package dev.jdata.db.schema.allocators.effective;

import dev.jdata.db.schema.allocators.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.IAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.AllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.AllSimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;

public abstract class AllSchemaMapEffectiveSchemaAllocators<

                INDEX_LIST extends IBaseIndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SchemaObject, INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends SchemaMap<SchemaObject, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends AllCompleteSchemaMaps<SCHEMA_MAP>,
                COMPLETE_SCHEMA_MAPS_BUILDER extends AllSimpleCompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends SchemaMapEffectiveSchemaAllocators<

                SchemaObject,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER,
                COMPLETE_SCHEMA_MAPS,
                COMPLETE_SCHEMA_MAPS_BUILDER,
                IAllCompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>> {

    AllSchemaMapEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?> columnIndexListAllocator, IBaseMutableIntSetAllocator intSetAllocator,
            IAllCompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator,
            SchemaMapBuilderAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator, indexListAllocators);
    }
}
