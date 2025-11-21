package dev.jdata.db.utils.adt.numbers.decimals;

import java.math.BigDecimal;
import java.util.Objects;

import dev.jdata.db.utils.adt.numbers.IHeapNumberMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableDecimal extends IMutableDecimal, IHeapNumberMarker {

    public static IHeapMutableDecimal ofPrecision(int precision) {

        Checks.isDecimalPrecision(precision);

        return new HeapMutableDecimal(AllocationType.HEAP, precision, 0);
    }

    public static IHeapMutableDecimal valueOf(BigDecimal value) {

        Objects.requireNonNull(value);

        return new HeapMutableDecimal(AllocationType.HEAP, value);
    }
}
