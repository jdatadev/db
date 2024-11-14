package dev.jdata.db.storage.backend.strings;

import java.io.IOException;

public interface StringsStorageBackend {

     void storeString(StoredStrings storedStrings) throws IOException;
}
