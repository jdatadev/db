package dev.jdata.db.utils.adt.elements;

public interface IIntElementsMutators {

    void add(int value);
    void addAll(IIntElements intElements);

    boolean remove(int value);
}
