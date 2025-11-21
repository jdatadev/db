package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface ILongOrderedOnlyElementsBuilder<

                T extends IOnlyElementsView & ILongOrderedElementsView,
                U extends IOnlyElementsView & ILongOrderedElementsView & IHeapContainsMarker>

        extends ILongOnlyElementsBuilder<T, U>, ILongOrderedAddTailElementsMutators {

}
