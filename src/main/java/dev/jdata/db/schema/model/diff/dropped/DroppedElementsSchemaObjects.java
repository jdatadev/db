package dev.jdata.db.schema.model.diff.dropped;

import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;

public final class DroppedElementsSchemaObjects extends BaseDroppedElementsSchemaObjects<MutableIntMaxDistanceNonBucketSet, DroppedElements> {

    public DroppedElementsSchemaObjects() {
        super(DroppedElements[]::new);
    }
}
