package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

abstract class DDLCompleteSchemaMapsWorkerObjects<

                INDEX_LIST extends IndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IndexListBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SchemaObject, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends IHeapAllCompleteSchemaMaps,
                COMPLETE_SCHEMA_MAPS_BUILDER extends CompleteSchemaMapsBuilder<
                                INDEX_LIST,
                                INDEX_LIST_BUILDER,
                                INDEX_LIST_ALLOCATOR,
                                SCHEMA_MAP,
                                SCHEMA_MAP_BUILDER,
                                COMPLETE_SCHEMA_MAPS,
                                HEAP_COMPLETE_SCHEMA_MAPS>>

        implements IDDLCompleteSchemaMapsWorkerObjects<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> {

    private final NodeObjectCache<COMPLETE_SCHEMA_MAPS_BUILDER> completeShemaMapsBuilderCache;

    abstract COMPLETE_SCHEMA_MAPS_BUILDER createCompleteSchemaMapsBuilder();

    DDLCompleteSchemaMapsWorkerObjects() {

        this.completeShemaMapsBuilderCache = new NodeObjectCache<>(this::createCompleteSchemaMapsBuilder);
    }

    @Override
    public final ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> allocateCompleteSchemaMapBuilders() {

        return completeShemaMapsBuilderCache.allocate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void freeCompleteSchemaMapBuilders(ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> completeSchemaMapsBuilder) {

        completeShemaMapsBuilderCache.free((COMPLETE_SCHEMA_MAPS_BUILDER)completeSchemaMapsBuilder);
    }
}
