package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;

public final class HeapAllSimpleCompleteSchemaMapsBuilder

        extends AllSimpleCompleteSchemaMapsBuilder<HeapSchemaMap<SchemaObject>, HeapAllCompleteSchemaMaps, HeapAllSimpleCompleteSchemaMapsBuilder> {

    public static final HeapAllSimpleCompleteSchemaMapsBuilder INSTANCE = new HeapAllSimpleCompleteSchemaMapsBuilder();

    private HeapAllSimpleCompleteSchemaMapsBuilder() {
        super(HeapSchemaMap[]::new);
    }

    @Override
    public HeapAllCompleteSchemaMaps build() {

        return new HeapAllCompleteSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX),
                mapOrEmpty(DDLObjectType.TRIGGER), mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
    }

    @Override
    protected HeapSchemaMap<?> makeEmptySchema() {

        return HeapSchemaMap.empty();
    }
}
