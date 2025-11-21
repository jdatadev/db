package dev.jdata.db.utils.adt.numbers.decimals;

import java.math.BigDecimal;

final class HeapMutableDecimal extends MutableDecimal<HeapMutableDecimal> implements IHeapMutableDecimal {

    HeapMutableDecimal(AllocationType allocationType, int precision, int scale) {
        super(allocationType, precision, scale);

        AllocationType.checkIsHeap(allocationType);
    }

    HeapMutableDecimal(AllocationType allocationType, BigDecimal value) {
        super(allocationType, value);

        AllocationType.checkIsHeap(allocationType);
    }
}
