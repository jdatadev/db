package dev.jdata.db.schema.allocators.model.diff.dropped;

import dev.jdata.db.schema.model.diff.dropped.BaseDroppedElements;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;

public interface IDroppedElementsAllocator<T extends IMutableIntSet, U extends BaseDroppedElements<T>> {

    IMutableIntSetAllocator<T> getIntSetAllocator();

    IIntToObjectMapAllocator<T> getIntToObjectMapAllocator();

    U allocateDroppedElements();

    void freeDroppedElements(U droppedElements);
}
