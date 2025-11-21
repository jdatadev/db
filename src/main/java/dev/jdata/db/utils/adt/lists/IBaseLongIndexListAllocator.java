package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongOrderedOnlyElementsAllocator;

interface IBaseLongIndexListAllocator<T extends IBaseLongIndexList, U extends IBaseMutableLongIndexList, V extends IBaseLongIndexListBuilder<T, ?>>

        extends ILongOrderedOnlyElementsAllocator<T, U, V> {

}
