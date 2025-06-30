package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongElementsMutators;
import dev.jdata.db.utils.adt.elements.ILongOrderedTailElementsMutators;
import dev.jdata.db.utils.adt.elements.IMutableElements;

public interface IMutableLongIndexList extends IMutableIntegerList, ILongIndexListCommon, IMutableElements, ILongElementsMutators, ILongOrderedTailElementsMutators {

}
