package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface ILongOnlyElementsBuilder<T extends IOnlyElementsView & ILongElementsView, U extends IOnlyElementsView & ILongElementsView & IHeapContainsMarker>

        extends IOnlyElementsBuilder<T, U>, ILongElementsMarker {

}
