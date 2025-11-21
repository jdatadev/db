package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

public interface IMultiHeadNodeListMutable<T> extends IMutableMarker, IMultiHeadNodeListMutators<T>, IClearable {

}
