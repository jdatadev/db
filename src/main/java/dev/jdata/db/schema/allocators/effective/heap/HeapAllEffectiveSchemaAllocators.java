package dev.jdata.db.schema.allocators.effective.heap;

import dev.jdata.db.schema.allocators.common.heap.HeapSchemaObjectIndexListAllocators;
import dev.jdata.db.schema.allocators.effective.AllEffectiveSchemaAllocators;
import dev.jdata.db.schema.allocators.model.schemamaps.heap.IHeapCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.HeapSchemaMap.HeapSchemaMapBuilder;
import dev.jdata.db.schema.model.HeapSchemaMap.HeapSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapAllSimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;

public final class HeapAllEffectiveSchemaAllocators

        extends AllEffectiveSchemaAllocators<

                HeapIndexList<SchemaObject>,
                HeapIndexListBuilder<SchemaObject>,
                HeapIndexListAllocator<SchemaObject>,
                HeapSchemaMap<SchemaObject>,
                HeapSchemaMapBuilder<SchemaObject>,
                HeapAllCompleteSchemaMaps,
                HeapAllSimpleCompleteSchemaMapsBuilder> {

    public HeapAllEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?> columnIndexListAllocator,
            IBaseMutableIntSetAllocator intSetAllocator, IHeapCompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator,
            HeapSchemaMapBuilderAllocator<SchemaObject> schemaMapBuilderAllocator, HeapSchemaObjectIndexListAllocators<SchemaObject> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator, indexListAllocators);
    }
}
