package dev.jdata.db.schema.effective;

import dev.jdata.db.schema.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;

abstract class CompleteEffectiveSchemaAllocators<

                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                MUTABLE_INT_SET extends IMutableIntSet,
                SCHEMA_OBJECT_INDEX_LIST extends IIndexList<SchemaObject>,
                SCHEMA_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<SchemaObject, SCHEMA_OBJECT_INDEX_LIST, ?>,
                SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SchemaObject, SCHEMA_OBJECT_INDEX_LIST, ?, SCHEMA_OBJECT_INDEX_LIST_BUILDER>,
                SCHEMA_OBJECTS extends ISchemaObjects<SchemaObject>,
                SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SchemaObject, SCHEMA_OBJECTS, ?>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap,
                HEAP_COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap & IHeapSchemaMapMarker,
                COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>>

        extends CompleteSchemaMapEffectiveSchemaAllocators<

                COLUMN_INDEX_LIST_BUILDER,
                MUTABLE_INT_SET,
                SCHEMA_OBJECT_INDEX_LIST,
                SCHEMA_OBJECT_INDEX_LIST_BUILDER,
                SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR,
                SCHEMA_OBJECTS,
                SCHEMA_OBJECTS_BUILDER,
                COMPLETE_SCHEMA_MAP,
                HEAP_COMPLETE_SCHEMA_MAP,
                COMPLETE_SCHEMA_MAP_BUILDER> {

    CompleteEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator,
            IMutableIntSetAllocator<MUTABLE_INT_SET> mutableIntSetAllocator,
            ICompleteSchemaMapBuilderAllocator<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator,
            ISchemaObjectsBuilderAllocator<SchemaObject, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER> schemaObjectsBuilderAllocator,
            SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> schemaObjectIndexListAllocators) {
        super(columnIndexListAllocator, mutableIntSetAllocator, schemaMapBuilderAllocator, schemaObjectsBuilderAllocator, schemaObjectIndexListAllocators);
    }
}
