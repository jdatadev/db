package dev.jdata.db.schema.model.diff.schemamaps;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;

final class HeapDiffSchemaMapsBuilder<T extends SchemaObject>

        extends SimpleDiffSchemaMapsBuilder<T, IHeapSchemaMap<T>, IHeapDiffSchemaMaps, IHeapDiffSchemaMaps, IHeapDiffSchemaMapsBuilder<T>>
        implements IHeapDiffSchemaMapsBuilder<T> {

    HeapDiffSchemaMapsBuilder(AllocationType allocationType) {
        super(allocationType, IHeapSchemaMap[]::new);
    }

    @Override
    protected IHeapDiffSchemaMaps build(AllocationType allocationType, IHeapSchemaMap<T>[] mutable) {

        checkHeapSchemaMapsBuildParameters(allocationType, mutable);

        return new HeapDiffSchemaMaps<>(allocationType, mapOrEmpty(mutable, DDLObjectType.TABLE), mapOrEmpty(mutable, DDLObjectType.VIEW),
                mapOrEmpty(mutable, DDLObjectType.INDEX), mapOrEmpty(mutable, DDLObjectType.TRIGGER), mapOrEmpty(mutable, DDLObjectType.FUNCTION),
                mapOrEmpty(mutable, DDLObjectType.PROCEDURE));
    }

    @Override
    protected IHeapDiffSchemaMaps empty() {

        return HeapDiffSchemaMaps.empty();
    }

    @Override
    protected IHeapDiffSchemaMaps heapBuild(AllocationType allocationType, IHeapSchemaMap<T>[] mutable) {

        checkHeapSchemaMapsBuildParameters(allocationType, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapDiffSchemaMaps heapEmpty() {

        return empty();
    }

    private static <R extends ISchemaMap<?>> R mapOrEmpty(IHeapSchemaMap<?>[] schemaMaps, DDLObjectType ddlObjectType) {

        return mapOrEmpty(schemaMaps, ddlObjectType, IHeapSchemaMap.empty());
    }
}
