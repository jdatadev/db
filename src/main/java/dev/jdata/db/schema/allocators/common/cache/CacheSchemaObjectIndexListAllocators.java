package dev.jdata.db.schema.allocators.common.cache;

import dev.jdata.db.schema.allocators.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.CachedIndexList.CacheIndexListAllocator;

@Deprecated // currently not in use
public final class CacheSchemaObjectIndexListAllocators extends SchemaObjectIndexListAllocators<CacheIndexListAllocator<? extends SchemaObject>> {

    public CacheSchemaObjectIndexListAllocators() {
        super(CacheIndexListAllocator[]::new, (t, g) -> new CacheIndexListAllocator<>(t.getCreateArray()));
    }
}
