package dev.jdata.db.utils.adt.lists;

public interface LargeLongList extends LongList, LargeList {

    long addHead(long value);
    long addTail(long value);
    long removeHead();
    long getValue(long node);
}
