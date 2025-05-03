package dev.jdata.db.utils.adt.lists;

final class IntValues<LIST extends BaseInnerOuterList<int[], LIST, IntValues<LIST>>> extends BaseIntValues<LIST, IntValues<LIST>> {

    IntValues(int initialOuterCapacity) {
        super(initialOuterCapacity);
    }
}
