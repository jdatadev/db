package dev.jdata.db.schema.model.schemaobjects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.ICachedContainsMarker;

public interface ICachedSchemaObjects<T extends SchemaObject> extends ISchemaObjects<T>, ICachedContainsMarker {

    public static <T extends SchemaObject> ICachedSchemaObjects<T> empty() {

        return CachedSchemaObjects.empty();
    }
}
