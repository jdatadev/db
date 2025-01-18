package dev.jdata.db.utils.adt.lists;

public interface LargeList extends LargeListIterable {

    long getHeadNode();
    long getTailNode();
}
