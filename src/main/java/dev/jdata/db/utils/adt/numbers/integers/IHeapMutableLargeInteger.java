package dev.jdata.db.utils.adt.numbers.integers;

import dev.jdata.db.utils.adt.numbers.IHeapNumberMarker;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IHeapMutableLargeInteger extends IMutableLargeInteger, IHeapNumberMarker {

    public static IHeapMutableLargeInteger create() {

        return ofPrecision(16);
    }

    public static IHeapMutableLargeInteger ofPrecision(int precision) {

        Checks.isIntegerPrecision(precision);

        return new HeapMutableLargeInteger(AllocationType.HEAP, precision);
    }
}
