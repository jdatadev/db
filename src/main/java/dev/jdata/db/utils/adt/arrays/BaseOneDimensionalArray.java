package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseOneDimensionalArray<T> extends BaseAnyDimensionalArray<T, T, T> implements IOneDimensionalArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ONE_DIMENSIONAL_ARRAY;

    static <T> void checkReallocateParameters(T elementsArray, int elementsArrayLength, int newCapacity) {

        Objects.requireNonNull(elementsArray);
        Checks.isArrayLength(elementsArrayLength);
        Checks.isAboveZero(elementsArrayLength);
        Checks.isIntCapacity(newCapacity);
        Checks.isGreaterThan(newCapacity, elementsArrayLength);
    }

    final void checkClearElementsArrayParameters(T elementsArray, int elementsArrayLength, int startIndex, int numElements) {

        assertShouldClear();

        Objects.requireNonNull(elementsArray);
        Checks.isArrayLength(elementsArrayLength);
        Checks.isAboveZero(elementsArrayLength);
        Checks.isIntNumElements(numElements);
        Checks.isAboveZero(numElements);
        Checks.checkFromIndexSize(startIndex, numElements, elementsArrayLength);
    }

    private final IntFunction<T> createElementsArray;

    private T elementsArray;
    private int limit;
    private int capacity;

    abstract T reallocate(T elementsArray, int newCapacity);
    abstract void clearElementsArray(T elementsArray, int startIndex, int numElements);

    BaseOneDimensionalArray(AllocationType allocationType, T elementsArray, int limit, int capacity, boolean hasClearValue, IntFunction<T> createElementsArray) {
        super(allocationType, hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("elementsArray", elementsArray).add("limit", limit).add("capacity", capacity)
                    .add("hasClearValue", hasClearValue).add("createElementsArray", createElementsArray));
        }

        Objects.requireNonNull(elementsArray);
        Checks.isArrayLimit(limit);
        Checks.isArrayCapacity(capacity);
        Checks.isLessThanOrEqualTo(limit, capacity);

        this.createElementsArray = createElementsArray;

        this.elementsArray = elementsArray;
        this.limit = limit;
        this.capacity = capacity;

        if (DEBUG) {

            exit();
        }
    }

    BaseOneDimensionalArray(AllocationType allocationType, BaseOneDimensionalArray<T> toCopy, Function<T, T> copyElements) {
        super(allocationType, toCopy);

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(copyElements);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyElements", copyElements));
        }

        this.createElementsArray = toCopy.createElementsArray;
        this.elementsArray = copyElements.apply(toCopy.elementsArray);
        this.limit = toCopy.limit;
        this.capacity = toCopy.capacity;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean isEmpty() {

        return limit == 0;
    }

    @Override
    public final long getLimit() {

        return limit;
    }

    @Override
    public final long getIndexLimit() {

        return limit;
    }

    @Override
    final long getToStringLimit() {

        return limit;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, T, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, createElementsArray, elementsArray, getLimit(), parameter);
    }

    @Override
    protected final void recreateElements() {

        final int capacity = this.capacity = DEFAULT_INITIAL_CAPACITY;

        this.elementsArray = createElementsArray.apply(capacity);

        clearArray();
    }

    @Override
    protected final void resetToNull() {

        this.elementsArray = null;
    }

    @Override
    protected final void initializeWithValues(T values, long numElements) {

        Objects.requireNonNull(values);
        Checks.isIntNumElements(numElements);

        this.elementsArray = values;
        this.limit = intNumElements(numElements);
    }

    final T getElementsArray() {
        return elementsArray;
    }

    final void setElementsArray(T elementsArray) {

        Objects.requireNonNull(elementsArray);

        this.elementsArray = Objects.requireNonNull(elementsArray);
    }

    final int ensureAddIndex() {

        if (DEBUG) {

            enter();
        }

        final int result = ensureIndex(limit);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final int ensureIndex(long index) {

        if (DEBUG) {

            enter(b -> b.add("index", index));
        }

        final int intIndex = intIndex(index);

        final int currentLimit = limit;
        final int currentCapacity = capacity;

        if (intIndex >= currentLimit) {

            final int newLimit = intIndex + 1;

            if (intIndex >= currentCapacity) {

                final int newCapacity = this.capacity = newLimit << 1;

                this.elementsArray = reallocate(elementsArray, newCapacity);
            }

            if (hasClearValue()) {

                clearElementsArray(elementsArray, currentLimit, newLimit - currentLimit);
            }

            this.limit = newLimit;
        }

        if (DEBUG) {

            exit(intIndex);
        }

        return intIndex;
    }

    final void clearArray() {

        if (DEBUG) {

            enter();
        }

        this.limit = 0;

        if (DEBUG) {

            exit();
        }
    }
}
