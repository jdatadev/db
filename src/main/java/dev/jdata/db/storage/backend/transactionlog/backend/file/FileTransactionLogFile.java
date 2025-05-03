package dev.jdata.db.storage.backend.transactionlog.backend.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.storage.backend.file.BaseStorageFile;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.IFileSystemAccess.OpenMode;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.SequentialFileAccess;

public final class FileTransactionLogFile extends BaseStorageFile<SequentialFileAccess> {

    private final long transactionId;
    private final DataOutputStream dataOutputStream;

    public static FileTransactionLogFile read(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath) throws IOException {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(filePath);

        final FileTransactionLogFile result;

        try (DataInputStream dataInputStream = fileSystemAccess.openDataInputStream(filePath)) {

            final TransactionOperationCode operationCode = TransactionOperationCode.ofCode(dataInputStream.readByte());

            if (operationCode != TransactionOperationCode.START_TRANSACTION) {

                throw new IllegalStateException();
            }

            result = new FileTransactionLogFile(dataInputStream.readLong(), fileSystemAccess, filePath);
        }

        return result;
    }

    FileTransactionLogFile(long transactionId, IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath) throws IOException {
        super(createSequentialFileAccess(fileSystemAccess, filePath));

        this.transactionId = Checks.isTransactionId(transactionId);

        this.dataOutputStream = getFile().getDataOutputStream();
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

        getFile().sync(true);
    }

    private void writeOperationCode(TransactionOperationCode operationCode) throws IOException {

        dataOutputStream.writeByte(operationCode.getCode());
    }

    private void writeTransactionId(long transactionId) throws IOException {

        dataOutputStream.writeLong(transactionId);
    }

    private static SequentialFileAccess createSequentialFileAccess(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath) throws IOException {

        if (fileSystemAccess.exists(filePath)) {

            throw new IllegalArgumentException();
        }

        return fileSystemAccess.openSequentialFileAccess(filePath, OpenMode.APPEND_EXISTING);
    }
}
