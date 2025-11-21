package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IImmutableIndexList;

import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.BaseObjectArrayList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public final class AddableListAllocator extends BaseCapacityInstanceAllocator<AddableListAllocator.AddableList<?>> implements IAddableListAllocator {

    public static final class AddableList<T> extends BaseObjectArrayList<T> implements IAddableList<T>, IImmutableIndexList<T>, IResettable {

        private boolean initialized;

        @SuppressWarnings("unchecked")
        public static <T> IAddableList<T> of(T instance) {

            return (AddableList<T>)new AddableList<>(AllocationType.HEAP, Object[]::new, instance);
        }

        @SafeVarargs
        public static <T> IAddableList<T> of(T ... instances) {

            Checks.checkElements(instances, Objects::requireNonNull);

            return new AddableList<>(AllocationType.HEAP, instances);
        }

        AddableList(IntFunction<T[]> createArray, int initialCapacity) {
            super(AllocationType.HEAP, createArray, initialCapacity);

            this.initialized = false;
        }

        private AddableList(AllocationType allocationType, AddableList<T> toCopy) {
            super(allocationType, toCopy);

            if (!toCopy.initialized) {

                throw new IllegalArgumentException();
            }

            initialize();
        }

        private AddableList(AllocationType allocationType, IntFunction<T[]> createArray, T instance) {
            super(allocationType, createArray, instance);

            initialize();
        }

        private AddableList(AllocationType allocationType, T[] instances) {
            super(allocationType, instances);

            initialize();
        }

        void initialize() {

            if (initialized) {

                throw new IllegalStateException();
            }

            this.initialized = true;
        }

        @Override
        public IImmutableIndexList<T> toImmutableIndexList() {

            return new AddableList<>(AllocationType.HEAP, this);
        }

        @Override
        public void add(T instance) {

            Objects.requireNonNull(instance);

            addTailElement(instance);
        }

        @Override
        public void reset() {

            if (!initialized) {

                throw new IllegalStateException();
            }

            clearElements();

            this.initialized = false;
        }

        int getCapacity() {

            return getElementsCapacity();
        }
    }

    public AddableListAllocator() {
        super(CapacityMax.INT, null, (c, p) -> new AddableList<>(AllocationType.CACHING_ALLOCATOR, Object[]::new, c), l -> l.getCapacity());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IAddableList<T> allocateList(int minimumCapacity) {

        final AddableList<T> result = (AddableList<T>)allocateFromFreeListOrCreateCapacityInstance(minimumCapacity);

        result.initialize();

        return result;
    }

    @Override
    public void freeList(IAddableList<?> list) {

        final AddableList<?> addableList = (AddableList<?>)list;

        addableList.reset();

        freeArrayInstance(addableList);
    }
}
