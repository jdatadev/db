package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

public abstract class InheritableLongKeyNonBucketMap<VALUES, MAP extends InheritableLongKeyNonBucketMap<VALUES, MAP>> extends BaseLongKeyNonBucketMap<VALUES, MAP> {

    protected InheritableLongKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);
    }
}
