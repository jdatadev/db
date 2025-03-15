package dev.jdata.db.engine.database;

import java.nio.charset.Charset;

import dev.jdata.db.engine.sessions.IDatabaseSessionStatus;

public interface IDatabaseSessions {

    int addSession(int databaseId, Charset charset);
    void closeSession(int databaseId, int sessionId);

    Charset getSessionCharset(int databaseId, int sessionId);

    @Deprecated
    IDatabaseSessionStatus getDatabaseSessionStatus(int databaseId);
}
