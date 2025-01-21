package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.engine.descriptorables.BaseSingleTypeDescriptorables;
import dev.jdata.db.utils.adt.maps.LongToObjectMap;
import dev.jdata.db.utils.checks.Checks;

public final class Transactions<T extends Transaction> extends BaseSingleTypeDescriptorables<Transaction.TransactionState, T> {

    public interface TransactionFactory<T extends Transaction> {

        T[] createTransactionArray(int length);

        T createTransaction(long globalTransactionId, int transactionDescriptor);
    }

    private final TransactionFactory<T> transactionFactory;

    private long transactionIdAllocator;

    private final LongToObjectMap<T> transactionByGlobalTransactionId;

    public Transactions(long initialTransactionId, TransactionFactory<T> transactionFactory) {
        super(transactionFactory::createTransactionArray);

        this.transactionIdAllocator = Checks.isTransactionId(initialTransactionId);
        this.transactionFactory = Objects.requireNonNull(transactionFactory);

        this.transactionByGlobalTransactionId = new LongToObjectMap<>(0, transactionFactory::createTransactionArray);
    }

    public synchronized int addTransaction() {

        final T transaction = null; // addDescriptorable(this, (d, t) -> t.transactionFactory.createTransaction(t.transactionIdAllocator ++, d));

        Objects.requireNonNull(transaction);

        transactionByGlobalTransactionId.put(transaction.getGlobalTransactionId(), transaction);

        return transaction.getTransactionDescriptor();
    }

    public synchronized void removeTransaction(T transaction) {

        Objects.requireNonNull(transaction);

        if (!transactionByGlobalTransactionId.remove(transaction.getGlobalTransactionId())) {

            throw new IllegalArgumentException();
        }

        removeDescriptorable(transaction);
    }
}
