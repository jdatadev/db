package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

abstract class MutableLongIndexListAllocator<T extends IMutableLongIndexList, U extends MutableLongIndexList>

        extends BaseMutableIndexListAllocator<T, U, long[], ILongIterableElementsView>
        implements IMutableLongIndexListAllocator<T> {

    MutableLongIndexListAllocator() {
        super(long[]::new);
    }
}
