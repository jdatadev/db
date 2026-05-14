package dev.jdata.db.schema.effective;

import dev.jdata.db.schema.common.HeapSchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjectsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.sets.IHeapMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;

public final class HeapCompleteEffectiveSchemaAllocators

        extends CompleteEffectiveSchemaAllocators<

                IHeapIndexListBuilder<Column>,
                IHeapMutableIntSet,
                IHeapIndexList<SchemaObject>,
                IHeapIndexListBuilder<SchemaObject>,
                IHeapIndexListAllocator<SchemaObject>,
                IHeapSchemaObjects<SchemaObject>,
                IHeapSchemaObjectsBuilder<SchemaObject>,
                IHeapCompleteSchemaMap,
                IHeapCompleteSchemaMap,
                IHeapCompleteSchemaMapBuilder> {

    HeapCompleteEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, IHeapIndexListBuilder<Column>> columnIndexListAllocator,
            IMutableIntSetAllocator<IHeapMutableIntSet> mutableIntSetAllocator, IHeapCompleteSchemaMapBuilderAllocator schemaMapBuilderAllocator,
            IHeapSchemaObjectsBuilderAllocator<SchemaObject> schemaObjectsBuilderAllocator, HeapSchemaObjectIndexListAllocators<SchemaObject> schemaObjectIndexListAllocators) {
        super(columnIndexListAllocator, mutableIntSetAllocator, schemaMapBuilderAllocator, schemaObjectsBuilderAllocator, schemaObjectIndexListAllocators);
    }
}
