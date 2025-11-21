package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntCapacityMapIndexKeyValueAdder<KEYS_SRC, VALUES_SRC, KEYS_DST, VALUES_DST> extends IKeyValueAdder {

    void addValue(int srcIndex, KEYS_SRC keysSrc, VALUES_SRC valuesSrc, int dstIndex, KEYS_DST keysDst, VALUES_DST valuesDst);
}
