package dev.jdata.db.utils.adt.lists;

public interface LargeIntList extends IntList, LargeList {

    int getValue(long node);

    long addHead(int value);
    long addTail(int value);

    int removeHead();
}
