package dev.jdata.db.utils.adt.numbers;

import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public abstract class BaseNumber<T extends BaseNumber<T>> extends ObjectCacheNode implements Comparable<T> {

    protected BaseNumber(AllocationType allocationType) {
        super(allocationType);
    }
}
