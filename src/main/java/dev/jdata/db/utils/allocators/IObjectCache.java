package dev.jdata.db.utils.allocators;

import dev.jdata.db.review.IDeprecatedInstanceAllocator;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer;

public interface IObjectCache<T> extends IDeprecatedInstanceAllocator<T> {

    void addCache(IAllocatorsStatisticsGatherer statisticsGatherer, String name, Class<T> objectType);
}
