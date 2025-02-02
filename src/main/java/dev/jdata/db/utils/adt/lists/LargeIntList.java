package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.Clearable;

public interface LargeIntList extends IntList, LargeList, Clearable {

    int getValue(long node);

    long addHead(int value);
    long addTail(int value);

    int removeHead();
}
