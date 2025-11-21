package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.checks.Checks;

public abstract class IntCapacityOnlyElementsBuilder<

                IMMUTABLE extends IElements & IOnlyElementsView,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                MUTABLE extends BaseADTElements<?, ELEMENTS_ARRAY, ?> & IMutableElements & IOnlyElementsMutable>

        extends OnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE, MUTABLE> {

    protected final void checkWithArrayParameters(AllocationType allocationType, Object elementsArray, int elementsArrayLength, int numElements) {

        Checks.areEqual(allocationType, getAllocationType());
        Objects.requireNonNull(elementsArray);
        Checks.checkIntNumElements(numElements, elementsArrayLength);
    }

    protected final void checkWithHeapArrayParameters(AllocationType allocationType, Object elementsArray, int elementsArrayLength, int numElements) {

        checkWithArrayParameters(allocationType, elementsArray, elementsArrayLength, numElements);
    }

    protected abstract IMMUTABLE withArray(AllocationType allocationType, ELEMENTS_ARRAY elementsArray, int numElements);
    protected abstract HEAP_IMMUTABLE withHeapArray(AllocationType allocationType, ELEMENTS_ARRAY elementsArray, int numElements);

    @Deprecated // allocates closure
    protected <P> IntCapacityOnlyElementsBuilder(AllocationType allocationType, long minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MUTABLE, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, builderMutableAllocator, (t, c, a) -> a.allocate(t, BaseADTElements.intCapacity(c), parameter));
    }

    @Override
    protected final IMMUTABLE build(AllocationType allocationType, MUTABLE mutable) {

        checkBuildParameters(allocationType, mutable);

        return mutable.makeFromElementsAndRecreateOrDispose(allocationType, this, (a, c, e, n, i) -> i.withArray(a, e, BaseADTElements.intNumElements(n)));
    }

    @Override
    protected final HEAP_IMMUTABLE heapBuild(AllocationType allocationType, MUTABLE mutable) {

        checkBuildParameters(allocationType, mutable);

        return mutable.makeFromElements(allocationType, this, (a, c, e, n, i) -> i.withHeapArray(a, e, BaseADTElements.intNumElements(n)));
    }
}
