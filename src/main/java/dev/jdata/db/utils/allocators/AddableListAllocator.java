package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;

import org.jutils.ast.objects.list.IAddableList;
import org.jutils.ast.objects.list.IImutableList;

import dev.jdata.db.utils.adt.lists.BaseArrayList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class AddableListAllocator extends BaseArrayAllocator<AddableListAllocator.AddableList<?>> implements IAddableListAllocator {

    public static final class AddableList<T> extends BaseArrayList<T> implements IAddableList<T>, IImutableList<T> {

        @SuppressWarnings("unchecked")
        public static <T> IAddableList<T> of(T instance) {

            return (AddableList<T>)new AddableList<>(Object[]::new, instance);
        }

        @SafeVarargs
        public static <T> IAddableList<T> of(T ... instances) {

            Checks.checkElements(instances, Objects::requireNonNull);

            return new AddableList<>(instances);
        }

        AddableList(IntFunction<T[]> createArray, int initialCapacity) {
            super(createArray, initialCapacity);
        }

        private AddableList(AddableList<T> toCopy) {
            super(toCopy);
        }

        private AddableList(IntFunction<T[]> createArray, T instance) {
            super(createArray, instance);
        }

        private AddableList(T[] instances) {
            super(instances);
        }

        @Override
        public IImutableList<T> toImmutableList() {

            return new AddableList<>(this);
        }

        @Override
        public void add(T instance) {

            Objects.requireNonNull(instance);

            addTail(instance);
        }
    }

    public AddableListAllocator() {
        super(c -> new AddableList<>(Object[]::new, c), l -> Integers.checkUnsignedLongToUnsignedInt(l.getCapacity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> IAddableList<T> allocateList(int minimumCapacity) {

        return (IAddableList<T>)allocateArrayInstance(minimumCapacity);
    }

    @Override
    public void freeList(IAddableList<?> list) {

        freeArrayInstance((AddableList<?>)list);
    }
}
