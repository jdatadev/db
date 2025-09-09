package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.HeapSchemaMap.HeapSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;

public final class HeapCompleteSchemaMapsBuilder extends CompleteSchemaMapsBuilder<

                HeapIndexList<SchemaObject>,
                HeapIndexListBuilder<SchemaObject>,
                HeapIndexListAllocator<SchemaObject>,
                HeapSchemaMap<SchemaObject>,
                HeapSchemaMap.HeapSchemaMapBuilder<SchemaObject>,
                HeapAllCompleteSchemaMaps,
                HeapAllCompleteSchemaMaps> {

    public HeapCompleteSchemaMapsBuilder() {
        super(HeapSchemaMapBuilder[]::new);
    }

    @Override
    public HeapAllCompleteSchemaMaps build() {

        return buildHeapAllocated();
    }

    @Override
    public HeapAllCompleteSchemaMaps buildHeapAllocated() {

        return new HeapAllCompleteSchemaMaps(buildSchemaMap(DDLObjectType.TABLE), buildSchemaMap(DDLObjectType.VIEW), buildSchemaMap(DDLObjectType.INDEX),
                buildSchemaMap(DDLObjectType.TRIGGER), buildSchemaMap(DDLObjectType.FUNCTION), buildSchemaMap(DDLObjectType.PROCEDURE));
    }
}
