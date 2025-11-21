package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

abstract class MutableIntSetAllocator<T extends IMutableIntSet, U extends BaseIntCapacityExponentSet<?, ?> & IMutableIntSet>

        extends BaseIntCapacityMutableSetAllocator<T, U, int[], IIntIterableElementsView>
        implements IMutableIntSetAllocator<T> {

    MutableIntSetAllocator() {
        super(int[]::new);
    }
}
