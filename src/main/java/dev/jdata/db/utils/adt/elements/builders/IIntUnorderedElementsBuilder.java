package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;
import dev.jdata.db.utils.adt.elements.IIntUnorderedTailElementsMutators;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public interface IIntUnorderedElementsBuilder<T extends IElements, U extends IElements & IOnlyElementsView & IHeapContainsMarker>

        extends IOnlyElementsBuilder<T, U>, IIntUnorderedTailElementsMutators<T> {

}
