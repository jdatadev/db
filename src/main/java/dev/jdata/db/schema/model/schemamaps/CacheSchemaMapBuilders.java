package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;

import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.cache.CachedSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.CachedIndexList;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

@Deprecated // currently not in use
final class CacheSchemaMapBuilders extends SchemaMapBuilders<

                SchemaObject,
                CachedIndexList<SchemaObject>,
                CachedIndexList.CachedIndexListBuilder<SchemaObject>,
                CacheIndexListAllocator<SchemaObject>,
                CachedSchemaMap<SchemaObject>,
                CachedSchemaMapBuilder<SchemaObject>,
                CachedCompleteSchemaMaps> {

    private final NodeObjectCache<CachedCompleteSchemaMaps> completeSchemaMapsCache;

    CacheSchemaMapBuilders() {
        super(CachedSchemaMapBuilder[]::new);

        this.completeSchemaMapsCache = new NodeObjectCache<>(CachedCompleteSchemaMaps::new);
    }

    @Override
    public CachedCompleteSchemaMaps build() {

        final CachedCompleteSchemaMaps result = completeSchemaMapsCache.allocate();

        result.initialize(build(DDLObjectType.TABLE), build(DDLObjectType.VIEW), build(DDLObjectType.INDEX), build(DDLObjectType.TRIGGER), build(DDLObjectType.FUNCTION),
                build(DDLObjectType.PROCEDURE));

        return result;
    }

    void freeCompleteSchemaMaps(CachedCompleteSchemaMaps completeSchemaMaps) {

        Objects.requireNonNull(completeSchemaMaps);

        completeSchemaMapsCache.free(completeSchemaMaps);
    }
}
