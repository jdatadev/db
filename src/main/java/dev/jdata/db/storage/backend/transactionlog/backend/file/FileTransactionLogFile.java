package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.storage.backend.file.BaseStorageFile;
import dev.jdata.db.utils.checks.Checks;

public final class FileTransactionLogFile extends BaseStorageFile<FileOutputStream> {

    private final long transactionId;
    private final DataOutputStream dataOutputStream;

    public static FileTransactionLogFile read(Path path) throws IOException {

        Objects.requireNonNull(path);

        final FileTransactionLogFile result;

        try (DataInputStream dataInputStream = new DataInputStream(Files.newInputStream(path))) {

            final TransactionOperationCode operationCode = TransactionOperationCode.ofCode(dataInputStream.readByte());

            if (operationCode != TransactionOperationCode.START_TRANSACTION) {

                throw new IllegalStateException();
            }

            result = new FileTransactionLogFile(dataInputStream.readLong(), path);
        }

        return result;
    }

    public FileTransactionLogFile(long transactionId, Path path) throws IOException {
        super(createFileOutputStream(path));

        this.transactionId = Checks.isTransactionId(transactionId);

        this.dataOutputStream = new DataOutputStream(getFile());
    }

    void append(long transactionId, TransactionOperationCode operationCode, byte[] buffer, int numBytes) throws IOException {

        Checks.isTransactionId(transactionId);
        Checks.areEqual(this.transactionId, transactionId);
        Objects.requireNonNull(operationCode);
        Objects.requireNonNull(buffer);
        Checks.isNumBytes(numBytes);

        writeOperationCode(operationCode);

        dataOutputStream.write(buffer, 0, numBytes);
    }

    void writeStartTransaction(long transactionId) throws IOException {

        Checks.isTransactionId(transactionId);
        Checks.areEqual(this.transactionId, transactionId);

        writeOperationCode(TransactionOperationCode.START_TRANSACTION);

        writeTransactionId(transactionId);
    }

    void writeCommitTransaction(long transactionId) throws IOException {

        Checks.isTransactionId(transactionId);
        Checks.areEqual(this.transactionId, transactionId);

        writeOperationCode(TransactionOperationCode.COMMIT_TRANSACTION);

        writeTransactionId(transactionId);

        dataOutputStream.flush();

        getFile().getChannel().force(true);
    }

    private void writeOperationCode(TransactionOperationCode operationCode) throws IOException {

        dataOutputStream.writeByte(operationCode.getCode());
    }

    private void writeTransactionId(long transactionId) throws IOException {

        dataOutputStream.writeLong(transactionId);
    }

    private static FileOutputStream createFileOutputStream(Path path) throws IOException {

        if (Files.exists(path)) {

            throw new IllegalArgumentException();
        }

        return new FileOutputStream(path.toFile());
    }
}
