package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.allocators.ICacheableMarker;

public interface ICachedSchemaMap<T extends SchemaObject> extends ISchemaMap<T>, ICacheableMarker {

    public static <T extends SchemaObject> ICachedSchemaMap<T> empty() {

        return CachedSchemaMap.empty();
    }
}
