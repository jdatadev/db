package dev.jdata.db.schema.model.diff.schemamap;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

final class HeapSimpleDiffSchemaMapBuilder<T extends SchemaObject>

        extends SimpleDiffSchemaMapBuilder<T, IHeapSchemaObjects<T>, IHeapDiffSchemaMap, IHeapDiffSchemaMap, IHeapDiffSchemaMapBuilder<T>>
        implements IHeapDiffSchemaMapBuilder<T> {

    HeapSimpleDiffSchemaMapBuilder(AllocationType allocationType) {
        super(allocationType, IHeapSchemaObjects[]::new);
    }

    @Override
    protected IHeapDiffSchemaMap build(AllocationType allocationType, IHeapSchemaObjects<T>[] mutable) {

        checkSchemaMapBuildParameters(allocationType, AllocationMechanism.HEAP, mutable);

        return new HeapDiffSchemaMap<>(allocationType, mapOrEmpty(mutable, DDLObjectType.TABLE), mapOrEmpty(mutable, DDLObjectType.VIEW),
                mapOrEmpty(mutable, DDLObjectType.INDEX), mapOrEmpty(mutable, DDLObjectType.TRIGGER), mapOrEmpty(mutable, DDLObjectType.FUNCTION),
                mapOrEmpty(mutable, DDLObjectType.PROCEDURE));
    }

    @Override
    protected IHeapDiffSchemaMap empty() {

        return HeapDiffSchemaMap.empty();
    }

    @Override
    protected IHeapDiffSchemaMap heapBuild(AllocationType allocationType, IHeapSchemaObjects<T>[] mutable) {

        checkSchemaMapBuildParameters(allocationType, AllocationMechanism.HEAP, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapDiffSchemaMap heapEmpty() {

        return empty();
    }

    private static <R extends ISchemaObjects<?>> R mapOrEmpty(IHeapSchemaObjects<?>[] schemaMaps, DDLObjectType ddlObjectType) {

        return mapOrEmpty(schemaMaps, ddlObjectType, IHeapSchemaObjects.empty());
    }
}
