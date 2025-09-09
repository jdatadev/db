package dev.jdata.db.schema.allocators.schemas;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedElementsSchemaObjects;
import dev.jdata.db.utils.allocators.IAllocators;

public interface IDatabaseSchemasAllocator extends IAllocators {

    DroppedElementsSchemaObjects allocateDroppedElementsSchemaObjects();
    void freeDroppedElementsSchemaObjects(DroppedElementsSchemaObjects droppedElementsSchemaObjects);

    DroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator();

    default DroppedElementsSchemaObjects copyDroppedSchemaObjects(DroppedElementsSchemaObjects toCopy) {

        final DroppedElementsSchemaObjects copy = allocateDroppedElementsSchemaObjects();

        copy.initialize(copy, getDroppedSchemaObjectsAllocator());

        return copy;
    }
}
