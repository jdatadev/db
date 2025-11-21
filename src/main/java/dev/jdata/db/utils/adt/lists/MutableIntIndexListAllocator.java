package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;

abstract class MutableIntIndexListAllocator<T extends IMutableIntIndexList, U extends MutableIntIndexList>

        extends BaseMutableIndexListAllocator<T, U, int[], IIntIterableElementsView>
        implements IMutableIntIndexListAllocator<T> {

    MutableIntIndexListAllocator() {
        super(int[]::new);
    }
}
