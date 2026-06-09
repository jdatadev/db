package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

final class HeapSimpleCompleteSchemaMapBuilder

        extends SimpleCompleteSchemaMapBuilder<

                        IHeapSchemaObjects<SchemaObject>,
                        IHeapSchemaObjectsBuilder<SchemaObject>,
                        IHeapCompleteSchemaMap,
                        IHeapCompleteSchemaMap,
                        IHeapCompleteSchemaMapBuilder>

        implements IHeapCompleteSchemaMapBuilder {

    HeapSimpleCompleteSchemaMapBuilder(AllocationType allocationType) {
        super(allocationType, IHeapSchemaObjectsBuilder[]::new, c -> IHeapSchemaObjectsBuilder.create(allocationType, c, SchemaObject[]::new));
    }

    @Override
    protected IHeapCompleteSchemaMap build(AllocationType allocationType, IHeapSchemaObjectsBuilder<SchemaObject>[] mutable) {

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
    protected IHeapCompleteSchemaMap heapBuild(AllocationType allocationType, IHeapSchemaObjectsBuilder<SchemaObject>[] mutable) {

        checkSchemaMapBuildParameters(allocationType, AllocationMechanism.HEAP, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapCompleteSchemaMap heapEmpty() {

        return empty();
    }

    private static <R extends ISchemaObjects<?>> R mapOrEmpty(IHeapSchemaObjectsBuilder<SchemaObject>[] schemaObjects, DDLObjectType ddlObjectType) {

        @SuppressWarnings("unchecked")
        final R result = (R)mapOrEmpty(schemaObjects, ddlObjectType, IHeapSchemaObjects.empty(), b -> b.buildNotEmpty());

        return result;
    }
}
