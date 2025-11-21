package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

@Deprecated // necessary?
public interface IObjectOnlyElementsBuilder<T, U extends IOnlyElementsView & IObjectElementsView<T>, V extends IOnlyElementsView & IObjectElementsView<T> & IHeapContainsMarker>

        extends IOnlyElementsBuilder<U, V>, IObjectElementsMarker {

}
