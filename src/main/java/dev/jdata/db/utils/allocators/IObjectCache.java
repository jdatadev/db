package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer;

public interface IObjectCache<T> extends IInstanceAllocator<T> {

    void addCache(IAllocatorsStatisticsGatherer statisticsGatherer, String name, Class<T> objectType);
}
