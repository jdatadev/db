package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.builders.IObjectOrderedElementsBuilder;

public interface IObjectListBuilder<T, U extends IList<T>, V extends IList<T> & IHeapContainsMarker> extends IObjectOrderedElementsBuilder<T, U, V> {

}
