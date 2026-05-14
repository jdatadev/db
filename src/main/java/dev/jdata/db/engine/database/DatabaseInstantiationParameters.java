package dev.jdata.db.engine.database;

import dev.jdata.db.DBConstants;
import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

final class DatabaseInstantiationParameters extends ObjectCacheNode implements IResettable {

    private String name;
    private DatabaseSchemaManager databaseSchemaManager;
    private DataCache dataCache;
    private long initialTransactionId;
    private long[] initialRowIdsByTableId;

    DatabaseInstantiationParameters(AllocationType allocationType) {
        super(allocationType);
    }

    void initialize(String name, DatabaseSchemaManager databaseSchemaManager, DataCache dataCache, long initialTransactionId, long[] initialRowIdsByTableId) {

        this.name = Initializable.checkNotYetInitialized(this.name, name);
        this.databaseSchemaManager = Initializable.checkNotYetInitialized(this.databaseSchemaManager, databaseSchemaManager);
        this.dataCache = Initializable.checkNotYetInitializedNullable(this.dataCache, dataCache);
        this.initialTransactionId = initialTransactionId != DBConstants.NO_TRANSACTION_ID ? Checks.isTransactionId(initialTransactionId) : DBConstants.NO_TRANSACTION_ID;
        this.initialRowIdsByTableId = Initializable.checkNotYetInitializedNullable(this.initialRowIdsByTableId, initialRowIdsByTableId);
    }

    @Override
    public void reset() {

        this.name = Initializable.checkResettable(name);
        this.databaseSchemaManager = Initializable.checkResettable(databaseSchemaManager);
        this.dataCache = Initializable.checkResettableNullable(dataCache);
        this.initialTransactionId = DBConstants.NO_TRANSACTION_ID;
        this.initialRowIdsByTableId = Initializable.checkResettable(initialRowIdsByTableId);
    }

    String getName() {
        return name;
    }

    DatabaseSchemaManager getDatabaseSchemaManager() {
        return databaseSchemaManager;
    }

    DataCache getDataCache() {
        return dataCache;
    }

    long getInitialTransactionId() {
        return initialTransactionId;
    }

    long[] getInitialRowIds() {
        return initialRowIdsByTableId;
    }
}
