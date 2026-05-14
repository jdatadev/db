package dev.jdata.db.engine.database;

public interface IDatabasesMutators {

    int createDatabase(CharSequence dbName, DatabaseParameters parameters);

    int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters);

    void dropDatabase(int databaseId);
}
