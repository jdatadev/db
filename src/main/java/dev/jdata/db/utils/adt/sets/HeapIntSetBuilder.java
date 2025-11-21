package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable;

final class HeapIntSetBuilder extends IntSetBuilder<IHeapIntSet, IHeapMutableIntSet, IHeapIntSet, IHeapIntSetBuilder, HeapIntSetAllocator> {

    public static abstract class BaseBuilder<T extends IBaseMutableIntSet> extends Allocatable {

        private final T set;

        BaseBuilder(T set) {
            super(AllocationType.HEAP);

            this.set = Objects.requireNonNull(set);
        }

        public final void add(int value) {

            set.addUnordered(value);
        }

        final IBaseIntSet build(IBaseIntSetAllocator<? extends IBaseIntSet> intSetAllocator) {

            return set.toImmutable(intSetAllocator);
        }
    }

}
