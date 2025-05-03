package dev.jdata.db.storage.backend.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.storage.file.FileStorage;
import dev.jdata.db.utils.file.access.FileAccess;

public abstract class BaseStorageFile<T extends FileAccess> extends FileStorage implements Closeable {

    private final T file;

    protected BaseStorageFile(T file) {

        this.file = Objects.requireNonNull(file);
    }

    @Override
    public final void close() throws IOException {

        file.close();
    }

    protected final T getFile() {
        return file;
    }
}
