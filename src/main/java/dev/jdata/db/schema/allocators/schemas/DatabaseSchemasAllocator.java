package dev.jdata.db.schema.allocators.schemas;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DatabaseSchemasAllocator implements IDatabaseSchemasAllocator {

    private final DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;

    protected DatabaseSchemasAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
    }

    @Override
    public DroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator() {

        return droppedSchemaObjectsAllocator;
    }
}
