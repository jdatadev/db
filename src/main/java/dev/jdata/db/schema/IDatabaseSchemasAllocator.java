package dev.jdata.db.schema;

import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects.IDroppedSchemaObjectsAllocator;
import dev.jdata.db.utils.allocators.IAllocators;

public interface IDatabaseSchemasAllocator extends IAllocators {

    DroppedSchemaObjects allocateDroppedSchemaObjects();
    void freeDroppedSchemaObjects(DroppedSchemaObjects droppedSchemaObjects);

    IDroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator();

    default DroppedSchemaObjects copyDroppedSchemaObjects(DroppedSchemaObjects toCopy) {

        final DroppedSchemaObjects copy = allocateDroppedSchemaObjects();

        copy.initialize(copy, getDroppedSchemaObjectsAllocator());

        return copy;
    }
}
