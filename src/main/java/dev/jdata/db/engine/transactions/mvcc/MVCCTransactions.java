package dev.jdata.db.engine.transactions.mvcc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.utils.adt.lists.FreeList;
import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.adt.sets.LongSet;
import dev.jdata.db.utils.checks.Checks;

public final class MVCCTransactions {

    private final List<MVCCTransaction> ongoingTransactions;
    private final List<MVCCTransaction> ongoingOriginatingFromTransactions;

    private final FreeList<MVCCTransaction> freeList;

    private int scratchTransactionDesriptor;
    private long scratchTransactionId;

    MVCCTransactions() {

        this.ongoingTransactions = new ArrayList<>();
        this.ongoingOriginatingFromTransactions = new ArrayList<>();

        this.freeList = new FreeList<>(MVCCTransaction[]::new);
    }

    synchronized void select(TransactionSelect select, MVCCTransaction mvccTransaction, BufferedRows commitedRows, LongSet addedRowIdsDst, LongSet removedRowIdsDst,
            List<MVCCTransaction> scratchTransansactionList) {

        Objects.requireNonNull(select);
        Objects.requireNonNull(mvccTransaction);
        Objects.requireNonNull(commitedRows);
        Objects.requireNonNull(addedRowIdsDst);
        Objects.requireNonNull(removedRowIdsDst);
        Checks.isNotEmpty(scratchTransansactionList);

        mvccTransaction.select(select, commitedRows, addedRowIdsDst, removedRowIdsDst);

        switch (mvccTransaction.getIsolationLevel()) {

        case COMMITTED_READ:

            break;

        case REPEATABLE_READ:
        case SERIALIZABLE:

            synchronized (this) {

                this.scratchTransactionId = mvccTransaction.getOriginatingTransactionId();

                final int index = Lists.findIndexWithoutClosure(ongoingOriginatingFromTransactions, this, (t, o) -> t.scratchTransactionId == o.getOriginatingTransactionId());

                if (index == -1) {

                    throw new IllegalStateException();
                }

                for (int i = index; i <= 0; -- i) {

                    final MVCCTransaction originatingTransaction = ongoingOriginatingFromTransactions.get(i);

                    scratchTransansactionList.add(originatingTransaction);
                }
            }

            final int numElements = scratchTransansactionList.size();

            for (int i = 0; i < numElements; ++ i) {

                final MVCCTransaction originatingTransaction = scratchTransansactionList.get(i);

                originatingTransaction.select(select, commitedRows, removedRowIdsDst, addedRowIdsDst);
            }
            break;

        default:
            throw new UnsupportedOperationException();
        }
    }

    synchronized void addStartedTransaction(int transactionDescriptor, DBIsolationLevel isolationLevel) {

        Checks.isTransactionDescriptor(transactionDescriptor);
        Objects.requireNonNull(isolationLevel);
/*
        if (!ongoingTransactions.isEmpty() && transactionId <= ongoingTransactions.get(ongoingTransactions.size() - 1).getTransactionId()) {

            throw new IllegalArgumentException();
        }
*/
/*
        this.scratchTransactionId = originatingTransactionId;

        if (Lists.containsWithoutClosure(ongoingTransactions, this, (t, o) -> t.scratchTransactionId == o.getTransactionId())) {

            throw new IllegalArgumentException();
        }
*/
        MVCCTransaction mvccTransaction = freeList.allocate();

        if (mvccTransaction == null) {

            mvccTransaction = new MVCCTransaction();
        }

        final long originatingTransactionId;

        if (ongoingOriginatingFromTransactions.isEmpty()) {

            originatingTransactionId = DBConstants.NO_TRANSACTION_ID;
        }
        else {
            originatingTransactionId = ongoingOriginatingFromTransactions.get(0).getOriginatingTransactionId();
        }

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        ongoingTransactions.add(mvccTransaction);
    }

    synchronized void commit(MVCCTransaction mvccTransaction) {

        Objects.requireNonNull(mvccTransaction);

        releaseTransaction(mvccTransaction);
    }

    synchronized void rollback(MVCCTransaction mvccTransaction) {

        Objects.requireNonNull(mvccTransaction);

        releaseTransaction(mvccTransaction);
    }

    private void moveToOngoingTransactions(MVCCTransaction mvccTransaction, long transactionId) {

        Objects.requireNonNull(mvccTransaction);
        Checks.isTransactionId(transactionId);

        mvccTransaction.setTransactionId(transactionId);

        if (ongoingTransactions.contains(mvccTransaction)) {

            throw new IllegalArgumentException();
        }

        if (ongoingOriginatingFromTransactions.contains(mvccTransaction)) {

            throw new IllegalArgumentException();
        }

        final long originatingTransactionId = mvccTransaction.getOriginatingTransactionId();

        if (!nonThreadSafeHasOriginatingTransactionId(originatingTransactionId)) {

            throw new IllegalArgumentException();
        }

        removeAnyExpiredOriginatingFrom();

        ongoingOriginatingFromTransactions.add(mvccTransaction);
//        ongoingTransactions.remove(mvccTransaction);
    }

    private void removeAnyExpiredOriginatingFrom() {

        final int numElements = ongoingOriginatingFromTransactions.size();

        int removeUpTo = -1;

        for (int i = 0; i < numElements; ++ i) {

            final MVCCTransaction originatingFromTransaction = ongoingOriginatingFromTransactions.get(i);

            if (nonThreadSafeHasReferringToOriginatingTransactionId(originatingFromTransaction.getTransactionId())) {

                break;
            }
            else {
                removeUpTo = i;
            }
        }

        for (int i = 0; i <= removeUpTo; ++ i) {

            ongoingOriginatingFromTransactions.remove(0);
        }
    }

    private boolean nonThreadSafeHasReferringToOriginatingTransactionId(long transactionId) {

        Checks.isTransactionId(transactionId);

        this.scratchTransactionId = transactionId;

        return Lists.containsWithoutClosure(ongoingTransactions, this, (t, o) -> t.scratchTransactionId == o.getTransactionId());
    }

    private boolean nonThreadSafeHasOriginatingTransactionId(long transactionId) {

        Checks.isTransactionId(transactionId);

        this.scratchTransactionId = transactionId;

        return Lists.containsWithoutClosure(ongoingOriginatingFromTransactions, this, (t, o) -> t.scratchTransactionId == o.getTransactionId());
    }

    private void releaseTransaction(MVCCTransaction mvccTransaction) {

        try {
            if (!ongoingTransactions.remove(mvccTransaction)) {

                throw new IllegalStateException();
            }
        }
        finally {

            freeList.free(mvccTransaction);
        }
    }
}
