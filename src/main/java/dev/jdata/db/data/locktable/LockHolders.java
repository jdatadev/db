package dev.jdata.db.data.locktable;

import dev.jdata.db.LockType;
import dev.jdata.db.utils.adt.elements.IElements;

public final class LockHolders implements IElements {

    private final long[] lockInfoValues;

    LockHolders(long[] lockInfoValues) {

        this.lockInfoValues = lockInfoValues;
    }

    @Override
    public boolean isEmpty() {

        return lockInfoValues.length == 0;
    }

    @Override
    public long getNumElements() {

        return lockInfoValues.length;
    }

    public int getTransactionDescriptor(int index) {

        return LockTable.transactionDescriptor(lockInfoValues[index]);
    }

    public LockType getTransactionLockType(int index) {

        return LockTable.transactionLockType(lockInfoValues[index]);
    }

    public int getStatementId(int index) {

        return LockTable.statementId(lockInfoValues[index]);
    }
}
