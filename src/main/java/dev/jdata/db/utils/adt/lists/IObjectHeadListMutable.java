package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.mutability.IMutableMarker;

@Deprecated // necessary?
interface IObjectHeadListMutable<T> extends IMutableMarker, IListView<T>, IObjectHeadListMutators<T> {

}
