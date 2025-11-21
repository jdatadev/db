package dev.jdata.db.schema.model.schemas;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;

public final class HeapDatabaseSchemasAllocator extends DatabaseSchemasAllocator {

    public HeapDatabaseSchemasAllocator(SchemaDroppedElementsAllocators droppedSchemaObjectsAllocator) {
        super(droppedSchemaObjectsAllocator);
    }

    @Override
    public DroppedElementsSchemaObjects allocateSchemaDroppedElements() {

        return new DroppedElementsSchemaObjects();
    }

    @Override
    public void freeDroppedElementsSchemaObjects(DroppedElementsSchemaObjects droppedSchemaObjects) {

        Objects.requireNonNull(droppedSchemaObjects);
    }
}
