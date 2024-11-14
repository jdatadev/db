package dev.jdata.db.utils.adt.lists;

public interface LongList extends LongListIterable {

    long addHead(long value);
    long addTail(long value);
    long getHeadNode();
    long getTailNode();
    long removeHead();
}
