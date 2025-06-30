package dev.jdata.db.utils.allocators;

public abstract class InstanceAllocator<T> extends BaseInstanceAllocator<T, ElementAllocator> implements IInstanceAllocator<T> {

    protected InstanceAllocator() {
        super(new ElementAllocator() { });
    }
}
