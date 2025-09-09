package dev.jdata.db.engine.database;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.engine.sessions.DBSession.LargeObjectStorer;
import dev.jdata.db.engine.transactions.Transactions.TransactionFactory;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseParameters implements IResettable {

    private IDatabasesAllocators allocators;
    private DatabaseStringManagement stringManagement;
    private LargeObjectStorer<IOException> largeObjectStorer;
    private TransactionFactory transactionFactory;

    private String name;
    private DatabaseSchemaManager databaseSchemas;
    private DataCache dataCache;
    private long initialTransactionId;
    private long[] initialRowIds;

    public void initializeStatic(IDatabasesAllocators allocators, DatabaseStringManagement stringManagement, LargeObjectStorer<IOException> largeObjectStorer,
            TransactionFactory transactionFactory) {

        this.allocators = Initializable.checkNotYetInitialized(this.allocators, allocators);
        this.stringManagement = Initializable.checkNotYetInitialized(this.stringManagement, stringManagement);
        this.largeObjectStorer = Initializable.checkNotYetInitialized(this.largeObjectStorer, largeObjectStorer);
        this.transactionFactory = Initializable.checkNotYetInitialized(this.transactionFactory, transactionFactory);
    }

    public void initializePerDatabase(DatabaseSchemaManager databaseSchemas, DataCache dataCache, long initialTransactionId, long[] initialRowIds) {

        this.databaseSchemas = Objects.requireNonNull(databaseSchemas);
        this.dataCache = dataCache;
        this.initialTransactionId = initialTransactionId != DBConstants.NO_TRANSACTION_ID ? Checks.isTransactionId(initialTransactionId) : DBConstants.NO_TRANSACTION_ID;
        this.initialRowIds = initialRowIds;
    }

    @Override
    public void reset() {

        this.allocators = Initializable.checkResettable(allocators);
        this.stringManagement = Initializable.checkResettable(stringManagement);
        this.largeObjectStorer = Initializable.checkResettable(largeObjectStorer);
        this.transactionFactory = Initializable.checkResettable(transactionFactory);

        this.name = null;
        this.databaseSchemas = null;
        this.dataCache = null;
        this.initialTransactionId = DBConstants.NO_TRANSACTION_ID;
        this.initialRowIds = null;
    }

    public IDatabasesAllocators getAllocators() {
        return allocators;
    }

    public DatabaseStringManagement getStringManagement() {
        return stringManagement;
    }

    public LargeObjectStorer<IOException> getLargeObjectStorer() {
        return largeObjectStorer;
    }

    public DatabaseSchemaManager getDatabaseSchemas() {
        return databaseSchemas;
    }

    public DataCache getDataCache() {
        return dataCache;
    }

    long getInitialTransactionId() {
        return initialTransactionId;
    }

    long[] getInitialRowIds() {
        return initialRowIds;
    }

    TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }
}
