package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ILongOrderedOnlyElementsBuilder;

interface ILongListBuilder<T extends ILongList, U extends ILongList & IHeapContainsMarker> extends ILongOrderedOnlyElementsBuilder<T, U> {

}
