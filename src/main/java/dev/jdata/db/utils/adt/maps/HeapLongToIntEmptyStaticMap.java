package dev.jdata.db.utils.adt.maps;

final class HeapLongToIntEmptyStaticMap extends LongToIntEmptyStaticMap implements IHeapLongToIntStaticMap {

    private static final HeapLongToIntEmptyStaticMap INSTANCE = new HeapLongToIntEmptyStaticMap();

    static IHeapLongToIntStaticMap empty() {

        return INSTANCE;
    }

    private HeapLongToIntEmptyStaticMap() {

    }
}
