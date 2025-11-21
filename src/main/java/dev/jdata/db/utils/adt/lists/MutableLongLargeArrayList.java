package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.ILongElementPredicate;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.function.LongComparator;

@Deprecated // currently not in use
abstract class MutableLongLargeArrayList extends Allocatable implements IMutableLongLargeIndexList {

    private final IMutableLongLargeArray mutableLongLargeArray;

    MutableLongLargeArrayList(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, BiIntToObjectFunction<IMutableLongLargeArray> createArray) {
        super(allocationType);

        Checks.isIntInitialOuterCapacityAtOrAboveZero(initialOuterCapacity);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);
        Objects.requireNonNull(createArray);

        this.mutableLongLargeArray = createArray.apply(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    public final long getCapacity() {

        return mutableLongLargeArray.getCapacity();
    }

    @Override
    public final long getNumElements() {

        return mutableLongLargeArray.getLimit();
    }

    @Override
    public final void clear() {

        mutableLongLargeArray.clear();
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        mutableLongLargeArray.forEach(parameter, forEach);
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return mutableLongLargeArray.forEachWithResult(defaultResult, parameter1, parameter2, forEach);
    }

    @Override
    public final boolean contains(long value) {

        return mutableLongLargeArray.contains(value);
    }

    @Override
    public final <P> boolean contains(P parameter, ILongElementPredicate<P> predicate) {

        Objects.requireNonNull(predicate);

        return mutableLongLargeArray.contains(parameter, predicate);
    }

    @Override
    public final boolean containsOnly(long value) {

        return mutableLongLargeArray.containsOnly(value);
    }

    @Override
    public final boolean containsOnly(long value, ILongContainsOnlyPredicate predicate) {

        Objects.requireNonNull(predicate);

        return mutableLongLargeArray.containsOnly(value, predicate);
    }

    @Override
    public final <P> long findAtMostOneIndex(P parameter, ILongElementPredicate<P> predicate) {

        return mutableLongLargeArray.findAtMostOneIndex(parameter, predicate);
    }

    @Override
    public final long[] toArray() {

        return mutableLongLargeArray.toArray();
    }

    @Override
    public final long get(long index) {

        Checks.isLongIndex(index);

        return mutableLongLargeArray.get(index);
    }

    @Override
    public final long removeTailAndReturnValue() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void addTail(long value) {

        mutableLongLargeArray.add(value);
    }

    @Override
    public final void set(long index, long value) {

        Checks.checkLongIndex(index, getNumElements());

        mutableLongLargeArray.set(index, value);
    }

    @Override
    public final long setAndReturnPrevious(long index, long value) {

        Checks.checkLongIndex(index, getNumElements());

        return mutableLongLargeArray.setAndReturnPrevious(index, value);
    }

    @Override
    public final void sort(LongComparator comparator) {

        Objects.requireNonNull(comparator);

        throw new UnsupportedOperationException();
    }
}
