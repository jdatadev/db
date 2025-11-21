package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.builders.ContainsBuilderAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.checks.Checks;

abstract class ElementsAllocator<

                IMMUTABLE extends IElements,
                HEAP_IMMUTABLE extends IElements & IHeapContainsMarker,
                ELEMENTS_ARRAY,
                INTERFACE_MUTABLE extends IMutableElements,
                CLASS_MUTABLE extends BaseADTElements<?, ELEMENTS_ARRAY, ?> & IMutableElements,
                BUILDER extends IElementsBuilder<IMMUTABLE, HEAP_IMMUTABLE>>

        extends ContainsBuilderAllocator<IMMUTABLE, HEAP_IMMUTABLE, CLASS_MUTABLE, BUILDER>
        implements IElementsArrayElementsAllocator<IMMUTABLE, INTERFACE_MUTABLE, BUILDER, ELEMENTS_ARRAY>, IAllocators {

    protected static final int DEFAULT_INITIAL_CAPACITY = ADTConstants.DEFAULT_INITIAL_CAPACITY;

    protected static final int DEFAULT_CAPACITY_MULTIPLICATOR = ADTConstants.DEFAULT_CAPACITY_MULTIPLICATOR;

    private final IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ELEMENTS_ARRAY> elementsAllocators;

    protected abstract long getElementsArrayLength(ELEMENTS_ARRAY elementsArray);

    private final AllocationType allocationType;

    protected ElementsAllocator(AllocationType allocationType, IElementsAllocators<IMMUTABLE, CLASS_MUTABLE, BUILDER, ELEMENTS_ARRAY> elementsAllocators) {

        this.allocationType = Objects.requireNonNull(allocationType);
        this.elementsAllocators = Objects.requireNonNull(elementsAllocators);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("elementsAllocators", RefType.INSTANTIATED, BaseCachedElementsAllocators.class, elementsAllocators);
    }

    @Override
    public final BUILDER createBuilder(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return elementsAllocators.allocateBuilder(minimumCapacity);
    }

    @Override
    public final void freeBuilder(BUILDER builder) {

        Objects.requireNonNull(builder);

        elementsAllocators.freeBuilder(builder);
    }

    @Override
    public final IMMUTABLE allocateImmutableFrom(ELEMENTS_ARRAY values, long startIndex, long numElements) {

        Objects.requireNonNull(values);
        Checks.checkLongIndexAndNumElements(startIndex, numElements);

        return elementsAllocators.allocateImmutableFrom(values, getElementsArrayLength(values), startIndex, numElements);
     }

    @Override
    public final void freeImmutable(IMMUTABLE immutable) {

        Objects.requireNonNull(immutable);

        elementsAllocators.freeImmutable(immutable);
    }

    @Override
    public final IMMUTABLE copyToImmutable(INTERFACE_MUTABLE mutable) {

        Objects.requireNonNull(mutable);

        @SuppressWarnings("unchecked")
        final CLASS_MUTABLE classMutable = (CLASS_MUTABLE)mutable;

        return makeFromClassMutableToImmutableAndRecreateOrDispose(classMutable);
    }

    @Override
    public final CLASS_MUTABLE allocateMutable(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return elementsAllocators.allocateMutable(minimumCapacity);
    }

    @Override
    protected final void freeMutable(CLASS_MUTABLE mutable) {

        Objects.requireNonNull(mutable);

        elementsAllocators.freeMutable(mutable);
    }

    final IMMUTABLE emptyImmutable() {

        return elementsAllocators.emptyImmutable();
    }

    final IMMUTABLE makeFromClassMutableToImmutableAndRecreateOrDispose(CLASS_MUTABLE mutable) {

        Objects.requireNonNull(mutable);

        return mutable.makeFromElementsAndRecreateOrDispose(allocationType, this, (a, c, e, n, i) -> i.allocateImmutableFrom(e, n));
    }
}
