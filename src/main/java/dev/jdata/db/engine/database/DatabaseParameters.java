package dev.jdata.db.engine.database;

import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;

public final class DatabaseParameters implements IResettable {

    public static enum DatabaseStorageType {

        MEMORY,
        FILE
    }

    private DatabaseStorageType storageType;
    private CharSequence fileStoragePath;

    public void initialize(DatabaseStorageType storageType, CharSequence fileStoragePath) {

        this.storageType = Initializable.checkNotYetInitialized(this.storageType, storageType);
        this.fileStoragePath = storageType == DatabaseStorageType.FILE
                ? Initializable.checkNotYetInitialized(this.fileStoragePath, fileStoragePath)
                : Initializable.checkNotYetInitializedToNull(this.fileStoragePath);
    }

    @Override
    public void reset() {

        this.storageType = Initializable.checkResettable(storageType);
        this.fileStoragePath = storageType == DatabaseStorageType.FILE
                ? Initializable.checkResettable(fileStoragePath)
                : Initializable.checkNotYetInitializedToNull(fileStoragePath);
    }
}
