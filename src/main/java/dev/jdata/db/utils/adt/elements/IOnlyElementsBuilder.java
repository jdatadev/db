package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

@Deprecated // what about arrays?
public interface IOnlyElementsBuilder<T extends IOnlyElementsView, U extends IOnlyElementsView & IHeapContainsMarker> extends IElementsBuilder<T, U> {

}
