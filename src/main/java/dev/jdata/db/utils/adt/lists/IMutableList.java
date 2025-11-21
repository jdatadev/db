package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IMutableObjectOrderedOnlyElements;

interface IMutableList<T> extends IMutableObjectOrderedOnlyElements<T>, IMutableListType, IObjectListCommon<T>, IObjectListMutators<T> {

}
