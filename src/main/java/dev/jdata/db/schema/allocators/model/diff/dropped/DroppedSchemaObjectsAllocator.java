package dev.jdata.db.schema.allocators.model.diff.dropped;

import dev.jdata.db.schema.model.diff.dropped.DroppedElements;
import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;

public abstract class DroppedSchemaObjectsAllocator extends BaseDroppedSchemaObjectsAllocator<MutableIntMaxDistanceNonBucketSet, DroppedElements> {

    protected  DroppedSchemaObjectsAllocator(IMutableIntSetAllocator<MutableIntMaxDistanceNonBucketSet> intSetAllocator,
            IIntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> intToObjectMapAllocator) {
        super(intSetAllocator, intToObjectMapAllocator);
    }
}
