package dev.jdata.db.schema.model.schemamaps.cache;

import java.util.Objects;

import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.cache.CachedSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.CachedIndexList;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CachedIndexListBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated // currently not in use
final class CacheCompleteSchemaMapsBuilder extends CompleteSchemaMapsBuilder<

                CachedIndexList<SchemaObject>,
                CachedIndexListBuilder<SchemaObject>,
                CacheIndexListAllocator<SchemaObject>,
                CachedSchemaMap<SchemaObject>,
                CachedSchemaMapBuilder<SchemaObject>,
                CachedAllCompleteSchemaMaps,
                HeapAllCompleteSchemaMaps> {

    private final NodeObjectCache<CachedAllCompleteSchemaMaps> completeSchemaMapsCache;

    CacheCompleteSchemaMapsBuilder() {
        super(CachedSchemaMapBuilder[]::new);

        this.completeSchemaMapsCache = new NodeObjectCache<>(CachedAllCompleteSchemaMaps::new);
    }

    @Override
    public CachedAllCompleteSchemaMaps build() {

        final CachedAllCompleteSchemaMaps result = completeSchemaMapsCache.allocate();

        result.initializeSchemaMaps(buildSchemaMap(DDLObjectType.TABLE), buildSchemaMap(DDLObjectType.VIEW), buildSchemaMap(DDLObjectType.INDEX),
                buildSchemaMap(DDLObjectType.TRIGGER), buildSchemaMap(DDLObjectType.FUNCTION), buildSchemaMap(DDLObjectType.PROCEDURE));

        return result;
    }

    @Override
    public HeapAllCompleteSchemaMaps buildHeapAllocated() {

        return new HeapAllCompleteSchemaMaps(buildSchemaMap(DDLObjectType.TABLE), buildSchemaMap(DDLObjectType.VIEW), buildSchemaMap(DDLObjectType.INDEX),
                buildSchemaMap(DDLObjectType.TRIGGER), buildSchemaMap(DDLObjectType.FUNCTION), buildSchemaMap(DDLObjectType.PROCEDURE));
    }

    void freeCompleteSchemaMaps(CachedAllCompleteSchemaMaps completeSchemaMaps) {

        Objects.requireNonNull(completeSchemaMaps);

        completeSchemaMapsCache.free(completeSchemaMaps);
    }
}
