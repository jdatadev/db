package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.lists.BaseLongValues;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseLongToObjectValues<

                INSTANCE,
                T,
                LIST extends BaseLongToObjectBucketMapMultiHeadSinglyLinkedList<INSTANCE, T, LIST, VALUES>,
                VALUES extends BaseLongToObjectValues<INSTANCE, T, LIST, VALUES>>

        extends BaseLongValues<LIST, VALUES> {

    private final IntFunction<T[]> createInnerArray;

    private T[][] objectValues;

    protected BaseLongToObjectValues(int initialOuterCapacity, IntFunction<T[][]> createOuterArray, IntFunction<T[]> createInnerArray) {
        super(initialOuterCapacity);

        Objects.requireNonNull(createOuterArray);
        Objects.requireNonNull(createInnerArray);

        this.createInnerArray = createInnerArray;

        this.objectValues = createOuterArray.apply(initialOuterCapacity);
    }

    public final T getObjectValue(int outerIndex, int innerIndex) {

        return objectValues[outerIndex][innerIndex];
    }

    public final void setObjectValue(int outerIndex, int innerIndex, T value) {

        objectValues[outerIndex][innerIndex] = value;
    }

    public final T getAndSetObjectValue(int outerIndex, int innerIndex, T value) {

        final T[] innerArray = objectValues[outerIndex];

        final T result = innerArray[innerIndex];

        innerArray[innerIndex] = value;

        return result;
    }

    @Override
    protected final void reallocateOuter(int newOuterLength) {

        Checks.isGreaterThan(newOuterLength, objectValues.length);

        super.reallocateOuter(newOuterLength);

        this.objectValues = Arrays.copyOf(objectValues, newOuterLength);
    }

    @Override
    protected final void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isIndex(outerIndex);
        Checks.isCapacity(innerCapacity);

        super.allocateInner(outerIndex, innerCapacity);

        objectValues[outerIndex] = createInnerArray.apply(innerCapacity);
    }
}
