package dev.jdata.db.engine.database;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.data.cache.row.RowCache;
import dev.jdata.db.engine.database.Database.DatabaseState;
import dev.jdata.db.engine.database.operations.IDatabaseOperations;
import dev.jdata.db.engine.database.strings.IStringCache;
import dev.jdata.db.engine.descriptorables.BaseSingleTypeDescriptorables;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.IDatabaseSessionStatus;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaManager;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.IHeapGenericCompleteDatabaseSchema;
import dev.jdata.db.schema.model.diff.databaseschema.IInitialDiffDatabaseSchema;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSet;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.checks.Checks;

public final class Databases<T extends IMutableLongLargeArray, U extends IMutableLongLargeSet>

        extends BaseSingleTypeDescriptorables<DatabaseState, Database<T, U>> implements IDatabases {

    private static final long[] NO_TRANSACTION_IDS = new long[0];

    private final DatabasesParameters<T, U> databasesParameters;
    private final IStringCache stringCache;

    private final NodeObjectCache<DatabaseInstantiationParameters> databaseInstantiationParametersCache;

    private final IHeapMutableWithRemoveStaticMap<String, Database<T, U>> databaseByName;

    private final IHeapMutableWithRemoveStaticMap<ISQLString, BaseSQLStatement> sqlStatementCache;

    private DatabaseInstantiationParameters scratchDatabaseInstantiationParameters;
    private String scratchDatabaseName;

    public Databases(AllocationType allocationType, DatabasesParameters<T, U> databasesParameters, IStringCache stringCache, boolean cacheStatements) {
        super(allocationType, Database[]::new);

        this.databasesParameters = Objects.requireNonNull(databasesParameters);
        this.stringCache = Objects.requireNonNull(stringCache);

        this.databaseInstantiationParametersCache = new NodeObjectCache<>(DatabaseInstantiationParameters::new);
        this.databaseByName = IHeapMutableWithRemoveStaticMap.create(0, String[]::new, Database[]::new);
        this.sqlStatementCache = cacheStatements ? IHeapMutableWithRemoveStaticMap.create(0, ISQLString[]::new, BaseSQLStatement[]::new) : null;
    }

    @Override
    public int getDatabase(CharSequence dbName) {

        Checks.isDBName(dbName);

        final int result;

        final String dbNameString = stringCache.getOrAddString(dbName);

        synchronized (databaseByName) {

            result = databaseByName.get(dbNameString).getDatabaseId();
        }

        return result;
    }

    @Override
    public int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters) {

        Checks.isDBName(dbName);
        Objects.requireNonNull(parameters);

        Database<T, U> database;

        final String dbNameString = stringCache.getOrAddString(dbName);

        synchronized (databaseByName) {

            database = databaseByName.get(dbNameString);

            if (database == null) {

                database = createAndAddDatabase(dbNameString);
            }
        }

        return database.getDatabaseId();
    }

    @Override
    public int createDatabase(CharSequence dbName, DatabaseParameters parameters) {

        Checks.isDBName(dbName);
        Objects.requireNonNull(parameters);

        Database<T, U> database;

        final String dbNameString = stringCache.getOrAddString(dbName);

        synchronized (databaseByName) {

            database = databaseByName.get(dbNameString);

            if (database == null) {

                database = createAndAddDatabase(dbNameString);
            }
        }

        return database.getDatabaseId();
    }

    private Database<T, U> createAndAddDatabase(String dbName) {

        final Database<T, U> database;

        this.scratchDatabaseInstantiationParameters = Initializable.checkNotYetInitialized(scratchDatabaseInstantiationParameters,
                databaseInstantiationParametersCache.allocate());

        this.scratchDatabaseName = Initializable.checkNotYetInitialized(scratchDatabaseName, dbName);

        try {
            database = addDescriptorable(this, (a, d, i) -> {

                final DatabaseInstantiationParameters instantiationParameters = i.scratchDatabaseInstantiationParameters;
                final String databaseName = i.scratchDatabaseName;

                final DatabaseId databaseId = new DatabaseId(d, databaseName);

                final IHeapGenericCompleteDatabaseSchema initialCompleteDatabaseSchema = IHeapGenericCompleteDatabaseSchema.empty(databaseId, DatabaseSchemaVersion.INITIAL);
                final IIndexList<IInitialDiffDatabaseSchema> initialDiffSchemas = IHeapIndexList.empty();

                final DatabaseSchemaManager databaseSchemaManager = DatabaseSchemaManager.of(databaseId, initialCompleteDatabaseSchema, initialDiffSchemas,
                        i.databasesParameters.getAllocators().getDatabaseSchemaManagementAllocators());

                instantiationParameters.initialize(databaseName, databaseSchemaManager, new RowCache(), DBConstants.INITIAL_TRANSACTION_ID, NO_TRANSACTION_IDS);

                return new Database<>(a, dbName, i.databasesParameters, instantiationParameters);
            });

            databaseByName.put(dbName, database);
        }
        finally {

            databaseInstantiationParametersCache.free(scratchDatabaseInstantiationParameters);

            this.scratchDatabaseInstantiationParameters = Initializable.checkResettable(scratchDatabaseInstantiationParameters);
            this.scratchDatabaseName = Initializable.checkResettable(scratchDatabaseName);
        }

        return database;
    }

    @Override
    public void dropDatabase(int databaseId) {

        Checks.isDatabaseId(databaseId);

        final Database<T, U> database = removeDescriptorable(databaseId);

        synchronized (databaseByName) {

            databaseByName.remove(database.getName());
        }
    }

    @Override
    public int addSession(int databaseId, Charset charset) {

        return getDescriptorable(databaseId).addSession(charset);
    }

    @Override
    public Charset getSessionCharset(int databaseId, int sessionId) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        final Database<T, U> database = getDescriptorable(databaseId);

        return database.getSessionCharset(sessionId);
    }

    @Override
    public void closeSession(int databaseId, int sessionId) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        final Database<T, U> database = getDescriptorable(databaseId);

        database.closeSession(sessionId);
    }

    @Override
    public IDatabaseSessionStatus getDatabaseSessionStatus(int databaseId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int prepareStatement(int databaseId, int sessionId, BaseSQLStatement sqlStatement, ISQLString sqlString) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        if (sqlStatementCache != null) {

            sqlStatementCache.put(sqlString, sqlStatement);
        }

        final Database<T, U> database = getDescriptorable(databaseId);

        return database.prepareStatement(sessionId, sqlStatement, sqlString);
    }

    @Override
    public <E extends Exception> long executePreparedStatement(int databaseId, int sessionId, int preparedStatementId, PreparedStatementParameters parameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(resultWriter);

        final Database<T, U> database = getDescriptorable(databaseId);

        return database.executePreparedStatement(sessionId, preparedStatementId, parameters, resultWriter);
    }

    @Override
    public long createPreparedStatementLargeObject(int databaseId, int sessionId, int preparedStatementId, long length) throws IOException {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Checks.isLongLengthAboveZero(length);

        final Database<T, U> database = getDescriptorable(databaseId);

        return database.createPreparedStatementLargeObjectPart(sessionId, preparedStatementId, length);
    }

    @Override
    public void storePreparedStatementLargeObjectPart(int databaseId, int sessionId, int preparedStatementId, long largeObjectRef, boolean isFinal, ByteBuffer byteBuffer,
            int offset, int length) throws IOException {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Checks.isLargeObjectRef(largeObjectRef);
        Objects.requireNonNull(byteBuffer);
        Checks.isIntOffset(offset);
        Checks.isNumBytes(length);

        final Database<T, U> database = getDescriptorable(databaseId);

        database.storePreparedStatementLargeObjectPart(sessionId, preparedStatementId, largeObjectRef, isFinal, byteBuffer, offset, length);
    }

    @Override
    public void freePreparedStatement(int databaseId, int sessionId, int preparedStatementId) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);

        getDescriptorable(databaseId).freePreparedStatement(sessionId, preparedStatementId);
    }

    @Override
    public IDatabaseOperations getDatabaseOperations(int databaseId) {

        Checks.isDatabaseId(databaseId);

        return getDescriptorable(databaseId);
    }
}
