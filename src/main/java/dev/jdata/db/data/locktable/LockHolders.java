package dev.jdata.db.data.locktable;

import dev.jdata.db.data.locktable.LockTable.LockType;

public final class LockHolders {

    private final long[] transactionValues;
    private final int[] statementValues;

    LockHolders(long[] transactionValues, int[] statementValues) {

        this.transactionValues = transactionValues;
        this.statementValues = statementValues;
    }

    public int getNumTransactionValues() {

        return transactionValues.length;
    }

    public long getTransactionId(int index) {

        return LockTable.transactionId(transactionValues[index]);
    }

    public LockType getTransactionLockType(int index) {

        return LockTable.transactionLockType(transactionValues[index]);
    }

    public int getNumStatementValues() {

        return statementValues.length;
    }

    public int getStatementId(int index) {

        return LockTable.statementId(statementValues[index]);
    }

    public LockType getStatementLockType(int index) {

        return LockTable.statementLockType(statementValues[index]);
    }
}
