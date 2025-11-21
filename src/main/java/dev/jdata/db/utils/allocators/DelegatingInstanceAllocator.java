package dev.jdata.db.utils.allocators;

import dev.jdata.db.review.IDeprecatedInstanceAllocator;

public abstract class DelegatingInstanceAllocator<T> extends BaseDelegatingInstanceAllocator<T, TrackingInstanceAllocator> implements IDeprecatedInstanceAllocator<T> {

    protected DelegatingInstanceAllocator() {
        super(new TrackingInstanceAllocator() { });
    }
}
