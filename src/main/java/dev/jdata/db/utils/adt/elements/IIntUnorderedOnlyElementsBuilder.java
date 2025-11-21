package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

public interface IIntUnorderedOnlyElementsBuilder<

                T extends IOnlyElementsView & IIntUnorderedElementsView,
                U extends IOnlyElementsView & IIntUnorderedElementsView & IHeapContainsMarker>

        extends IIntOnlyElementsBuilder<T, U>, IIntUnorderedElementsMutators {

}
