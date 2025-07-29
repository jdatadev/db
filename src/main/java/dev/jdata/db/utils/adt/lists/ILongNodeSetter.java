package dev.jdata.db.utils.adt.lists;

@FunctionalInterface
public interface ILongNodeSetter<T> {

    void setNode(T instance, long node);
}
