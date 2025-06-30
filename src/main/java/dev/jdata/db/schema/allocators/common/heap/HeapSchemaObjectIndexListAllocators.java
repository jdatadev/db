package dev.jdata.db.schema.allocators.common.heap;

import dev.jdata.db.schema.allocators.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;

public final class HeapSchemaObjectIndexListAllocators<T extends SchemaObject> extends SchemaObjectIndexListAllocators<HeapIndexListAllocator<T>> {

    public static final HeapSchemaObjectIndexListAllocators<?> INSTANCE = new HeapSchemaObjectIndexListAllocators<>();

    private HeapSchemaObjectIndexListAllocators() {
        super(HeapIndexListAllocator[]::new, (t, g) -> new HeapIndexListAllocator<>(t.getCreateArray()));
    }
}
