package dev.jdata.db.utils.allocators;

public abstract class TrackingInstanceAllocator<T> extends BaseTrackingInstanceAllocator<T, InstanceAllocationTracking> implements ITypedInstanceAllocator<T> {

    protected TrackingInstanceAllocator() {
        super(new InstanceAllocationTracking());
    }
}
