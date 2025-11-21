package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.contains.builders.IContainsBuildable;

interface IElementsBuildable<T extends IElementsView, U extends IElementsView & IHeapContainsMarker> extends IContainsBuildable<T, U> {

}
