package dev.jdata.db.engine.database;

import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.engine.sessions.DBSession.LargeObjectStorer;
import dev.jdata.db.engine.transactions.Transactions.TransactionFactory;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseParameters implements IClearable {

    private IDatabasesAllocators allocators;
    private DatabaseStringManagement stringManagement;
    private LargeObjectStorer<IOException> largeObjectStorer;
    private TransactionFactory transactionFactory;

    private String name;
    private DatabaseSchemaManager databaseSchemas;
    private DataCache dataCache;
    private long initialTransactionId;
    private long[] initialRowIds;

    @Override
    public void clear() {

        this.allocators = null;
        this.stringManagement = null;
        this.largeObjectStorer = null;
        this.transactionFactory = null;

        this.name = null;
        this.databaseSchemas = null;
        this.dataCache = null;
        this.initialTransactionId = DBConstants.NO_TRANSACTION_ID;
        this.initialRowIds = null;
    }

    public void initializeStatic(IDatabasesAllocators allocators, DatabaseStringManagement stringManagement, LargeObjectStorer<IOException> largeObjectStorer,
            TransactionFactory transactionFactory) {

        this.allocators = Objects.requireNonNull(allocators);
        this.stringManagement = Objects.requireNonNull(stringManagement);
        this.largeObjectStorer = Objects.requireNonNull(largeObjectStorer);
        this.transactionFactory= Objects.requireNonNull(transactionFactory);
    }

    public void initializePerDatabase(DatabaseSchemaManager databaseSchemas, DataCache dataCache, long initialTransactionId, long[] initialRowIds) {

        this.databaseSchemas = Objects.requireNonNull(databaseSchemas);
        this.dataCache = dataCache;
        this.initialTransactionId = initialTransactionId != DBConstants.NO_TRANSACTION_ID ? Checks.isTransactionId(initialTransactionId) : DBConstants.NO_TRANSACTION_ID;
        this.initialRowIds = initialRowIds;
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
