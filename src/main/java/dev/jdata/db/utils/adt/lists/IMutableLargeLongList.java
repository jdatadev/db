package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.IClearable;

public interface IMutableLargeLongList extends ILongList, ILargeList, IClearable {

    long getValue(long node);

    long addHead(long value);
    long addTail(long value);

    long removeHead();
}
