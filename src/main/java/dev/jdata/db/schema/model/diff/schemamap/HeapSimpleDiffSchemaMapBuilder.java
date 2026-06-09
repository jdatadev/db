package dev.jdata.db.schema.model.diff.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjectsBuilder;

final class HeapSimpleDiffSchemaMapBuilder<T extends SchemaObject>

        extends SimpleDiffSchemaMapBuilder<T, IHeapSchemaObjects<T>, IHeapSchemaObjectsBuilder<T>, IHeapDiffSchemaMap, IHeapDiffSchemaMap, IHeapDiffSchemaMapBuilder<T>>
        implements IHeapDiffSchemaMapBuilder<T> {

    HeapSimpleDiffSchemaMapBuilder(AllocationType allocationType, IntFunction<T[]> createSchemaObjectsArray) {
        super(allocationType, IHeapSchemaObjectsBuilder[]::new, c -> IHeapSchemaObjectsBuilder.create(allocationType, c, createSchemaObjectsArray));
    }

    @Override
    protected IHeapDiffSchemaMap build(AllocationType allocationType, IHeapSchemaObjectsBuilder<T>[] mutable) {

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
    protected IHeapDiffSchemaMap heapBuild(AllocationType allocationType, IHeapSchemaObjectsBuilder<T>[] mutable) {

        checkSchemaMapBuildParameters(allocationType, AllocationMechanism.HEAP, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapDiffSchemaMap heapEmpty() {

        return empty();
    }

    private static <T extends SchemaObject, U extends SchemaObject> IHeapSchemaObjects<U> mapOrEmpty(IHeapSchemaObjectsBuilder<T>[] schemaObjectsBuilderArray,
            DDLObjectType ddlObjectType) {

        @SuppressWarnings("unchecked")
        final IHeapSchemaObjects<U> result = (IHeapSchemaObjects<U>)mapOrEmpty(schemaObjectsBuilderArray, ddlObjectType, IHeapSchemaObjects.empty(), b -> b.buildNotEmpty());

        return result;
    }
}
