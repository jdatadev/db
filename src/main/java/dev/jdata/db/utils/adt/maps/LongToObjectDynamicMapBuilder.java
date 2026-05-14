package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ElementsExceptions;

abstract class LongToObjectDynamicMapBuilder<

                VALUE,
                IMMUTABLE extends ILongToObjectDynamicMap<VALUE>,
                HEAP_IMMUTABLE extends ILongToObjectDynamicMap<VALUE> & IHeapContainsMarker,
                MAKE_ELEMENTS_FROM extends MutableLongToObjectMaxDistanceNonBucketMap<VALUE, MAKE_ELEMENTS_FROM>>

        extends MapBuilder<IMMUTABLE, HEAP_IMMUTABLE, MAKE_ELEMENTS_FROM>
        implements IBaseLongToObjectDynamicMapBuilder<VALUE, IMMUTABLE, HEAP_IMMUTABLE> {

    <P> LongToObjectDynamicMapBuilder(AllocationType allocationType, int minimumCapacity, P parameter,
            IIntCapacityBuilderMutableAllocator<MAKE_ELEMENTS_FROM, P> builderMutableAllocator) {
        super(allocationType, minimumCapacity, parameter, builderMutableAllocator);
    }

    @Override
    public final void add(long key, VALUE value) {

        Objects.requireNonNull(value);

        final MAKE_ELEMENTS_FROM mutable = getMutable();

        if (mutable.containsKey(key)) {

            throw ElementsExceptions.alreadyAddedException();
        }

        mutable.put(key, value);
    }
}
