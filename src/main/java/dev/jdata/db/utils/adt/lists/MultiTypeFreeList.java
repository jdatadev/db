package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.BaseNumElements;
import dev.jdata.db.utils.adt.maps.IHeapMutableNonRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableNonRemoveStaticMap;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

public final class MultiTypeFreeList<T> extends BaseNumElements<IMutableNonRemoveStaticMap<Class<?>, FreeList<T>>, Void, Void> implements IFreeing<T> {

    private final IHeapMutableNonRemoveStaticMap<Class<?>, FreeList<T>> freeListsByType;

    @SafeVarargs
    public MultiTypeFreeList(AllocationType allocationType, IntFunction<T[]> createArray, Class<? extends T> ... typesToAllocate) {
        super(allocationType);

        Objects.requireNonNull(createArray);
        Checks.isNotEmpty(typesToAllocate);
        Checks.checkElements(typesToAllocate, Objects::requireNonNull);

        final int numTypesToAllocate = typesToAllocate.length;

        this.freeListsByType = IHeapMutableNonRemoveStaticMap.create(numTypesToAllocate, Class[]::new, FreeList[]::new);

        for (Class<? extends T> typeToAllocate : typesToAllocate) {

            freeListsByType.put(typeToAllocate, new FreeList<>(createArray));
        }

        if (freeListsByType.getNumElements() != numTypesToAllocate) {

            throw new IllegalArgumentException();
        }
    }

    public T allocate(Class<? extends T> typeToAllocate) {

        Objects.requireNonNull(typeToAllocate);

        final FreeList<T> freeList = freeListsByType.get(typeToAllocate);

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

    @Override
    protected <P, R> R makeFromElements(AllocationType allocationType, P parameter,
            IMakeFromElementsFunction<IMutableNonRemoveStaticMap<Class<?>, FreeList<T>>, Void, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected Void copyValues(Void values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected void initializeWithValues(Void values, long numElements) {

        checkIntIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
    }
}
