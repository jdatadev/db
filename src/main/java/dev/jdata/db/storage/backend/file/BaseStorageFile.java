package dev.jdata.db.storage.backend.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

public abstract class BaseStorageFile<T extends Closeable> implements Closeable {

    private final T file;

    protected BaseStorageFile(T file) {

        this.file = Objects.requireNonNull(file);
    }

    protected final T getFile() {
        return file;
    }

    @Override
    public final void close() throws IOException {

        file.close();
    }
}
