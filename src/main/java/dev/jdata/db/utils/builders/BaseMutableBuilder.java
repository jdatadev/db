package dev.jdata.db.utils.builders;

import java.util.Objects;

import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;
import dev.jdata.db.utils.adt.mutability.IImmutable;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseMutableBuilder<T extends IImmutable, U extends IImmutable & IHeapTypeMarker, V> extends BaseBuilder<T, U> {

    @FunctionalInterface
    public interface IBuilderMutableAllocator<T, P> {

        T allocate(AllocationType allocationType, long minimumCapacity, P parameter);
    }

    @FunctionalInterface
    public interface IIntCapacityBuilderMutableAllocator<T, P> {

        T allocate(AllocationType allocationType, int minimumCapacity, P parameter);
    }

    protected final void checkBuildParameters(AllocationType allocationType, IMutable mutable) {

        Checks.areEqual(allocationType, getAllocationType());
        Objects.requireNonNull(mutable);
    }

    private static void checkHeapBuildParameters(AllocationType allocationType, IMutable mutable) {

        checkHeapBuildParameters(allocationType, (Object)mutable);
    }

    private static void checkHeapBuildParameters(AllocationType allocationType, Object mutable) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(mutable);
    }

    private static void checkCachedBuildParameters(AllocationType allocationType, IMutable mutable) {

        checkCachedBuildParameters(allocationType, (Object)mutable);
    }

    private static void checkCachedBuildParameters(AllocationType allocationType, Object mutable) {

        AllocationType.checkIsCached(allocationType);
        Objects.requireNonNull(mutable);
    }

    private final V mutable;

    protected abstract T build(AllocationType allocationType, V mutable);
    protected abstract U heapBuild(AllocationType allocationType, V mutable);

    protected <P> BaseMutableBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> builderMutableAllocator) {
        super(allocationType);

        Checks.isIntInitialCapacity(minimumCapacity);
        Objects.requireNonNull(builderMutableAllocator);

        this.mutable = builderMutableAllocator.allocate(allocationType, minimumCapacity, parameter);
    }

    @Override
    protected final T build() {

        return build(getAllocationType(), mutable);
    }

    @Override
    protected final U heapBuild() {

        return heapBuild(getAllocationType(), mutable);
    }

    protected final V getMutable() {

        checkIsAllocatedRenamed();

        return mutable;
    }

    @Override
    public String toString() {

        checkIsAllocatedRenamed();

        return getClass().getSimpleName() + " [ mutable=" + mutable + "]";
    }
}
