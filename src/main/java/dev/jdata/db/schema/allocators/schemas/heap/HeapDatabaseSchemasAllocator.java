package dev.jdata.db.schema.allocators.schemas.heap;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.schemas.DatabaseSchemasAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedElementsSchemaObjects;

public final class HeapDatabaseSchemasAllocator extends DatabaseSchemasAllocator {

    public HeapDatabaseSchemasAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {
        super(droppedSchemaObjectsAllocator);
    }

    @Override
    public DroppedElementsSchemaObjects allocateDroppedElementsSchemaObjects() {

        return new DroppedElementsSchemaObjects();
    }

    @Override
    public void freeDroppedElementsSchemaObjects(DroppedElementsSchemaObjects droppedSchemaObjects) {

        Objects.requireNonNull(droppedSchemaObjects);
    }
}
