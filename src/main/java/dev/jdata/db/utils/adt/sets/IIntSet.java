package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.IIntSetAllocator;

public interface IIntSet extends ISet, IIntSetCommon {

    public static abstract class BaseBuilder<T extends IMutableIntSet> extends Allocatable {

        private final T set;

        BaseBuilder(T set) {
            super(AllocationType.HEAP);

            this.set = Objects.requireNonNull(set);
        }

        public final void add(int value) {

            set.add(value);
        }

        final IIntSet build(IIntSetAllocator<? extends IIntSet> intSetAllocator) {

            return set.toImmutable(intSetAllocator);
        }
    }

    public static final class Builder extends BaseBuilder<MutableIntMaxDistanceNonBucketSet> {

        private Builder(int initialCapacityExponent) {
            super(new MutableIntMaxDistanceNonBucketSet(initialCapacityExponent));
        }
    }

    public static Builder createBuilder(int initialCapacityExponent) {

        return new Builder(initialCapacityExponent);
    }

    IHeapIntSet toHeapAllocated();
}
