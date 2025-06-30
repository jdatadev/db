package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.HeapSchemaMap.HeapSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIndexList.HeapIndexListBuilder;

public final class HeapSchemaMapBuilders extends SchemaMapBuilders<

                SchemaObject,
                HeapIndexList<SchemaObject>,
                HeapIndexListBuilder<SchemaObject>,
                HeapIndexListAllocator<SchemaObject>,
                HeapSchemaMap<SchemaObject>,
                HeapSchemaMap.HeapSchemaMapBuilder<SchemaObject>,
                HeapCompleteSchemaMaps> {

    public static final HeapSchemaMapBuilders INSTANCE = new HeapSchemaMapBuilders();

    private HeapSchemaMapBuilders() {
        super(HeapSchemaMapBuilder[]::new);
    }

    @Override
    public HeapCompleteSchemaMaps build() {

        return new HeapCompleteSchemaMaps(build(DDLObjectType.TABLE), build(DDLObjectType.VIEW), build(DDLObjectType.INDEX), build(DDLObjectType.TRIGGER),
                build(DDLObjectType.FUNCTION), build(DDLObjectType.PROCEDURE));
    }
}
