package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.IClearable;

public interface LargeIntList extends IntList, LargeList, IClearable {

    int getValue(long node);

    long addHead(int value);
    long addTail(int value);

    int removeHead();
}
