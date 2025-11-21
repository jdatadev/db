package dev.jdata.db.utils.adt.maps;

@Deprecated
@FunctionalInterface
public interface IIntCapacityMapIndexValueSetter<T, U> {

    void setValue(T src, int srcIndex, U dst, int dstIndex);
}
