package dev.jdata.db.utils.adt.lists;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

public final class MultiTypeFreeList<T> extends BaseNumElements implements IFreeing<T>, IElements {

    private final Map<Class<?>, FreeList<T>> freeListsByType;

    @SafeVarargs
    public MultiTypeFreeList(IntFunction<T[]> createArray, Class<? extends T> ... typesToAllocate) {

        Objects.requireNonNull(createArray);
        Checks.isNotEmpty(typesToAllocate);
        Checks.checkElements(typesToAllocate, Objects::requireNonNull);

        final int numTypesToAllocate = typesToAllocate.length;

        this.freeListsByType = new HashMap<>(numTypesToAllocate);

        for (Class<? extends T> typeToAllocate : typesToAllocate) {

            freeListsByType.put(typeToAllocate, new FreeList<>(createArray));
        }

        if (freeListsByType.size() != numTypesToAllocate) {

            throw new IllegalArgumentException();
        }
    }

    public T allocate(Class<? extends T> typeToAllocate) {

        Objects.requireNonNull(typeToAllocate);

        final FreeList<T> freeList = freeListsByType.get(typeToAllocate);

        if (freeList == null) {

            throw new IllegalArgumentException();
        }

        final T result = freeList.allocate();

        decrementNumElements();

        return result;
    }

    @Override
    public void free(T instance) {

        Objects.requireNonNull(instance);

        final FreeList<T> freeList = freeListsByType.get(instance.getClass());

        if (freeList == null) {

            throw new IllegalArgumentException();
        }

        freeList.free(instance);

        incrementNumElements();
    }
}
