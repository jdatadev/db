package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;

final class HeapAllSimpleCompleteSchemaMapsBuilder

        extends AllSimpleCompleteSchemaMapsBuilder<IHeapSchemaMap<SchemaObject>, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMapsBuilder>
        implements IHeapAllCompleteSchemaMapsBuilder {

    HeapAllSimpleCompleteSchemaMapsBuilder(AllocationType allocationType) {
        super(allocationType, IHeapSchemaMap[]::new);
    }

    @Override
    protected IHeapAllCompleteSchemaMaps build(AllocationType allocationType, IHeapSchemaMap<SchemaObject>[] mutable) {

        checkHeapSchemaMapsBuildParameters(allocationType, mutable);

        return new HeapAllCompleteSchemaMaps(allocationType, mapOrEmpty(mutable, DDLObjectType.TABLE), mapOrEmpty(mutable, DDLObjectType.VIEW),
                mapOrEmpty(mutable, DDLObjectType.INDEX), mapOrEmpty(mutable, DDLObjectType.TRIGGER), mapOrEmpty(mutable, DDLObjectType.FUNCTION),
                mapOrEmpty(mutable, DDLObjectType.PROCEDURE));
    }

    @Override
    protected IHeapAllCompleteSchemaMaps empty() {

        return HeapAllCompleteSchemaMaps.empty();
    }

    @Override
    protected IHeapAllCompleteSchemaMaps heapBuild(AllocationType allocationType, IHeapSchemaMap<SchemaObject>[] mutable) {

        checkHeapSchemaMapsBuildParameters(allocationType, mutable);

        return build(allocationType, mutable);
    }

    @Override
    protected IHeapAllCompleteSchemaMaps heapEmpty() {

        return empty();
    }

    private static <R extends ISchemaMap<?>> R mapOrEmpty(IHeapSchemaMap<SchemaObject>[] schemaMaps, DDLObjectType ddlObjectType) {

        return mapOrEmpty(schemaMaps, ddlObjectType, IHeapSchemaMap.empty());
    }
}
