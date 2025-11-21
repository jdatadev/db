package dev.jdata.db.ddl.helpers.buildschema;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.allocators.ObjectCache;

abstract class DDLCompleteSchemaMapsWorkerObjects<

                INDEX_LIST extends IIndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SchemaObject, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SchemaObject, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SchemaObject>,
                COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        implements IDDLCompleteSchemaMapsWorkerObjects<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> {

    private final ObjectCache<COMPLETE_SCHEMA_MAPS_BUILDER> completeShemaMapsBuilderCache;

    abstract COMPLETE_SCHEMA_MAPS_BUILDER createCompleteSchemaMapsBuilder();

    DDLCompleteSchemaMapsWorkerObjects(IntFunction<COMPLETE_SCHEMA_MAPS_BUILDER[]> createCompleteSchemaMapsArray) {

        Objects.requireNonNull(createCompleteSchemaMapsArray);

        this.completeShemaMapsBuilderCache = new ObjectCache<>(this::createCompleteSchemaMapsBuilder, createCompleteSchemaMapsArray);
    }

    @Override
    public final COMPLETE_SCHEMA_MAPS_BUILDER allocateCompleteSchemaMapsBuilder() {

        return completeShemaMapsBuilderCache.allocate();
    }
    @Override
    public final void freeCompleteSchemaMapsBuilder(COMPLETE_SCHEMA_MAPS_BUILDER completeSchemaMapsBuilder) {

        Objects.requireNonNull(completeSchemaMapsBuilder);

        completeShemaMapsBuilderCache.free(completeSchemaMapsBuilder);
    }
}
