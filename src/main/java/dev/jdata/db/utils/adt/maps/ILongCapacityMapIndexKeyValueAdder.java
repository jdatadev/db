package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
interface ILongCapacityMapIndexKeyValueAdder<KEYS_SRC, VALUES_SRC, KEYS_DST, VALUES_DST> {

    void addValue(long srcIndex, KEYS_SRC keysSrc, VALUES_SRC valuesSrc, long dstIndex, KEYS_DST keysDst, VALUES_DST valuesDst);
}
