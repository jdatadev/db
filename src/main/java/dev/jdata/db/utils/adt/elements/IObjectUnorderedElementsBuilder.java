package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IObjectUnorderedElementsBuilder<

                T,
                U extends IOnlyElementsView & IObjectUnorderedElementsView<T>,
                V extends IOnlyElementsView & IObjectUnorderedElementsView<T> & IHeapContainsMarker>

        extends IObjectOnlyElementsBuilder<T, U, V>, IObjectUnorderedAddable<T> {

}
