package dev.jdata.db.schema.allocators.model.diff.dropped;

import dev.jdata.db.schema.model.diff.dropped.DroppedElements;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;

public interface IDroppedElementsAllocator {

    IBaseMutableIntSetAllocator getMutableIntSetAllocator();

    IIntToObjectMapAllocator<IMutableIntSet> getIntToObjectMapAllocator();

    DroppedElements allocateDroppedElements();

    void freeDroppedElements(DroppedElements droppedElements);
}
