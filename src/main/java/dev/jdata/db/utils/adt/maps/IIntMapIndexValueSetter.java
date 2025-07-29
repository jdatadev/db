package dev.jdata.db.utils.adt.maps;

@FunctionalInterface
public interface IIntMapIndexValueSetter<T, U> {

    void setValue(T src, int srcIndex, U dst, int dstIndex);
}
