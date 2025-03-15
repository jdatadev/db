package dev.jdata.db.engine.database;

import java.nio.charset.Charset;

@Deprecated
public interface IDatabase {

    int addSession(Charset charset);

    void closeSession(int sessionId);
}
