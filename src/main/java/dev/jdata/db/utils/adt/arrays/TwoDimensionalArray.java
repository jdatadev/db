package dev.jdata.db.utils.adt.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.IForEachSequenceElement;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public final class TwoDimensionalArray<T> extends BaseAnyLargeArray<T[][], T[]> implements IMutableElements, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_TWO_DIMENSIONAL_ARRAY;

    private final int innerInitialCapacity;
    private final IntFunction<T[]> createInnerArray;

    private int numElements;

    public TwoDimensionalArray(int outerInitialCapacity, IntFunction<T[][]> createOuterArray, int innerInitialCapacity, IntFunction<T[]> createInnerArray) {
        super(outerInitialCapacity, false, createOuterArray, true);

        Checks.isInitialCapacity(innerInitialCapacity);
        Objects.requireNonNull(createInnerArray);

        if (DEBUG) {

            enter(b -> b.add("outerInitialCapacity", outerInitialCapacity).add("createOuterArray", createOuterArray).add("innerInitialCapacity", innerInitialCapacity)
                    .add("createInnerArray", createInnerArray));
        }

        this.innerInitialCapacity = innerInitialCapacity;
        this.createInnerArray = createInnerArray;

        this.numElements = 0;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long getNumElements() {
        return numElements;
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearArray();

        this.numElements = 0;

        if (DEBUG) {

            exit();
        }
    }

    public int getNumOuterElements() {

        return getNumOuterUtilizedEntries();
    }

    public T findExactlyOne(int index, Predicate<T> predicate) {

        checkOuterIndex(index);
        Objects.requireNonNull(predicate);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("predicate", predicate));
        }

        final T result = findExactlyOne(index, predicate, (e, p) -> p.test(e));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public <P> T findExactlyOne(int index, P parameter, BiPredicate<T, P> predicate) {

        checkOuterIndex(index);
        Objects.requireNonNull(predicate);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("parameter", parameter).add("predicate", predicate));
        }

        final T[] innerArray = getOuterArray()[index];

        final int numInnerElements = getNumInnerElements(index);

        T result = null;

        for (int i = 0; i < numInnerElements; ++ i) {

            final T element = innerArray[i];

            if (predicate.test(element, parameter)) {

                if (result != null) {

                    throw new IllegalStateException();
                }

                result = element;
            }
        }

        if (result == null) {

            throw new NoSuchElementException();
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public void forEachElement(IForEachSequenceElement<T> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("forEach", forEach));
        }

        final int numOuter = getNumOuterUtilizedEntries();
        final T[][] outer = getOuterArray();

        for (int i = 0; i < numOuter; ++ i) {

            final T[] innerArray = outer[i];

            if (innerArray != null) {

                final int numInnerElements = getNumInnerElements(i);

                for (int j = 0; j < numInnerElements; ++ j) {

                    forEach.each(i, innerArray[j]);
                }
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    public void add(int index, T value) {

        Checks.checkIndex(index, getNumOuterElements() + 1);
        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        addWithOuterExpand(index, value);

        if (DEBUG) {

            exit();
        }
    }

    public void addWithOuterExpand(int index, T value) {

        Checks.isIndex(index);
        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("value", value));
        }

        final int capacityMultiplicator = 2;

        T[][] outerArray = getOuterArray();

        if (outerArray == null) {

            final int numOuter = index + 1;

            outerArray = allocateInitialOuterArrayAndInnerArrayElements(numOuter);

            for (int i = 0; i < numOuter; ++ i) {

                outerArray[i] = createInnerArray.apply(innerInitialCapacity);

                clearNumInnerElementsIfRequired(i);
            }
        }

        if (index >= outerArray.length) {

            final int newCapacity = index * capacityMultiplicator;

            if (DEBUG) {

                debug("expanding outer array capacity=" + outerArray.length + " newCapacity=" + newCapacity);
            }

            outerArray = reallocateOuterArrayAndInnerArrayNumElements(newCapacity);
        }

        T[] innerArray = outerArray[index];

        if (innerArray == null) {

            if (DEBUG) {

                debug("creating new inner array index=" + index);
            }

            innerArray = createInnerArray.apply(innerInitialCapacity);

            innerArray[0] = value;

            outerArray[index] = innerArray;

            setNumInnerElements(index, 1);
        }
        else {
            if (DEBUG) {

                debug("adding to existing inner array index=" + index);
            }

            final int innerNumElements = getNumInnerElements(index);

            if (innerNumElements == innerArray.length) {

                if (DEBUG) {

                    debug("expanding inner array index=" + index);
                }

                innerArray = Arrays.copyOf(innerArray, innerArray.length * capacityMultiplicator);

                outerArray[index] = innerArray;
            }

            if (DEBUG) {

                debug("added to existing inner array index=" + index + " innerNumElements=" + innerNumElements);
            }

            innerArray[innerNumElements] = value;

            incrementNumInnerElements(index);
        }

        final int numOuterElements = getNumOuterElements();

        if (index >= numOuterElements) {

            if (DEBUG) {

                debug("updating num outer elements index=" + index + " numOuterElements=" + numOuterElements);
            }

            setNumOuterUtilizedEntries(index + 1, outerArray.length);
        }

        ++ numElements;

        if (DEBUG) {

            exit();
        }
    }

    public List<T> toUnmodifiableList(int index) {

        checkOuterIndex(index);

        if (DEBUG) {

            enter(b -> b.add("index", index));
        }

        final int numInnerElements = getNumInnerElements(index);

        final List<T> result;

        if (numInnerElements == 0) {

            result = Collections.emptyList();
        }
        else {
            final ArrayList<T> list = new ArrayList<>(numInnerElements);

            final T[] innerArray = getOuterArray()[index];

            for (int i = 0; i < numInnerElements; ++ i) {

                list.add(innerArray[i]);
            }

            result = Collections.unmodifiableList(list);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected void clearInnerArray(T[] innerArray, long startIndex, long numElements) {

        throw new UnsupportedOperationException();
    }

    @Override
    protected int getOuterArrayCapacity() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected long getInnerElementCapacity() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected int getOuterArrayLength(T[][] outerArray) {

        throw new UnsupportedOperationException();
    }

    @Override
    protected T[][] copyOuterArray(T[][] outerArray, int newCapacity) {

        Objects.requireNonNull(outerArray);
        Checks.isGreaterThan(newCapacity, outerArray.length);

        return Arrays.copyOf(outerArray, newCapacity);
    }

    @Override
    protected T[] getInnerArray(T[][] outerArray, int index) {

        return outerArray[index];
    }

    private void checkOuterIndex(int index) {

        Checks.checkIndex(index, getNumOuterElements());
    }
}
