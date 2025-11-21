package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

abstract class MutableLongSetAllocator<T extends IMutableLongSet, U extends BaseIntCapacityExponentSet<?, ?> & IMutableLongSet>

        extends BaseIntCapacityMutableSetAllocator<T, U, long[], ILongIterableElementsView>
        implements IMutableLongSetAllocator<T> {

    MutableLongSetAllocator() {
        super(long[]::new);
    }
}
