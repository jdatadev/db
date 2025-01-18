package dev.jdata.db.utils.adt.lists;

@FunctionalInterface
public interface LongNodeSetter<T> {

    void setNode(T instance, long node);
}
