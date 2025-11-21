package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.ILongByIndexMutators;
import dev.jdata.db.utils.adt.elements.ILongOrderedAddTailElementsMutators;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface ILongIndexListMutable extends IMutableMarker, ILongByIndexMutators, ILongOrderedAddTailElementsMutators, ILongTailListRemovalMutators, ILongIndexListMutators {

}
