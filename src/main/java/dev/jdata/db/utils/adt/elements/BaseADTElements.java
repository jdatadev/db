package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.allocators.Allocatable;

public abstract class BaseADTElements extends Allocatable implements IElements {

    protected static final int DEFAULT_INITIAL_CAPACITY = 10;

    protected static final int DEFAULT_CAPACITY_MULTIPLICATOR = 2;

    protected BaseADTElements(AllocationType allocationType) {
        super(allocationType);
    }
}
