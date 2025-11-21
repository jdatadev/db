package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.adt.elements.allocators.ElementsBuilderTrackingAllocator;
import dev.jdata.db.utils.adt.lists.HeapIntIndexList.HeapIntIndexListAllocator;
import dev.jdata.db.utils.adt.lists.HeapIntIndexList.HeapIntIndexListBuilder;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.checks.Checks;

public abstract class IntIndexList extends BaseIntIndexList implements IIntIndexList {

    public interface IIntIndexListAllocator<T extends IntIndexList, U extends IntIndexListBuilder<T, U>> {

        U allocateIntIndexListBuilder(int minimumCapacity);
        void freeIntIndexListBuilder(U builder);

        void freeIntIndexList(IntIndexList list);

        default U allocateIntIndexListBuilder() {

            return allocateIntIndexListBuilder(DEFAULT_INITIAL_CAPACITY);
        }
    }

    public static abstract class IntIndexListAllocator<T extends IntIndexList, U extends IntIndexListBuilder<T, U>>

            extends ElementsBuilderTrackingAllocator<T, U>
            implements IIntIndexListAllocator<T, U> {

        @Override
        public abstract U allocateIntIndexListBuilder(int minimumCapacity);

        @Override
        public abstract void freeIntIndexListBuilder(U builder);

        abstract T allocateIntIndexListFrom(int[] values, int numElements);

        @Override
        public abstract void freeIntIndexList(IntIndexList list);

        abstract MutableIntIndexList allocateMutableIntIndexList(int minimumCapacity);
        abstract void freeMutableIntIndexList(MutableIntIndexList list);
    }

    public static HeapIntIndexListBuilder createBuilder() {

        return createBuilder(DEFAULT_INITIAL_CAPACITY);
    }

    public static HeapIntIndexListBuilder createBuilder(int initialCapacity) {

        return createBuilder(initialCapacity, HeapIntIndexListAllocator.INSTANCE);
    }

    public static <T extends IntIndexList, U extends IntIndexListBuilder<T, U>> U createBuilder(IIntIndexListAllocator<T, U> listAllcator) {

        return createBuilder(DEFAULT_INITIAL_CAPACITY, listAllcator);
    }

    public static <T extends IntIndexList, U extends IntIndexListBuilder<T, U>> U createBuilder(int minimumCapacity, IIntIndexListAllocator<T, U> listAllocator) {

        return listAllocator.allocateIntIndexListBuilder(minimumCapacity);
    }

    public static abstract class IntIndexListBuilder<T extends IntIndexList, U extends IntIndexListBuilder<T, U>> extends Allocatable implements IIntIndexListBuilder<T> {

        private final IntIndexListAllocator<T, U> listAllocator;
        private final MutableIntIndexList list;

        abstract T empty();

        IntIndexListBuilder(AllocationType allocationType, int minimumCapacity, IntIndexListAllocator<T, U> listAllocator) {
            super(allocationType);

            Checks.isInitialCapacity(minimumCapacity);
            Objects.requireNonNull(listAllocator);

            this.listAllocator = listAllocator;

            this.list = listAllocator.allocateMutableIntIndexList(minimumCapacity);
        }

        @Override
        public final boolean isEmpty() {

            return list.isEmpty();
        }

        @Override
        public final void addTail(int value) {

            checkIsAllocated();

            list.addTail(value);
        }

        @Override
        public final void addTail(int ... values) {

            checkIsAllocated();

            list.addTail(values);
        }

        @Override
        public final void addTail(IIntIterableElementsView intElements) {

            checkIsAllocated();

            list.addTail(intElements);
        }

        @Override
        public final T buildOrEmpty() {

            return isEmpty() ? empty() : build();
        }

        @Override
        public final T buildOrNull() {

            return isEmpty() ? null : build();
        }

        final long getCapacity() {

            return list.getElementCapacity();
        }

        private T build() {

            return list.makeFromElements(this, (c, e, n, i) -> i.listAllocator.allocateIntIndexListFrom(e, n));
        }
    }

    public static <T extends IntIndexList, U extends IntIndexListBuilder<T, U>> T of(int value, IIntIndexListAllocator<T, U> listAllocator) {

        Objects.requireNonNull(listAllocator);

        final U builder = createBuilder(1, listAllocator);

        builder.addTail(value);

        final T result = builder.buildOrEmpty();

        listAllocator.freeIntIndexListBuilder(builder);

        return result;
    }

    @SafeVarargs
    private static <T extends IntIndexList, U extends IntIndexListBuilder<T, U>> T of(IntIndexListAllocator<T, U> indexListAllocator, int ... elements) {

        Objects.requireNonNull(indexListAllocator);

        final IntIndexListBuilder<T, U> builder = indexListAllocator.allocateIntIndexListBuilder(elements.length);

        builder.addTail(elements);

        return builder.build();
    }


    IntIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    IntIndexList(AllocationType allocationType, int value) {
        super(allocationType, value, false);
    }

    private IntIndexList(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    IntIndexList(AllocationType allocationType, int[] values, int numElements) {
        super(allocationType, values, numElements);
    }

    private IntIndexList(IntIndexList toCopy) {
        super(toCopy);
    }
}
