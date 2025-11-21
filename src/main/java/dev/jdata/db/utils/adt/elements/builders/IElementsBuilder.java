package dev.jdata.db.utils.adt.elements.builders;

import dev.jdata.db.utils.adt.contains.builders.IContainsBuilder;
import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IElements;

public interface IElementsBuilder<T extends IElements, U extends IElements & IHeapContainsMarker> extends IContainsBuilder<T, U>, IElementsBuildable<T, U> {

}
