package dev.jdata.db.engine.database;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Objects;

import dev.jdata.db.engine.database.Database.DatabaseState;
import dev.jdata.db.engine.descriptorables.BaseSingleTypeDescriptorables;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.sessions.IDatabaseSessionStatus;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.SQLParser.SQLString;
import dev.jdata.db.utils.adt.maps.ObjectMap;
import dev.jdata.db.utils.checks.Checks;

public final class Databases extends BaseSingleTypeDescriptorables<DatabaseState, Database> implements IDatabases {

    private final IStringCache stringCache;

    private final ObjectMap<String, Database> databaseByName;

    private final ObjectMap<SQLString, BaseSQLStatement> statementCache;

    public Databases(IStringCache stringCache, boolean cacheStatements) {
        super(Database[]::new);

        this.stringCache = Objects.requireNonNull(stringCache);

        this.databaseByName = new ObjectMap<>(0, String[]::new, Database[]::new);
        this.statementCache = cacheStatements ? new ObjectMap<>(0, SQLString[]::new, BaseSQLStatement[]::new) : null;
    }

    @Override
    public int getDatabase(CharSequence dbName) {

        Checks.isDBName(dbName);

        final int result;

        final String dbNameString = stringCache.getString(dbName);

        synchronized (databaseByName) {

            result = databaseByName.get(dbNameString).getDatabaseId();
        }

        return result;
    }

    @Override
    public int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters) {

        Checks.isDBName(dbName);
        Objects.requireNonNull(parameters);

        Database database;

        final String dbNameString = stringCache.getString(dbName);

        synchronized (databaseByName) {

            database = databaseByName.get(dbNameString);

            if (database == null) {

                database = addDescriptorable(parameters, p -> new Database(dbNameString, p, p.getAllocators(), p.getAllocators()));

                databaseByName.put(dbNameString, database);
            }
        }

        return database.getDatabaseId();
    }

    @Override
    public int createDatabase(CharSequence dbName, DatabaseParameters parameters) {

        Checks.isDBName(dbName);
        Objects.requireNonNull(parameters);

        Database database;

        final String dbNameString = stringCache.getString(dbName);

        synchronized (databaseByName) {

            database = databaseByName.get(dbNameString);

            if (database == null) {

                database = addDescriptorable(parameters, p -> new Database(dbNameString, p, p.getAllocators(), p.getAllocators()));

                databaseByName.put(dbNameString, database);
            }
        }

        return database.getDatabaseId();
    }

    @Override
    public void dropDatabase(int databaseId) {

        Checks.isDatabaseId(databaseId);

        final Database database = removeDescriptorable(databaseId);

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

        final Database database = getDescriptorable(databaseId);

        return database.getSessionCharset(sessionId);
    }

    @Override
    public void closeSession(int databaseId, int sessionId) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        final Database database = getDescriptorable(databaseId);

        database.closeSession(sessionId);
    }

    @Override
    public IDatabaseSessionStatus getDatabaseSessionStatus(int databaseId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int prepareStatement(int databaseId, int sessionId, BaseSQLStatement sqlStatement, SQLString sqlString) {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);

        if (statementCache != null) {

            statementCache.put(sqlString, sqlStatement);
        }

        final Database database = getDescriptorable(databaseId);

        return database.prepareStatement(sessionId, sqlStatement, sqlString);
    }

    @Override
    public <E extends Exception> long executePreparedStatement(int databaseId, int sessionId, int preparedStatementId, PreparedStatementParameters parameters,
            ExecuteSQLResultWriter<E> resultWriter) throws EvaluateException, E {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Objects.requireNonNull(parameters);
        Objects.requireNonNull(resultWriter);

        final Database database = getDescriptorable(databaseId);

        return database.executePreparedStatement(sessionId, preparedStatementId, parameters, resultWriter);
    }

    @Override
    public long createPreparedStatementLargeObject(int databaseId, int sessionId, int preparedStatementId, long length) throws IOException {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Checks.isPreparedStatementId(preparedStatementId);
        Checks.isLengthAboveZero(length);

        final Database database = getDescriptorable(databaseId);

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
        Checks.isOffset(offset);
        Checks.isNumBytes(length);

        final Database database = getDescriptorable(databaseId);

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
