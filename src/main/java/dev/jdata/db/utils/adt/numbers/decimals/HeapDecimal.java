package dev.jdata.db.utils.adt.numbers.decimals;

final class HeapDecimal extends ImmutableDecimal<HeapDecimal> implements IHeapDecimal {

    static HeapDecimal create(AllocationType allocationType, int precision, int scale) {

        AllocationType.checkIsHeap(allocationType);

        return new HeapDecimal(allocationType, precision, scale);
    }

    private HeapDecimal(AllocationType allocationType, int precision, int scale) {
        super(allocationType, precision, scale);
    }
}
