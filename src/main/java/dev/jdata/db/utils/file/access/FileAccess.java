package dev.jdata.db.utils.file.access;

import java.io.Closeable;
import java.io.IOException;

public interface FileAccess extends Closeable {

    void sync(boolean syncMetaData) throws IOException;
}
