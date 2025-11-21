package dev.jdata.db.utils.adt.maps;

@Deprecated // necessary?
@FunctionalInterface
public interface IIntCapacityMapIndexValueAdder<T, U> {

    void addValue(T src, int srcIndex, U dst);
}
