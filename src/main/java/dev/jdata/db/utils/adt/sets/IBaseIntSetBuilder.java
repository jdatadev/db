package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.builders.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.builders.IIntUnorderedElementsBuilder;

interface IBaseIntSetBuilder<T extends IBaseIntSet, U extends IBaseIntSet & IHeapContainsMarker> extends IIntUnorderedElementsBuilder<T, U> {

}
