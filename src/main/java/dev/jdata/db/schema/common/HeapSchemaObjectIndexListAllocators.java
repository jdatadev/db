package dev.jdata.db.schema.common;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;

public final class HeapSchemaObjectIndexListAllocators<T extends SchemaObject> extends SchemaObjectIndexListAllocators<IHeapIndexListAllocator<T>> {

    public static final HeapSchemaObjectIndexListAllocators<?> INSTANCE = new HeapSchemaObjectIndexListAllocators<>();

    private HeapSchemaObjectIndexListAllocators() {
        super(IHeapIndexListAllocator[]::new, (t, g) -> IHeapIndexListAllocator.create(t.getCreateArray()));
    }
}
