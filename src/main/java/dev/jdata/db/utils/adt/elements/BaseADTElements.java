package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.mutability.IImmutable;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseADTElements<T, U, V> extends Allocatable {

    @FunctionalInterface
    public interface IMakeFromElementsFunction<T, U, P, R> {

        R apply(AllocationType allocationType, IntFunction<T> createElementsArray, U from, long numElements, P parameter);
    }

    protected static final int DEFAULT_INITIAL_CAPACITY = ADTConstants.DEFAULT_INITIAL_CAPACITY;

    protected static final int DEFAULT_CAPACITY_MULTIPLICATOR = ADTConstants.DEFAULT_CAPACITY_MULTIPLICATOR;

    protected static void checkHeapCopyArrayParameters(AllocationType allocationType, Object valuesArray, int numElements) {

        AllocationType.checkIsHeap(allocationType);
        Checks.isArray(valuesArray);
        Checks.checkArrayNumElements(valuesArray, numElements);
    }

    protected static void checkHeapCopyArrayParameters(AllocationType allocationType, Object valuesArray, int startIndex, int numElements) {

        AllocationType.checkIsHeap(allocationType);
        Checks.checkArrayFromIndexSize(valuesArray, startIndex, numElements);
    }

    protected static void checkHeapCopyImmutableParameters(AllocationType allocationType, IImmutable immutable) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(immutable);
    }

    protected static void checkHeapCopyMutableParameters(AllocationType allocationType, IMutable mutable) {

        AllocationType.checkIsHeap(allocationType);
        Objects.requireNonNull(mutable);
    }

    protected static void checkHeapWithArrayParameters(AllocationType allocationType, Object valuesArray, int numElements) {

        checkWithArrayParameters(allocationType, valuesArray, numElements, AllocationType::checkIsHeap);
    }

    protected static void checkCachedWithArrayParameters(AllocationType allocationType, Object valuesArray, int numElements) {

        checkWithArrayParameters(allocationType, valuesArray, numElements, AllocationType::checkIsCached);
    }

    private static void checkWithArrayParameters(AllocationType allocationType, Object valuesArray, int numElements, Consumer<AllocationType> checkAllocationType) {

        checkAllocationType.accept(allocationType);
        Checks.isArray(valuesArray);
        Checks.isIntNumElements(numElements);
    }

    protected static <T extends IOnlyElementsView> void checkIntCopyValuesParameters(T elements, long startIndex, long numElements) {

        checkIntCopyValuesParameters(elements, intNumElements(elements), startIndex, numElements);
    }

    protected static <T> void checkIntCopyValuesParameters(T elements, long elementsLength, long startIndex, long numElements) {

        Objects.requireNonNull(elements);
        Checks.isIntLengthAboveOrAtZero(elementsLength);
        Checks.isIntIndex(startIndex);
        Checks.isAboveZero(startIndex);
        Checks.isIntNumElements(numElements);
        Checks.checkFromIndexSize(startIndex, numElements, elementsLength);
    }

    protected static <T> void checkIntOrLongCopyValuesParameters(T elements, long elementsLength, long startIndex, long numElements) {

        checkLongCopyValuesParameters(elements, elementsLength, startIndex, numElements);
    }

    protected static <T> void checkLongCopyValuesParameters(T elements, long elementsLength, long startIndex, long numElements) {

        Objects.requireNonNull(elements);
        Checks.isLongLengthAboveOrAtZero(elementsLength);
        Checks.isLongIndex(startIndex);
        Checks.isAboveZero(startIndex);
        Checks.isLongNumElements(numElements);
        Checks.checkFromIndexSize(startIndex, numElements, elementsLength);
    }

    protected static <T extends IOnlyElementsView> void checkIntIntitializeWithValuesParameters(T elements, long numElements) {

        checkIntIntitializeWithValuesParameters(elements, intNumElements(elements), numElements);
    }

    protected static <T> void checkIntIntitializeWithValuesParameters(T elements, long elementsLength, long numElements) {

        Objects.requireNonNull(elements);
        Checks.isIntLengthAboveZero(elementsLength);
        Checks.checkIntNumElements(numElements, elementsLength);
    }

    protected static <T> void checkIntOrLongIntitializeWithValuesParameters(T elements, long elementsLength, long numElements) {

        checkLongIntitializeWithValuesParameters(elements, elementsLength, numElements);
    }

    protected static <T> void checkLongIntitializeWithValuesParameters(T elements, long elementsLength, long numElements) {

        Objects.requireNonNull(elements);
        Checks.isLongLengthAboveZero(elementsLength);
        Checks.checkLongNumElements(numElements, elementsLength);
    }

    protected static int intIndex(long index) {

        return IByIndexView.intIndex(index);
    }

    protected static int intNumElements(IOnlyElementsView onlyElementsView) {

        return IOnlyElementsView.intNumElements(onlyElementsView);
    }

    protected static int intNumElements(long numElements) {

        return IOnlyElementsView.intNumElementsRenamed(numElements);
    }

    protected static int intMinimumCapacity(long minimumCapacity) {

        return ICapacity.intCapacityRenamed(minimumCapacity);
    }

    protected static int intCapacity(long minimumCapacity) {

        return ICapacity.intCapacityRenamed(minimumCapacity);
    }

    protected abstract <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, U, P, R> makeFromElements);
    protected abstract void recreateElements();
    protected abstract void resetToNull();

    protected abstract V copyValues(V values, long startIndex, long numElements);
    protected abstract void initializeWithValues(V values, long numElements);

    protected BaseADTElements(AllocationType allocationType) {
        super(allocationType);
    }

    public final <P, R> R makeFromElementsAndRecreateOrDispose(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, U, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        checkIsAllocatedRenamed();

        final R result = makeFromElements(allocationType, parameter, makeFromElements);

        if (getAllocationType().isRecreatable()) {

            recreateElements();
        }
        else {
            resetToNullAndDispose();
        }

        return result;
    }

    private <P, R> R makeFromElementsAndDispose(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, U, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        final R result = makeFromElements(allocationType, parameter, makeFromElements);

        resetToNullAndDispose();

        return result;
    }

    private <P, R> R makeFromElementsAndRecreate(AllocationType allocationType, P parameter, IMakeFromElementsFunction<T, U, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        final R result = makeFromElements(allocationType, parameter, makeFromElements);

        recreateElements();

        return result;
    }

    private void initialize(V values, long valuesLength, long numElements) {

        Objects.requireNonNull(values);
        Checks.checkLongNumElements(numElements, valuesLength);

        initializeWithValues(values, numElements);
    }

    protected final void initialize(V values, long valuesLength, long startIndex, long numElements) {

        Objects.requireNonNull(values);
        Checks.checkFromIndexSize(startIndex, numElements, valuesLength);

        if (startIndex == 0L) {

            initializeWithValues(values, numElements);
        }
        else {
            initializeWithValues(copyValues(values, startIndex, startIndex + numElements), numElements);
        }
    }

    private void resetToNullAndDispose() {

        setDisposed();

        resetToNull();
    }
}
