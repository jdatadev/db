package dev.jdata.db.engine.database;

import dev.jdata.db.engine.database.operations.IDatabaseOperations;

@Deprecated
public interface IDatabaseOperationsGetter {

    IDatabaseOperations getDatabaseOperations(int databaseId);
}
