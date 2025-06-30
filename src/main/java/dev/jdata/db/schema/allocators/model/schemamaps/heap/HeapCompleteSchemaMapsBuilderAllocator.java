package dev.jdata.db.schema.allocators.model.schemamaps.heap;

import java.util.Objects;

import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps;

public final class HeapCompleteSchemaMapsBuilderAllocator implements IHeapCompleteSchemaMapsBuilderAllocator {

    public static final HeapCompleteSchemaMapsBuilderAllocator INSTANCE = new HeapCompleteSchemaMapsBuilderAllocator();

    @Override
    public HeapCompleteSchemaMaps.HeapCompleteSchemaMapsBuilder allocateCompleteSchemaMapsBuilder() {

        return HeapCompleteSchemaMaps.HeapCompleteSchemaMapsBuilder.INSTANCE;
    }

    @Override
    public void freeCompleteSchemaMapsBuilder(HeapCompleteSchemaMaps.HeapCompleteSchemaMapsBuilder builder) {

        Objects.requireNonNull(builder);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);
    }
}
