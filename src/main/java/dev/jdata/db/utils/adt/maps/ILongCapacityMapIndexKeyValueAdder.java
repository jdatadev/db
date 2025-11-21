package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
interface ILongCapacityMapIndexKeyValueAdder<KEYS_SRC, KEYS_DST, VALUES_SRC, VALUES_DST> {

    void addValue(long index, KEYS_SRC keysSrc, KEYS_DST keysDst, VALUES_SRC valuesSrc, VALUES_DST valuesDst);
}
