package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IIntUnorderedOnlyElementsBuilder;

interface IBaseIntSetBuilder<T extends IBaseIntSet, U extends IBaseIntSet & IHeapContainsMarker> extends IIntUnorderedOnlyElementsBuilder<T, U> {

}
