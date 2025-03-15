package dev.jdata.db.engine.server;

import dev.jdata.db.engine.database.IDatabaseOperations;
import dev.jdata.db.engine.database.IDatabaseLookup;
import dev.jdata.db.engine.database.IDatabasePreparedStatements;
import dev.jdata.db.engine.database.IDatabaseSessions;

public interface IDatabaseServer extends IDatabaseLookup, IDatabaseSessions, IDatabasePreparedStatements {

    @Deprecated
    IDatabaseOperations getDatabaseOperations(int databaseId);
}
