package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.builders.IContainsBuilder;

public interface IElementsBuilder<T extends IElementsView, U extends IElementsView & IHeapContainsMarker> extends IContainsBuilder<T, U>, IElementsBuildable<T, U> {

}
