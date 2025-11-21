package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IObjectOrderedTailElementsMutators;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public interface IObjectOrderedElementsBuilder<T, U extends IElements, V extends IElements & IOnlyElementsView & IHeapContainsMarker>

        extends IOnlyElementsBuilder<U, V>, IObjectOrderedTailElementsMutators<T> {

}
