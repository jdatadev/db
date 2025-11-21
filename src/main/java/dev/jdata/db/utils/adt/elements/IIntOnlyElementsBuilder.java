package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;

interface IIntOnlyElementsBuilder<T extends IOnlyElementsView & IIntElementsView, U extends IOnlyElementsView & IIntElementsView & IHeapContainsMarker>

        extends IOnlyElementsBuilder<T, U>, IIntElementsMarker {

}
