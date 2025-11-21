package dev.jdata.db.utils.adt.maps;

@Deprecated // implement properly
public interface ILongToIntMapAllocator {

    IMutableLongToIntWithRemoveStaticMap allocateLongToIntMap(int initialCapacityExponent);

    void freeLongToIntMap(IMutableLongToIntWithRemoveStaticMap longToIntMap);
}

