package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.IClearable;

public interface LargeLongList extends LongList, LargeList, IClearable {

    long addHead(long value);
    long addTail(long value);
    long removeHead();
    long getValue(long node);
}
