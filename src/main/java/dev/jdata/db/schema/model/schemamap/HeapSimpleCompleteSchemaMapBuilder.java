package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

final class HeapSimpleCompleteSchemaMapBuilder

        extends SimpleCompleteSchemaMapBuilder<IHeapSchemaObjects<SchemaObject>, IHeapCompleteSchemaMap, IHeapCompleteSchemaMap, IHeapCompleteSchemaMapBuilder>
        implements IHeapCompleteSchemaMapBuilder {

    HeapSimpleCompleteSchemaMapBuilder(AllocationType allocationType) {
        super(allocationType, IHeapSchemaObjects[]::new);
    }

    @Override
    protected IHeapCompleteSchemaMap build(AllocationType allocationType, IHeapSchemaObjects<SchemaObject>[] mutable) {

        checkSchemaMapBuildParameters(allocationType, AllocationMechanism.HEAP, mutable);

        return new HeapCompleteSchemaMap(allocationType, mapOrEmpty(mutable, DDLObjectType.TABLE), mapOrEmpty(mutable, DDLObjectType.VIEW),
                mapOrEmpty(mutable, DDLObjectType.INDEX), mapOrEmpty(mutable, DDLObjectType.TRIGGER), mapOrEmpty(mutable, DDLObjectType.FUNCTION),
                mapOrEmpty(mutable, DDLObjectType.PROCEDURE));
    }

    @Override
    protected IHeapCompleteSchemaMap empty() {

        return HeapCompleteSchemaMap.empty();
    }

    @Override
    protected IHeapCompleteSchemaMap heapBuild(AllocationType allocationType, IHeapSchemaObjects<SchemaObject>[] mutable) {

        checkSchemaMapBuildParameters(allocationType, AllocationMechanism.HEAP, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapCompleteSchemaMap heapEmpty() {

        return empty();
    }

    private static <R extends ISchemaObjects<?>> R mapOrEmpty(IHeapSchemaObjects<SchemaObject>[] schemaObjects, DDLObjectType ddlObjectType) {

        return mapOrEmpty(schemaObjects, ddlObjectType, IHeapSchemaObjects.empty());
    }
}
