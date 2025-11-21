package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIntOrderedOnlyElementsAllocator;

interface IBaseIntIndexListAllocator<T extends IBaseIntIndexList, U extends IBaseMutableIntIndexList, V extends IBaseIntIndexListBuilder<T, ?>>

        extends IIntOrderedOnlyElementsAllocator<T, U, V> {

}
