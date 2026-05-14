package dev.jdata.db.schema.model.schemaobjects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.builders.ICachedContainsBuilderMarker;

public interface ICachedSchemaObjectsBuilder<T extends SchemaObject>

        extends ISchemaObjectsBuilder<T, ICachedSchemaObjects<T>, IHeapSchemaObjects<T>>, ICachedContainsBuilderMarker {

}
