package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.IObjectByIndexMutators;
import dev.jdata.db.utils.adt.elements.IObjectOrderedTailElementsMutators;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface IObjectIndexListMutable<T> extends IMutableMarker, IObjectByIndexMutators<T>, IObjectOrderedTailElementsMutators<T>, IObjectTailListRemovalMutators<T> {

}
