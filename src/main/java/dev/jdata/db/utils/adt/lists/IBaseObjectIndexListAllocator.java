package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectOrderedOnlyElementsAllocator;

interface IBaseObjectIndexListAllocator<

        T,
        IMMUTABLE extends IBaseObjectIndexList<T>,
        MUTABLE extends IBaseMutableObjectIndexList<T>,
        BUILDER extends IBaseObjectIndexListBuilder<T, IMMUTABLE, ?>>

        extends IObjectOrderedOnlyElementsAllocator<T, IMMUTABLE, MUTABLE, BUILDER> {

}
