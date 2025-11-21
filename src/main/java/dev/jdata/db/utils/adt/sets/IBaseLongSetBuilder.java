package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.ILongUnorderedOnlyElementsBuilder;

interface IBaseLongSetBuilder<T extends IBaseLongSet, U extends IBaseLongSet & IHeapContainsMarker> extends ILongUnorderedOnlyElementsBuilder<T, U> {

}
