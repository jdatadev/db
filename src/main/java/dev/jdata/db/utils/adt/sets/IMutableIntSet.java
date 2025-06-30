package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.utils.adt.elements.IIntUnorderedElementsMutators;
import dev.jdata.db.utils.adt.elements.IMutableIntElements;
import dev.jdata.db.utils.allocators.IIntSetAllocator;

public interface IMutableIntSet extends IIntSetCommon, IMutableSet, IMutableIntElements, IIntUnorderedElementsMutators, IIntSetMutators {

    <T extends IIntSet> T toImmutable(IIntSetAllocator<T> intSetAllocator);
}
