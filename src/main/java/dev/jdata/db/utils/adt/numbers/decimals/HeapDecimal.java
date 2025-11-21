package dev.jdata.db.utils.adt.numbers.decimals;

final class HeapDecimal extends ImmutableDecimal<HeapDecimal> implements IHeapDecimal {

    HeapDecimal(AllocationType allocationType, int precision, int scale) {
        super(allocationType, precision, scale);

        AllocationType.checkIsHeap(allocationType);
    }
}
