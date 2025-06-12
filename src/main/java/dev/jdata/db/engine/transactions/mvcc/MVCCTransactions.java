package dev.jdata.db.engine.transactions.mvcc;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.utils.adt.lists.FreeList;
import dev.jdata.db.utils.adt.lists.MutableIndexList;
import dev.jdata.db.utils.adt.sets.MutableLongBucketSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public final class MVCCTransactions {

    private final MutableIndexList<MVCCTransaction> ongoingTransactions;
    private final MutableIndexList<MVCCTransaction> ongoingOriginatingFromTransactions;

    private final FreeList<MVCCTransaction> freeList;

    private int scratchTransactionDesriptor;
    private long scratchTransactionId;

    MVCCTransactions() {

        this.ongoingTransactions = new MutableIndexList<>(AllocationType.HEAP);
        this.ongoingOriginatingFromTransactions = new MutableIndexList<>(AllocationType.HEAP);

        this.freeList = new FreeList<>(MVCCTransaction[]::new);
    }

    synchronized void select(TransactionSelect select, MVCCTransaction mvccTransaction, BufferedRows commitedRows, MutableLongBucketSet addedRowIdsDst,
            MutableLongBucketSet removedRowIdsDst, MutableIndexList<MVCCTransaction> scratchTransansactionList) {

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

                final long index = ongoingOriginatingFromTransactions.findIndex(this, (o, t) -> o.getOriginatingTransactionId() == t.scratchTransactionId);

                if (index == -1) {

                    throw new IllegalStateException();
                }

                for (long i = index; i <= 0; -- i) {

                    final MVCCTransaction originatingTransaction = ongoingOriginatingFromTransactions.get(i);

                    scratchTransansactionList.addTail(originatingTransaction);
                }
            }

            final long numElements = scratchTransansactionList.getNumElements();

            for (long i = 0L; i < numElements; ++ i) {

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
            originatingTransactionId = ongoingOriginatingFromTransactions.getHead().getOriginatingTransactionId();
        }

        mvccTransaction.initialize(transactionDescriptor, originatingTransactionId, isolationLevel);

        ongoingTransactions.addTail(mvccTransaction);
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

        if (ongoingTransactions.containsInstance(mvccTransaction)) {

            throw new IllegalArgumentException();
        }

        if (ongoingOriginatingFromTransactions.containsInstance(mvccTransaction)) {

            throw new IllegalArgumentException();
        }

        final long originatingTransactionId = mvccTransaction.getOriginatingTransactionId();

        if (!nonThreadSafeHasOriginatingTransactionId(originatingTransactionId)) {

            throw new IllegalArgumentException();
        }

        removeAnyExpiredOriginatingFrom();

        ongoingOriginatingFromTransactions.addTail(mvccTransaction);
//        ongoingTransactions.remove(mvccTransaction);
    }

    private void removeAnyExpiredOriginatingFrom() {

        final long numElements = ongoingOriginatingFromTransactions.getNumElements();

        long removeUpTo = -1L;

        for (long i = 0L; i < numElements; ++ i) {

            final MVCCTransaction originatingFromTransaction = ongoingOriginatingFromTransactions.get(i);

            if (nonThreadSafeHasReferringToOriginatingTransactionId(originatingFromTransaction.getTransactionId())) {

                break;
            }
            else {
                removeUpTo = i;
            }
        }

        ongoingOriginatingFromTransactions.removeHead(removeUpTo);
    }

    private boolean nonThreadSafeHasReferringToOriginatingTransactionId(long transactionId) {

        Checks.isTransactionId(transactionId);

        this.scratchTransactionId = transactionId;

        return ongoingTransactions.contains(this, (o, t) -> o.getTransactionId() == t.scratchTransactionId);
    }

    private boolean nonThreadSafeHasOriginatingTransactionId(long transactionId) {

        Checks.isTransactionId(transactionId);

        this.scratchTransactionId = transactionId;

        return ongoingOriginatingFromTransactions.contains(this, (o, t) -> o.getTransactionId() == t.scratchTransactionId);
    }

    private void releaseTransaction(MVCCTransaction mvccTransaction) {

        try {
            if (!ongoingTransactions.removeInstance(mvccTransaction)) {

                throw new IllegalStateException();
            }
        }
        finally {

            freeList.free(mvccTransaction);
        }
    }
}
