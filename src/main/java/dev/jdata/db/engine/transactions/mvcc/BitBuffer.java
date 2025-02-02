package dev.jdata.db.engine.transactions.mvcc;

@Deprecated
public interface BitBuffer {

    boolean getBit(long bitOffset);

    long getLong(long bitOffset, boolean signed, int numBits);
}
