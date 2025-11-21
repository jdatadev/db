package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
interface IIntCapacityMapIndexKeyValueAdder<KEYS_SRC, KEYS_DST, VALUES_SRC, VALUES_DST> {

    void addValue(int index, KEYS_SRC keysSrc, KEYS_DST keysDst, VALUES_SRC valuesSrc, VALUES_DST valuesDst);
}
