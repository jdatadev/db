package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IIntOrderedOnlyElementsBuilder;

interface IIntListBuilder<T extends IIntList, U extends IIntList & IHeapContainsMarker> extends IIntOrderedOnlyElementsBuilder<T, U> {

}
