package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IIntOrderedOnlyElementsBuilder<

                T extends IOnlyElementsView & IIntOrderedElementsView,
                U extends IOnlyElementsView & IIntOrderedElementsView & IHeapContainsMarker>

        extends IIntOnlyElementsBuilder<T, U>, IIntOrderedAddTailElementsMutators {

}
