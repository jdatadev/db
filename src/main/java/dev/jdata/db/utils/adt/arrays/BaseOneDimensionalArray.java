package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseOneDimensionalArray<T> extends BaseArray implements IOneDimensionalArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ONE_DIMENSIONAL_ARRAY;

    T elements;
    int limit;
    int capacity;

    abstract T reallocate(T elements, int newCapacity);

    BaseOneDimensionalArray(T elements, int limit, int capacity, boolean hasClearValue) {
        super(hasClearValue);

        if (DEBUG) {

            enter(b -> b.add("elements", elements).add("limit", limit).add("capacity", capacity).add("hasClearValue", hasClearValue));
        }

        Objects.requireNonNull(elements);
        Checks.isArrayLimit(limit);
        Checks.isArrayCapacity(capacity);
        Checks.isLessThanOrEqualTo(limit, capacity);

        this.elements = elements;
        this.limit = limit;
        this.capacity = capacity;

        if (DEBUG) {

            exit();
        }
    }

    BaseOneDimensionalArray(BaseOneDimensionalArray<T> toCopy, Function<T, T> copyElements) {
        super(toCopy);

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(copyElements);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyElements", copyElements));
        }

        this.elements = copyElements.apply(toCopy.elements);
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

        final int intIndex = Integers.checkUnsignedLongToUnsignedInt(index);

        final int currentLimit = limit;
        final int currentCapacity = capacity;

        if (intIndex >= currentLimit) {

            final int newLimit = intIndex + 1;

            if (intIndex >= currentCapacity) {

                final int newCapacity = this.capacity = newLimit << 1;

                this.elements = reallocate(elements, newCapacity);
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
