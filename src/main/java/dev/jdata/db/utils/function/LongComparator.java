package dev.jdata.db.utils.function;

@FunctionalInterface
public interface LongComparator {

    int compare(long l1, long l2);
}
