package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.maps.IMutableMapType;
import dev.jdata.db.utils.checks.Checks;

public abstract class IntCapacityOnlyElementsBuilder<

                IMMUTABLE extends IElements & IOnlyElementsView,
                HEAP_IMMUTABLE extends IElements & IOnlyElementsView & IHeapContainsMarker,
                MAKE_ELEMENTS_FROM,
                MUTABLE extends BaseADTElements<?, MAKE_ELEMENTS_FROM, ?> & IMutableElements & IOnlyElementsMutable>

        extends OnlyElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE, MUTABLE> {

    protected final <T extends IOnlyElementsView & IMutableMapType> void checkWithMakeElementsFromParameters(AllocationType allocationType, T makeElementsFrom, int numElements) {

        Checks.areEqual(allocationType, getAllocationType());
        Objects.requireNonNull(makeElementsFrom);
        Checks.isIntNumElements(numElements);
        Checks.areEqual(makeElementsFrom.getNumElements(), numElements);
    }

    protected final <T extends IOnlyElementsView & IMutableMapType> void checkWithHeapMakeElementsFromParameters(AllocationType allocationType, T makeElementsFrom,
            int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, numElements);
    }

    protected final void checkWithMakeElementsFromParameters(AllocationType allocationType, Object makeElementsFrom, int makeElementsFromNumElements, int numElements) {

        Checks.areEqual(allocationType, getAllocationType());
        Objects.requireNonNull(makeElementsFrom);
        Checks.checkIntNumElements(numElements, makeElementsFromNumElements);
    }

    protected final void checkWithHeapMakeElementsFromParameters(AllocationType allocationType, Object makeElementsFrom, int makeElementsFromNumElements, int numElements) {

        checkWithMakeElementsFromParameters(allocationType, makeElementsFrom, makeElementsFromNumElements, numElements);
    }

    protected abstract IMMUTABLE withMakeElementsFrom(AllocationType allocationType, MAKE_ELEMENTS_FROM makeElementsFrom, int numElements);
    protected abstract HEAP_IMMUTABLE withHeapMakeElementsFrom(AllocationType allocationType, MAKE_ELEMENTS_FROM makeElementsFrom, int numElements);

    @Deprecated // allocates closure
    protected <P> IntCapacityOnlyElementsBuilder(AllocationType allocationType, long minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MUTABLE, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, builderMutableAllocator, (t, c, a) -> a.allocate(t, BaseADTElements.intCapacity(c), parameter));
    }

    @Override
    protected final IMMUTABLE build(AllocationType allocationType, MUTABLE mutable) {

        checkBuildParameters(allocationType, mutable);

        return mutable.makeFromElementsAndRecreateOrDispose(allocationType, this, (a, c, e, n, i) -> i.withMakeElementsFrom(a, e, BaseADTElements.intNumElements(n)));
    }

    @Override
    protected final HEAP_IMMUTABLE heapBuild(AllocationType allocationType, MUTABLE mutable) {

        checkBuildParameters(allocationType, mutable);

        return mutable.makeFromElements(allocationType, this, (a, c, e, n, i) -> i.withHeapMakeElementsFrom(a, e, BaseADTElements.intNumElements(n)));
    }
}
