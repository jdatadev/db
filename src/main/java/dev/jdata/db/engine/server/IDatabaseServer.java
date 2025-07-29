package dev.jdata.db.engine.server;

import dev.jdata.db.engine.database.IDatabaseLookup;
import dev.jdata.db.engine.database.IDatabasePreparedStatements;
import dev.jdata.db.engine.database.IDatabaseSessions;
import dev.jdata.db.engine.database.operations.IDatabaseOperations;

public interface IDatabaseServer extends IDatabaseLookup, IDatabaseSessions, IDatabasePreparedStatements {

    @Deprecated
    IDatabaseOperations getDatabaseOperations(int databaseId);
}
