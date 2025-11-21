package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapBuilder;

public interface IHeapSchemaMapBuilderAllocator<T extends SchemaObject> extends ISchemaMapBuilderAllocator<T, IHeapSchemaMap<T>, IHeapSchemaMapBuilder<T>> {

}
