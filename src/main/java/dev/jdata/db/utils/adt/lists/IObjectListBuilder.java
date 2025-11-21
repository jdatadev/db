package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectOrderedOnlyElementsBuilder;

interface IObjectListBuilder<T, U extends IList<T>, V extends IList<T> & IHeapContainsMarker> extends IObjectOrderedOnlyElementsBuilder <T, U, V> {

}
