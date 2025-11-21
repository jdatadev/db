package dev.jdata.db.schema.effective;

import dev.jdata.db.schema.common.HeapSchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapAllSimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;

public final class HeapAllEffectiveSchemaAllocators

        extends AllEffectiveSchemaAllocators<

                IHeapIndexList<SchemaObject>,
                IHeapIndexListBuilder<SchemaObject>,
                IHeapIndexListAllocator<SchemaObject>,
                IHeapSchemaMap<SchemaObject>,
                IHeapSchemaMapBuilder<SchemaObject>,
                HeapAllCompleteSchemaMaps,
                HeapAllSimpleCompleteSchemaMapsBuilder> {

    public HeapAllEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?> columnIndexListAllocator,
            IBaseMutableIntSetAllocator intSetAllocator, IHeapAllCompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator,
            HeapSchemaMapBuilderAllocator<SchemaObject> schemaMapBuilderAllocator, HeapSchemaObjectIndexListAllocators<SchemaObject> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator, indexListAllocators);
    }
}
