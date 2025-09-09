package dev.jdata.db.schema.model.schemamaps.cache;

import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.AllSimpleCompleteSchemaMapsBuilder;

public final class CachedAllSimpleCompleteSchemaBuilder

        extends AllSimpleCompleteSchemaMapsBuilder<CachedSchemaMap<SchemaObject>, CachedAllCompleteSchemaMaps, CachedAllSimpleCompleteSchemaBuilder> {

    public CachedAllSimpleCompleteSchemaBuilder() {
        super(CachedSchemaMap[]::new);
    }

    @Override
    public CachedAllCompleteSchemaMaps build() {

        return new CachedAllCompleteSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX),
                mapOrEmpty(DDLObjectType.TRIGGER), mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
    }

    @Override
    protected CachedSchemaMap<?> makeEmptySchema() {

        return CachedSchemaMap.empty();
    }
}
