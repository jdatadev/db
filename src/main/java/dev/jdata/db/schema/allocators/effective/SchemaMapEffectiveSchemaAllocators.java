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
import dev.jdata.db.schema.model.schemamaps.SimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;

public abstract class SchemaMapEffectiveSchemaAllocators<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IBaseIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<SCHEMA_MAP>,
                COMPLETE_SCHEMA_MAPS_BUILDER extends SimpleCompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>,
                COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends EffectiveSchemaAllocators<

                SCHEMA_OBJECT,
                INDEX_LIST,
                INDEX_LIST_BUILDER,
                INDEX_LIST_ALLOCATOR,
                SCHEMA_MAP,
                SCHEMA_MAP_BUILDER,
                COMPLETE_SCHEMA_MAPS,
                COMPLETE_SCHEMA_MAPS_BUILDER,
                COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR> {

    private final SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> indexListAllocators;

    SchemaMapEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?> columnIndexListAllocator, IBaseMutableIntSetAllocator intSetAllocator,
            COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR completeSchemaMapsBuilderAllocator,
            SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator);

        this.indexListAllocators = Objects.requireNonNull(indexListAllocators);
    }

    public final SchemaObjectIndexListAllocators<INDEX_LIST_ALLOCATOR> getIndexListAllocators() {
        return indexListAllocators;
    }
}
