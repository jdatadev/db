package dev.jdata.db.utils.adt.numbers;

import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseNumber<T extends BaseNumber<T>> extends ObjectCacheNode implements ILargeNumberCommon {

    private int precision;

    protected BaseNumber(AllocationType allocationType, int precision) {
        super(allocationType);

        this.precision = Checks.isNumberPrecision(precision);
    }

    protected BaseNumber(AllocationType allocationType, ILargeNumberView largeNumber) {
        this(allocationType, largeNumber.getPrecision());
    }

    @Override
    public final int getPrecision() {

        return precision;
    }

    protected final void initialize(int precision) {

        this.precision = Checks.isNumberPrecision(precision);
    }
}
