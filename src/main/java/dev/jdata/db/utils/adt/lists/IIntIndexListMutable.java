package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.byindex.IIntByIndexMutators;
import dev.jdata.db.utils.adt.elements.IIntOrderedAddTailElementsMutators;
import dev.jdata.db.utils.adt.mutability.IMutableMarker;

interface IIntIndexListMutable extends IMutableMarker, IIntByIndexMutators, IIntOrderedAddTailElementsMutators, IIntTailListRemovalMutators, IIntIndexListMutators {

}
