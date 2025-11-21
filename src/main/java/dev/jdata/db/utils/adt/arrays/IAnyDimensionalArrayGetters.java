package dev.jdata.db.utils.adt.arrays;

interface IAnyDimensionalArrayGetters extends IArrayGettersMarker {

    boolean hasClearValue();

    void toString(StringBuilder sb);
    void toHexString(StringBuilder sb);

    default String toHexString() {

        final StringBuilder sb = new StringBuilder();

        toHexString(sb);

        return sb.toString();
    }
}
