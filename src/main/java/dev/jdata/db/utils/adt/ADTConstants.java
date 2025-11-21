package dev.jdata.db.utils.adt;

import dev.jdata.db.utils.adt.capacity.CapacityExponents;

public class ADTConstants {

    public static final int DEFAULT_INITIAL_CAPACITY = 10;

    public static final int DEFAULT_CAPACITY_MULTIPLICATOR = 2;

    public static final int DEFAULT_HASHED_INITIAL_CAPACITY_EXPONENT = 3;
    public static final int DEFAULT_HASHED_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DEFAULT_HASHED_INITIAL_CAPACITY_EXPONENT);

    public static final float DEFAULT_HASHED_LOAD_FACTOR = 0.75f;
}
