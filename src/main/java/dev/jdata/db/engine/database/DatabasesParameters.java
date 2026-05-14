package dev.jdata.db.engine.database;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.engine.database.allocators.IDatabasesAllocators;
import dev.jdata.db.engine.sessions.DBSession.ILargeObjectStorer;
import dev.jdata.db.engine.transactions.Transactions.ITransactionFactory;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSet;

public final class DatabasesParameters<T extends IMutableLongLargeArray, U extends IMutableLongLargeSet> {

    private final IDatabasesAllocators<T, U> databasesAllocators;
    private final DatabaseStringManagement stringManagement;
    private final ILargeObjectStorer<IOException> largeObjectStorer;
    private final ITransactionFactory transactionFactory;

    public DatabasesParameters(IDatabasesAllocators<T, U> databasesAllocators, DatabaseStringManagement stringManagement, ILargeObjectStorer<IOException> largeObjectStorer,
            ITransactionFactory transactionFactory) {

        this.databasesAllocators = Objects.requireNonNull(databasesAllocators);
        this.stringManagement = Objects.requireNonNull(stringManagement);
        this.largeObjectStorer = Objects.requireNonNull(largeObjectStorer);
        this.transactionFactory = Objects.requireNonNull(transactionFactory);
    }

    public IDatabasesAllocators<T, U> getAllocators() {
        return databasesAllocators;
    }

    public DatabaseStringManagement getStringManagement() {
        return stringManagement;
    }

    public ILargeObjectStorer<IOException> getLargeObjectStorer() {
        return largeObjectStorer;
    }

    ITransactionFactory getTransactionFactory() {
        return transactionFactory;
    }
}
