package dev.jdata.db.schema.model.schemaobjects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.allocators.IAllocators;

public interface ISchemaObjectsBuilderAllocator<T extends SchemaObject, U extends ISchemaObjects<T>, V extends ISchemaObjectsBuilder<T, U, ?>> extends IAllocators {

    V createBuilder(int minimumCapacity);
    void freeBuilder(V builder);
}
