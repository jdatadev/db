package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.IObjectByIndexMutators;
import dev.jdata.db.utils.adt.elements.IObjectOrderedAddTailElementsMutators;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface IObjectIndexListMutable<T> extends IMutableMarker, IObjectByIndexMutators<T>, IObjectOrderedAddTailElementsMutators<T>, IObjectTailListRemovalMutators<T> {

}
