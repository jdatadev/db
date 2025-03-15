package dev.jdata.db.engine.database;

public interface IDatabaseLookup {

    int getDatabase(CharSequence dbName);

    int createDatabase(CharSequence dbName, DatabaseParameters parameters);

    int getOrCreateDatabase(CharSequence dbName, DatabaseParameters parameters);

    void dropDatabase(int databaseId);
}
