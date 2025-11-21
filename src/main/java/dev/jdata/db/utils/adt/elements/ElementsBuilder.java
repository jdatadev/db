package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.builders.ContainsBuilder;
import dev.jdata.db.utils.checks.Checks;

abstract class ElementsBuilder<T extends IElements, U extends IElements & IHeapContainsMarker, V extends IMutableElements>

        extends ContainsBuilder<T, U, V>
        implements IElementsBuilder<T, U> {

    protected static void checkBuilderCreateParameters(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism) {

        AllocationType.checkIsAllocationMechanism(allocationType, expectedAllocationMechanism);
    }

    protected static void checkBuilderCreateParameters(AllocationType allocationType, AllocationMechanism expectedAllocationMechanism, int initialCapacity) {

        checkBuilderCreateParameters(allocationType, expectedAllocationMechanism);
        Checks.isIntInitialCapacityAtOrAboveZero(initialCapacity);
    }

    protected static final int DEFAULT_INITIAL_CAPACITY = ADTConstants.DEFAULT_INITIAL_CAPACITY;

    <P> ElementsBuilder(AllocationType allocationType, long minimumCapacity, P parameter, IBuilderMutableAllocator<V, P> allocator) {
        super(allocationType, minimumCapacity, parameter, allocator);
    }
}
