package dev.jdata.db.schema.allocators.model.diff.dropped;

import dev.jdata.db.schema.model.diff.dropped.DroppedElements;
import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;

public interface IDroppedElementsAllocator {

    IMutableIntSetAllocator getIntSetAllocator();

    IIntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> getIntToObjectMapAllocator();

    DroppedElements allocateDroppedElements();

    void freeDroppedElements(DroppedElements droppedElements);
}
