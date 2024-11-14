package dev.jdata.db.utils.adt;

@FunctionalInterface
public interface ForEachSequenceElement<T> {

    void each(int index, T element);
}
