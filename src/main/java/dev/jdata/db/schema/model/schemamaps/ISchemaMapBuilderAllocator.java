package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.utils.allocators.IAllocators;

public interface ISchemaMapBuilderAllocator<T extends SchemaObject, U extends ISchemaMap<T>, V extends ISchemaMapBuilder<T, U, ?>> extends IAllocators {

    V createBuilder(int minimumCapacity);
    void freeBuilder(V builder);
}
