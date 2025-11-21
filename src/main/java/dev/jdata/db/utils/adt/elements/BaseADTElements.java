package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.ADTConstants;
import dev.jdata.db.utils.allocators.Allocatable;

public abstract class BaseADTElements extends Allocatable implements IOnlyElementsView {

    protected static final int DEFAULT_INITIAL_CAPACITY = ADTConstants.DEFAULT_INITIAL_CAPACITY;

    protected static final int DEFAULT_CAPACITY_MULTIPLICATOR = ADTConstants.DEFAULT_CAPACITY_MULTIPLICATOR;

    protected BaseADTElements(AllocationType allocationType) {
        super(allocationType);
    }
}
