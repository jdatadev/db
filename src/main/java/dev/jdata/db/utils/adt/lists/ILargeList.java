package dev.jdata.db.utils.adt.lists;

public interface ILargeList extends LargeListIterable {

    long getHeadNode();
    long getTailNode();
}
