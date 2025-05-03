package dev.jdata.db.utils.adt.lists;

final class LongValues<LIST extends BaseInnerOuterList<long[], LIST, LongValues<LIST>>> extends BaseLongValues<LIST, LongValues<LIST>> {

    LongValues(int initialOuterCapacity) {
        super(initialOuterCapacity);
    }
}
