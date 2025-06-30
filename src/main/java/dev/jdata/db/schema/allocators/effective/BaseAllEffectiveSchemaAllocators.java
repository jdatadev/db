package dev.jdata.db.schema.allocators.effective;

import java.util.Objects;

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

public abstract class BaseAllEffectiveSchemaAllocators<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<?>,
                COMPLETE_SCHEMA_MAPS_BUILDER extends CompleteSchemaMapsBuilder<?, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends EffectiveSchemaAllocators<

                SCHEMA_OBJECT,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER,
                COMPLETE_SCHEMA_MAPS,
                COMPLETE_SCHEMA_MAPS_BUILDER> {

    private final SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> indexListAllocators;

    BaseAllEffectiveSchemaAllocators(IndexListAllocator<Column, ?, ?, ?> columnIndexListAllocator, IMutableIntSetAllocator intSetAllocator,
            ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator,
            SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator);

        this.indexListAllocators = Objects.requireNonNull(indexListAllocators);
    }

    public final SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> getIndexListAllocators() {
        return indexListAllocators;
    }
}
