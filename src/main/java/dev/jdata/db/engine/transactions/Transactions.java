package dev.jdata.db.engine.transactions;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.descriptorables.BaseSingleTypeDescriptorables;
import dev.jdata.db.utils.adt.maps.LongToObjectMap;
import dev.jdata.db.utils.checks.Checks;

public final class Transactions extends BaseSingleTypeDescriptorables<Transaction.TransactionState, Transaction> {

    @FunctionalInterface
    public interface TransactionFactory {

        Transaction createTransaction();
    }

    private static final IntFunction<Transaction[]> createTransactionArray = Transaction[]::new;

    private final TransactionFactory transactionFactory;

    private long transactionIdAllocator;

    private final LongToObjectMap<Transaction> transactionByGlobalTransactionId;

    public Transactions(long initialTransactionId, TransactionFactory transactionFactory) {
        super(createTransactionArray);

        this.transactionIdAllocator = initialTransactionId != DBConstants.NO_TRANSACTION_ID ? Checks.isTransactionId(initialTransactionId) : DBConstants.INITIAL_TRANSACTION_ID;
        this.transactionFactory = Objects.requireNonNull(transactionFactory);

        this.transactionByGlobalTransactionId = new LongToObjectMap<>(0, createTransactionArray);
    }

    public synchronized Transaction getTransaction(int transactionDescriptor) {

        Checks.isTransactionDescriptor(transactionDescriptor);

        return getDescriptorable(transactionDescriptor);
    }

    public synchronized int addTransaction() {

        final Transaction transaction = addDescriptorable(this, t -> t.transactionFactory.createTransaction());

        Objects.requireNonNull(transaction);

        transactionByGlobalTransactionId.put(transaction.getGlobalTransactionId(), transaction);

        return transaction.getTransactionDescriptor();
    }

    public synchronized void removeTransaction(Transaction transaction) {

        Objects.requireNonNull(transaction);

        if (!transactionByGlobalTransactionId.remove(transaction.getGlobalTransactionId())) {

            throw new IllegalArgumentException();
        }

        removeDescriptorable(transaction);
    }
}
