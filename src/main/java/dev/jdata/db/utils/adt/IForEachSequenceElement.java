package dev.jdata.db.utils.adt;

@FunctionalInterface
public interface IForEachSequenceElement<T> {

    void each(int index, T element);
}
