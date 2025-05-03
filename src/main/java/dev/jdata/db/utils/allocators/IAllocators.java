package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public interface IAllocators extends IAllocator {

    public interface IAllocatorsStatisticsGatherer {

        public enum RefType {

            PASSED,
            INSTANTIATED
        }

        <T> void addInstanceAllocator(String name, RefType refType, Class<?> objectType, IInstanceAllocator<T> instanceAllocator);

        void addAllocators(String name, RefType refType, IAllocators cache);
        void addAllocators(String name, RefType refType, Class<?> objectTye, IAllocators cache);

        <T> void addObjectCache(String name, Class<T> objectType, ObjectCache<T> objectCache);
        <T extends ObjectCacheNode> void addNodeObjectCache(String name, Class<T> objectType, NodeObjectCache<T> nodeObjectCache);
    }

    void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer);
}
