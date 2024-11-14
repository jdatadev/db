package dev.jdata.db.utils.file.access;

import java.io.IOException;
import java.nio.channels.FileChannel;

abstract class BaseFileAccess implements FileAccess {

    abstract FileChannel getFileChannel();

    @Override
    public final void sync(boolean syncMetaData) throws IOException {

        getFileChannel().force(true);
    }
}
