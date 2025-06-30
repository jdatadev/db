package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.ObjLongConsumer;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.strings.StringBuilders;
import dev.jdata.db.utils.checks.Checks;

public final class LargeLongArray extends LargeLimitArray<long[][], long[]> implements IClearable, ILongArray {

    private static final ArrayClearer<long[], Long> arrayClearer = (a, s, n, p) -> {

        if  (p != null) {

            Arrays.fill(a, s, s + n, p.longValue());
        }
    };

    private final Long clearValue;

    public LargeLongArray(int initialOuterCapacity, int innerCapacityExponent, Long clearValue) {
        super(initialOuterCapacity, long[][]::new, a -> a.length, innerCapacityExponent);

        this.clearValue = clearValue;
    }

    @Override
    public void clear() {

        super.clear();

        clearArrays(clearValue, arrayClearer);
    }

    @Override
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    public long get(long index) {

        Objects.checkIndex(index, getLimit());

        return getOuterArray()[getOuterIndex(index)][getInnerElementIndex(index)];
    }

    public void add(long value) {

        final long[] array = checkCapacity();

        final long index = getAndIncrementLimit();

        array[getInnerElementIndex(index)] = value;
    }

    public void set(long index, long value) {

        Checks.isIndexNotOutOfBounds(index);

        final int outerIndex = ensureCapacityAndLimit(index);

        getOuterArray()[outerIndex][getInnerElementIndex(index)] = value;
    }

    @Override
    protected long[][] copyOuterArray(long[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    protected int getOuterArrayLength(long[][] outerArray) {

        return outerArray.length;
    }

    @Override
    protected long[] getInnerArray(long[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    protected long[] abstractCreateAndSetInnerArray(long[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new long[innerArrayLength];
    }

    @Override
    protected long[] checkCapacity() {

        return checkCapacity(clearValue, arrayClearer);
    }

    @Override
    int ensureCapacityAndLimit(long index) {

        return ensureCapacityAndLimit(index, clearValue, arrayClearer);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder();

        toString(sb, StringBuilder::append);

        return sb.toString();
    }

    public void toString(StringBuilder sb, ObjLongConsumer<StringBuilder> appender) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(appender);

        ByIndex.toString(this, 0L, getLimit(), sb, null, appender, null, null, (instance, index, b, a) -> a.accept(b, instance.get(index)));
    }

    public String toHexString() {

        final StringBuilder sb = new StringBuilder();

        toHexString(sb);

        return sb.toString();
    }

    public void toHexString(StringBuilder sb) {

        Objects.requireNonNull(sb);

        toString(sb, (b, element) -> StringBuilders.hexString(b, element, true, 16));
    }
}
