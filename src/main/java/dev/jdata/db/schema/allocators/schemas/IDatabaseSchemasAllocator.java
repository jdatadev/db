package dev.jdata.db.schema.allocators.schemas;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.utils.allocators.IAllocators;

public interface IDatabaseSchemasAllocator extends IAllocators {

    DroppedSchemaObjects allocateDroppedSchemaObjects();
    void freeDroppedSchemaObjects(DroppedSchemaObjects droppedSchemaObjects);

    DroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator();

    default DroppedSchemaObjects copyDroppedSchemaObjects(DroppedSchemaObjects toCopy) {

        final DroppedSchemaObjects copy = allocateDroppedSchemaObjects();

        copy.initialize(copy, getDroppedSchemaObjectsAllocator());

        return copy;
    }
}
