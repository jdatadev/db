package dev.jdata.db.storage.backend.strings.file;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

import dev.jdata.db.storage.backend.strings.StoredStrings;
import dev.jdata.db.storage.backend.strings.StringsStorageBackend;

final class FileStringsStorageBackend implements StringsStorageBackend {

//    private final GZIPOutputStream outputStream;
    private final DataOutputStream dataOutputStream;

    FileStringsStorageBackend(Path filePath) throws IOException {

        this.dataOutputStream = new DataOutputStream(new GZIPOutputStream(Files.newOutputStream(filePath)));
    }

    @Override
    public void storeString(StoredStrings storedStrings) throws IOException {

        final int numStoredStrings = storedStrings.getNumStoredStrings();

        for (int i = 0; i < numStoredStrings; ++ i) {

            dataOutputStream.writeLong(storedStrings.getIdentifier(i));
            dataOutputStream.writeUTF(storedStrings.getString(i));
        }
    }
}
