package dev.jdata.db.utils.adt.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.ForEachSequenceElement;
import dev.jdata.db.utils.adt.MutableElements;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public final class TwoDimensionalArray<T> implements MutableElements, PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_TWO_DIMENSIONAL_ARRAY;

    private final int innerInitialCapacity;
    private final IntFunction<T[]> createInnerArray;

    private T[][] outerArray;
    private int[] innerArrayNumElements;

    private int numOuterElements;
    private int numElements;

    public TwoDimensionalArray(int outerInitialCapacity, IntFunction<T[][]> createOuterArray, int innerInitialCapacity, IntFunction<T[]> createInnerArray) {

        Checks.isInitialCapacity(outerInitialCapacity);
        Objects.requireNonNull(createOuterArray);
        Checks.isInitialCapacity(innerInitialCapacity);
        Objects.requireNonNull(createInnerArray);

        if (DEBUG) {

            enter(b -> b.add("outerInitialCapacity", outerInitialCapacity).add("createOuterArray", createOuterArray).add("innerInitialCapacity", innerInitialCapacity)
                    .add("createInnerArray", createInnerArray));
        }

        this.innerInitialCapacity = innerInitialCapacity;
        this.createInnerArray = createInnerArray;

        this.outerArray = createOuterArray.apply(outerInitialCapacity);
        this.innerArrayNumElements = new int[outerInitialCapacity];

        this.numOuterElements = 0;
        this.numElements = 0;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean isEmpty() {

        return numOuterElements == 0;
    }

    @Override
    public final long getNumElements() {
        return numElements;
    }

    @Override
    public void clear() {

        Arrays.fill(innerArrayNumElements, 0, numOuterElements, 0);

        this.numOuterElements = 0;
        this.numElements = 0;
    }

    public int getNumOuterElements() {
        return numOuterElements;
    }

    public T findExactlyOne(int index, Predicate<T> predicate) {

        checkIndex(index);
        Objects.requireNonNull(predicate);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("predicate", predicate));
        }

        final T[] innerArray = outerArray[index];

        final int numInnerElements = innerArrayNumElements[index];

        T found = null;

        for (int i = 0; i < numInnerElements; ++ i) {

            final T element = innerArray[i];

            if (predicate.test(element)) {

                if (found != null) {

                    throw new IllegalStateException();
                }

                found = element;
            }
        }

        if (found == null) {

            throw new NoSuchElementException();
        }

        if (DEBUG) {

            exit(found);
        }

        return found;
    }

    public void forEachElement(ForEachSequenceElement<T> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("forEach", forEach));
        }

        final int numOuter = numOuterElements;
        final T[][] outer = outerArray;

        for (int i = 0; i < numOuter; ++ i) {

            final T[] innerArray = outer[i];

            if (innerArray != null) {

                final int numInnerElements = innerArrayNumElements[i];

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

        Objects.checkIndex(index, numOuterElements + 1);
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

        if (index >= outerArray.length) {

            final int newCapacity = index * capacityMultiplicator;

            if (DEBUG) {

                debug("expanding outer array capacity=" + outerArray.length + " newCapacity=" + newCapacity);
            }

            this.outerArray = Arrays.copyOf(outerArray, newCapacity);
            this.innerArrayNumElements = Arrays.copyOf(innerArrayNumElements, newCapacity);
        }

        T[] innerArray = outerArray[index];

        if (innerArray == null) {

            if (DEBUG) {

                debug("creating new inner array index=" + index);
            }

            innerArray = createInnerArray.apply(innerInitialCapacity);

            innerArray[0] = value;

            outerArray[index] = innerArray;
            innerArrayNumElements[index] = 1;
        }
        else {
            if (DEBUG) {

                debug("adding to existing inner array index=" + index);
            }

            final int innerNumElements = innerArrayNumElements[index];

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

            ++ innerArrayNumElements[index];
        }

        if (index >= numOuterElements) {

            if (DEBUG) {

                debug("updating num outer elements index=" + index + " numOuterElements=" + numOuterElements);
            }

            this.numOuterElements = index + 1;
        }

        ++ numElements;

        if (DEBUG) {

            exit();
        }
    }

    public List<T> toUnmodifiableList(int index) {

        checkIndex(index);

        if (DEBUG) {

            enter(b -> b.add("index", index));
        }

        final int numInnerElements = innerArrayNumElements[index];

        final List<T> result;

        if (numInnerElements == 0) {

            result = Collections.emptyList();
        }
        else {
            final ArrayList<T> list = new ArrayList<>(numInnerElements);

            final T[] innerArray = outerArray[index];

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

    private void checkIndex(int index) {

        Objects.checkIndex(index, numOuterElements);
    }
}
