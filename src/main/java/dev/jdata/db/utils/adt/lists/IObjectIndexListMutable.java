package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.IObjectByIndexMutators;

interface IObjectIndexListMutable<T> extends IObjectListMutable<T>, IObjectByIndexMutators<T>, IObjectIndexListMutators<T> {

}
