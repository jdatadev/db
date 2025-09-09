package dev.jdata.db.schema.allocators.model.schemamaps.heap;

import java.util.Objects;

import dev.jdata.db.schema.model.schemamaps.HeapAllSimpleCompleteSchemaMapsBuilder;

public final class HeapCompleteSchemaMapsBuilderAllocator implements IHeapCompleteSchemaMapsBuilderAllocator {

    public static final HeapCompleteSchemaMapsBuilderAllocator INSTANCE = new HeapCompleteSchemaMapsBuilderAllocator();

    @Override
    public HeapAllSimpleCompleteSchemaMapsBuilder allocateCompleteSchemaMapsBuilder() {

        return HeapAllSimpleCompleteSchemaMapsBuilder.INSTANCE;
    }

    @Override
    public void freeCompleteSchemaMapsBuilder(HeapAllSimpleCompleteSchemaMapsBuilder builder) {

        Objects.requireNonNull(builder);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);
    }
}
