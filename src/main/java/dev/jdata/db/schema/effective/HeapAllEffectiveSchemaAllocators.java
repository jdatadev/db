package dev.jdata.db.schema.effective;

import dev.jdata.db.schema.common.HeapSchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.sets.IHeapMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;

public final class HeapAllEffectiveSchemaAllocators

        extends AllEffectiveSchemaAllocators<

                IHeapIndexListBuilder<Column>,
                IHeapMutableIntSet,
                IHeapIndexList<SchemaObject>,
                IHeapIndexListBuilder<SchemaObject>,
                IHeapIndexListAllocator<SchemaObject>,
                IHeapSchemaMap<SchemaObject>,
                IHeapSchemaMapBuilder<SchemaObject>,
                IHeapAllCompleteSchemaMaps,
                IHeapAllCompleteSchemaMaps,
                IHeapAllCompleteSchemaMapsBuilder> {

    HeapAllEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, IHeapIndexListBuilder<Column>> columnIndexListAllocator,
            IMutableIntSetAllocator<IHeapMutableIntSet> intSetAllocator, IHeapAllCompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator,
            IHeapSchemaMapBuilderAllocator<SchemaObject> schemaMapBuilderAllocator, HeapSchemaObjectIndexListAllocators<SchemaObject> indexListAllocators) {
        super(columnIndexListAllocator, intSetAllocator, completeSchemaMapsBuilderAllocator, schemaMapBuilderAllocator, indexListAllocators);
    }
}
