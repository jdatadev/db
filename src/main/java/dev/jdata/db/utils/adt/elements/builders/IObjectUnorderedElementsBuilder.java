package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public interface IObjectUnorderedElementsBuilder<T, U extends IElements, V extends IElements & IOnlyElementsView & IHeapContainsMarker>

        extends IOnlyElementsBuilder<U, V>, IObjectUnorderedAddable<T> {

}
