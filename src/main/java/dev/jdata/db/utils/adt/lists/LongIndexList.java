package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.lists.HeapLongIndexList.HeapLongIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapLongIndexList.HeapLongIndexListBuilder;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.BuilderElementAllocator;
import dev.jdata.db.utils.builders.IBuilder;
import dev.jdata.db.utils.checks.Checks;

public abstract class LongIndexList extends BaseLongIndexList implements ILongIndexList {

    public interface ILongIndexListAllocator<T extends LongIndexList, U extends LongIndexListBuilder<T, U>> {

        U allocateLongIndexListBuilder(int minimumCapacity);
        void freeLongIndexListBuilder(U builder);

        void freeLongIndexList(LongIndexList list);

        default U allocateLongIndexListBuilder() {

            return allocateLongIndexListBuilder(DEFAULT_INITIAL_CAPACITY);
        }
    }

    public static abstract class LongIndexListAllocator<T extends LongIndexList, U extends LongIndexListBuilder<T, U>> extends BuilderElementAllocator<U>

            implements ILongIndexListAllocator<T, U> {

        @Override
        public abstract U allocateLongIndexListBuilder(int minimumCapacity);

        @Override
        public abstract void freeLongIndexListBuilder(U builder);

        abstract T allocateLongIndexListFrom(long[] values, int numElements);

        @Override
        public abstract void freeLongIndexList(LongIndexList list);

        abstract MutableLongIndexList allocateMutableLongIndexList(int minimumCapacity);
        abstract void freeMutableLongIndexList(MutableLongIndexList list);
    }

    public static HeapLongIndexListBuilder createBuilder() {

        return createBuilder(DEFAULT_INITIAL_CAPACITY);
    }

    public static HeapLongIndexListBuilder createBuilder(int initialCapacity) {

        return createBuilder(initialCapacity, HeapLongIndexListAllocator.INSTANCE);
    }

    public static <T extends LongIndexList, U extends LongIndexListBuilder<T, U>> U createBuilder(ILongIndexListAllocator<T, U> listAllcator) {

        return createBuilder(DEFAULT_INITIAL_CAPACITY, listAllcator);
    }

    public static <T extends LongIndexList, U extends LongIndexListBuilder<T, U>> U createBuilder(int minimumCapacity, ILongIndexListAllocator<T, U> listAllocator) {

        return listAllocator.allocateLongIndexListBuilder(minimumCapacity);
    }

    public static abstract class LongIndexListBuilder<T extends LongIndexList, U extends LongIndexListBuilder<T, U>>

            extends Allocatable
            implements ILongIndexListBuildable<T, U>, IBuilder {

        private final LongIndexListAllocator<T, U> listAllocator;
        private final MutableLongIndexList list;

        LongIndexListBuilder(AllocationType allocationType, int minimumCapacity, LongIndexListAllocator<T, U> listAllocator) {
            super(allocationType);

            Checks.isInitialCapacity(minimumCapacity);
            Objects.requireNonNull(listAllocator);

            this.listAllocator = listAllocator;

            this.list = listAllocator.allocateMutableLongIndexList(minimumCapacity);
        }

        @Override
        public final boolean isEmpty() {

            return list.isEmpty();
        }

        @Override
        public final U addTail(long value) {

            checkIsAllocated();

            list.addTail(value);

            return getThis();
        }

        @Override
        public final U addTail(long ... values) {

            checkIsAllocated();

            list.addTail(values);

            return getThis();
        }

        @Override
        public final T build() {

            return list.makeFromElements(this, (e, n, i) -> i.listAllocator.allocateLongIndexListFrom(e, n));
        }

        final long getCapacity() {

            return list.getElementCapacity();
        }

        @SuppressWarnings("unchecked")
        private U getThis() {

            return (U)this;
        }
    }

    public static <T extends LongIndexList, U extends LongIndexListBuilder<T, U>> T of(long value, ILongIndexListAllocator<T, U> listAllocator) {

        Objects.requireNonNull(listAllocator);

        final U builder = createBuilder(1, listAllocator);

        final T result = builder.addTail(value)
                .build();

        listAllocator.freeLongIndexListBuilder(builder);

        return result;
    }

    @SafeVarargs
    private static <T extends LongIndexList, U extends LongIndexListBuilder<T, U>> T of(LongIndexListAllocator<T, U> indexListAllocator, long ... elements) {

        Objects.requireNonNull(indexListAllocator);

        final LongIndexListBuilder<T, U> builder = indexListAllocator.allocateLongIndexListBuilder(elements.length);

        builder.addTail(elements);

        return builder.build();
    }


    LongIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    LongIndexList(AllocationType allocationType, long value) {
        super(allocationType, value);
    }

    private LongIndexList(AllocationType allocationType, long[] values) {
        super(allocationType, values);
    }

    LongIndexList(AllocationType allocationType, long[] values, int numElements) {
        super(allocationType, values, numElements);
    }

    private LongIndexList(LongIndexList toCopy) {
        super(toCopy);
    }
}
