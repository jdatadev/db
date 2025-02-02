package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.Clearable;

public interface LargeLongList extends LongList, LargeList, Clearable {

    long addHead(long value);
    long addTail(long value);
    long removeHead();
    long getValue(long node);
}
