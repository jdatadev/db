package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.IContainsBuildable;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;

interface IElementsBuildable<T extends IElements, U extends IElements & IHeapContainsMarker> extends IContainsBuildable<T, U> {

}
