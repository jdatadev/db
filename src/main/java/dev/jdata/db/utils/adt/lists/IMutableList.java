package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IMutableObjectOrderedOnlyElements;

public interface IMutableList<T>

        extends IMutableObjectOrderedOnlyElements<T>, IObjectListCommon<T>, IBaseListMutable, IObjectListMutators<T>, IMutableListMarker {

}
