package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IObjectOrderedOnlyElementsBuilder<

                T,
                U extends IOnlyElementsView & IObjectOrderedElementsView<T>,
                V extends IOnlyElementsView & IObjectOrderedElementsView<T> & IHeapContainsMarker>

        extends IObjectOnlyElementsBuilder<T, U, V>, IObjectOrderedAddTailElementsMutators<T> {

}
