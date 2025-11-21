package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface ILongUnorderedOnlyElementsBuilder<

                T extends IOnlyElementsView & ILongUnorderedElementsView,
                U extends IOnlyElementsView & ILongUnorderedElementsView & IHeapContainsMarker>

        extends ILongOnlyElementsBuilder<T, U>, ILongUnorderedElementsMutators {

}
