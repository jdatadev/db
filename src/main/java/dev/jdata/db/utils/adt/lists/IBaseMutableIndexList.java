package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ICopyToImmutable;

interface IBaseMutableIndexList<T, U extends IBaseIndexList<T>, V extends IBaseIndexListAllocator<T, U, ?, ?>>

        extends IMutableList<T>, IObjectIndexListCommon<T>, IObjectIndexListMutable<T>, ICopyToImmutable<U, V> {

}
