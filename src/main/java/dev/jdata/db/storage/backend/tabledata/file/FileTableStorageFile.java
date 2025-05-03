package dev.jdata.db.storage.backend.tabledata.file;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import dev.jdata.db.data.RowDataNumBitsGetter;
import dev.jdata.db.storage.backend.file.BaseStorageFile;
import dev.jdata.db.storage.backend.tabledata.file.StorageTableFileSchema.StorageTableFileSchemaGetters;
import dev.jdata.db.utils.allocators.IByteArrayAllocator;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.file.access.IFilePath;
import dev.jdata.db.utils.file.access.IFileSystemAccess.OpenMode;
import dev.jdata.db.utils.file.access.RandomFileAccess;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.scalars.Integers;

public final class FileTableStorageFile extends BaseStorageFile<RandomFileAccess> implements PrintDebug {

    private static final boolean DEBUG = Boolean.TRUE;

    private static final boolean OPTIMIZE_LAST_BYTE = Boolean.TRUE;

    static final String FILE_NAME_PREFIX = "tabledata";

    static FileTableStorageFile addNewFile(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath, int sequenceNo, StorageTableFileSchema storageTableFileSchema,
            long startRowId) throws IOException {

        if (parseSequenceNo(filePath.getFileName()) != sequenceNo) {

            throw new IllegalArgumentException();
        }

        final RandomFileAccess randomFileAccess = createRandomFileAccess(fileSystemAccess, filePath, OpenMode.READ_WRITE_CREATE_FAIL_IF_EXISTS);

        storageTableFileSchema.write(randomFileAccess);

        randomFileAccess.writeLong(startRowId);

        return new FileTableStorageFile(sequenceNo, storageTableFileSchema, randomFileAccess, startRowId, 0L);
    }

    static FileTableStorageFile openExistingFile(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath,
            StorageTableFileSchemaGetters storageTableFileSchemaGetters) throws IOException {

        final RandomFileAccess randomFileAccess = createRandomFileAccess(fileSystemAccess, filePath, OpenMode.READ_WRITE_EXISTING);

        final StorageTableFileSchema storageTableSchema = StorageTableFileSchema.read(randomFileAccess, storageTableFileSchemaGetters);

        final long startRow = randomFileAccess.readLong();

        final long tableFileOffset = randomFileAccess.getFilePointer();
        final long tableFileLength = randomFileAccess.length();

        final long tableFileDataLength = tableFileLength - tableFileOffset;

        final int numBitsPerByte = 8;

        final long tableFileDataBits = tableFileDataLength * numBitsPerByte;

        final int totalNumRowBits = storageTableSchema.getTotalNumRowBits();

        final long numRows = tableFileDataBits / totalNumRowBits;

        final long numLeftOverBits = totalNumRowBits - (numRows * totalNumRowBits);

        if (numLeftOverBits >= numBitsPerByte) {

            throw new IllegalStateException();
        }

        return new FileTableStorageFile(filePath, storageTableSchema, randomFileAccess, startRow, randomFileAccess.length());
    }

    static FileTableStorageFile openExistingFileFromStorageTableFileSchema(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath, int sequenceNo,
            StorageTableFileSchema storageTableFileSchema, long startRowId) throws IOException {

        final RandomFileAccess randomFileAccess = createRandomFileAccess(fileSystemAccess, filePath, OpenMode.READ_WRITE_EXISTING);

        final FileTableStorageFile result;

        boolean ok = false;

        try {
            randomFileAccess.seek(storageTableFileSchema.getNumHeaderBytes());

            if (randomFileAccess.readLong() != sequenceNo) {

                throw new IllegalArgumentException();
            }

            result = new FileTableStorageFile(sequenceNo, storageTableFileSchema, randomFileAccess, startRowId, randomFileAccess.length());

            ok = true;
        }
        finally {

            if (!ok) {

                randomFileAccess.close();
            }
        }

        return result;
    }

    static String constructTableFileName(long sequenceNo) {

        Checks.isSequenceNo(sequenceNo);

        return constructTableFileName(FILE_NAME_PREFIX, sequenceNo);
    }

    private final int sequenceNo;
    private final StorageTableFileSchema storageTableFileSchema;
    private final long startRowId;

    private final int numHeaderBytes;
    private final int numBitsPerRow;

    private long fileOffset;
    private long fileLength;

    private long numRows;
    private long lastRowId;

//  private int numLeftOverBits;

    private byte lastByte;

    private FileTableStorageFile(IFilePath filePath, StorageTableFileSchema storageTableFileSchema, RandomFileAccess randomFileAccess, long startRowId, long fileLength)
            throws IOException {
        this(parseSequenceNo(filePath.getFileName()), storageTableFileSchema, randomFileAccess, startRowId, fileLength);
    }

    private FileTableStorageFile(int sequenceNo, StorageTableFileSchema storageTableFileSchema, RandomFileAccess randomFileAccess, long startRowId, long fileLength)
            throws IOException {
        super(randomFileAccess);

        Checks.isSequenceNo(sequenceNo);
        Objects.requireNonNull(storageTableFileSchema);
        Objects.requireNonNull(randomFileAccess);
        Checks.isRowId(startRowId);
        Checks.isNotNegative(fileLength);

        if (DEBUG) {

            enter(b -> b.add("sequenceNo", sequenceNo).add("storageTableFileSchema", storageTableFileSchema).add("randomFileAccess", randomFileAccess)
                    .add("startRowId", startRowId));
        }

        this.sequenceNo = Checks.isSequenceNo(sequenceNo);
        this.storageTableFileSchema = Objects.requireNonNull(storageTableFileSchema);
        this.startRowId = Checks.isRowId(startRowId);

        this.numHeaderBytes = storageTableFileSchema.getNumHeaderBytes() + 8;
        this.numBitsPerRow = storageTableFileSchema.getTotalNumRowBits();

/*
        this.numRows = Checks.isNotNegative(numRows);
        this.numLeftOverBits = Checks.isNumByteBitsOrZero(numLeftOverBits);
*/

        this.fileLength = fileLength;

        if (fileLength != 0L) {

            if (OPTIMIZE_LAST_BYTE) {

                final long atLastByte = fileLength - 1;

                randomFileAccess.seek(atLastByte);

                this.fileOffset = atLastByte;

                this.lastByte = randomFileAccess.readByte();

                ++ this.fileOffset;
            }
            else {
                final long endOfFile = fileLength;

                randomFileAccess.seek(fileOffset);

                this.fileOffset = endOfFile;
            }
        }
        else {
            this.fileOffset = 0L;
        }

        this.numRows = computeNumRows(fileLength, numBitsPerRow);
        this.lastRowId = computeLastRowId(startRowId, numRows);

        if (DEBUG) {

            exit(b -> b.add("this.numBitsPerRow", numBitsPerRow).add("this.fileLength", fileLength).binary("this.lastByte", lastByte).add("this.numRows", numRows)
                    .add("this.lastRowId", lastRowId));
        }
    }

    public int getSequenceNo() {
        return sequenceNo;
    }

    public StorageTableFileSchema getStorageTableFileSchema() {
        return storageTableFileSchema;
    }

    public boolean hasLeftOverBits() {

        return computeNumLeftOverBits() != 0;
    }

    public int computeNumLeftOverBits() {

        return fileLength != 0L ? (int)((fileLength * 8) % numBitsPerRow) : 0;
    }

    public byte readLastByte() throws IOException {

        if (DEBUG) {

            enter(b -> b.binary("this.lastByte", lastByte).add("this.fileOffset", fileOffset));
        }

        if (fileLength != 0L) {

            throw new IllegalStateException();
        }

        final byte result;

        if (OPTIMIZE_LAST_BYTE) {

            result = lastByte;
        }
        else {
            final long atLastByte = fileLength - 1;

            final RandomFileAccess randomFileAccess = getFile();

            randomFileAccess.seek(atLastByte);

            this.fileOffset = atLastByte;

            result = randomFileAccess.readByte();

            ++ this.fileOffset;
        }

        if (DEBUG) {

            exit(result, b -> b.add("this.fileOffset", fileOffset));
        }

        return result;
    }

    public void append(int numAppendedRows, byte[] rowByteBuffer, int numBytes, boolean rewindOneByte) throws IOException {

        if (DEBUG) {

            enter(b -> b.add("numAppendedRows", numAppendedRows).add("rowByteBuffer.length", rowByteBuffer.length).add("numBytes", numBytes)
                    .add("rewindOneByte", rewindOneByte).add("this.fileLength", fileLength).add("this.fileOffset", fileOffset));
        }

        final RandomFileAccess randomFileAccess = getFile();

        if (fileLength == 0L) {

            Assertions.isFalse(rewindOneByte);
        }
        else {
            final long seekPosition;

            if (rewindOneByte) {

                Assertions.isAboveZero(fileLength);

                seekPosition = fileLength - 1;
            }
            else {
                seekPosition = fileLength;
            }

            if (fileOffset != seekPosition) {

                randomFileAccess.seek(seekPosition);

                this.fileOffset = seekPosition;
            }
        }

        randomFileAccess.write(rowByteBuffer, 0, numBytes);

        this.fileOffset += numBytes;
        this.fileLength = fileOffset;

        if (OPTIMIZE_LAST_BYTE) {

            this.lastByte = rowByteBuffer[numBytes - 1];
        }

        this.numRows += numAppendedRows;

        if (numRows != computeNumRows(fileLength, numBitsPerRow)) {

            throw new IllegalStateException();
        }

        this.lastRowId = computeLastRowId(startRowId, numRows);

        if (DEBUG) {

            exit(b -> b.add("this.fileLength", fileLength).add("this.fileOffset", fileOffset).add("this.startRowId", startRowId).add("this.numRows", numRows)
                    .add("this.lastRowId", lastRowId));
        }
    }

    private void checkContainsRowId(long rowId) {

        if (rowId < startRowId) {

            throw new IllegalArgumentException();
        }
        else if (rowId > lastRowId) {

            throw new IllegalArgumentException();
        }
    }

    public long getRowBitOffset(long rowId) {

        checkContainsRowId(rowId);

        if (DEBUG) {

            enter(b -> b.add("rowId", rowId));
        }

        final long result = ((rowId - startRowId) * numBitsPerRow);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    public byte readByteAtRowBitOffset(long rowBitOffset) throws IOException {

        Checks.isOffset(rowBitOffset);

        if (DEBUG) {

            enter(b -> b.add("rowBitOffset", rowBitOffset).add("this.fileOffset", fileOffset));
        }

        final long byteOffset = numHeaderBytes + (rowBitOffset >>> 3);

        if (byteOffset >= fileLength) {

            throw new IllegalArgumentException();
        }

        final RandomFileAccess randomFileAccess = getFile();

        randomFileAccess.seek(byteOffset);

        this.fileOffset = byteOffset;

        final byte result = randomFileAccess.readByte();

        ++ fileOffset;

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("byteOffset", byteOffset).add("this.fileOffset", fileOffset));
        }

        return result;
    }

    public void update(long fileByteOffset, byte[] rowByteBuffer, int numBytes) throws IOException {

        Checks.isOffset(fileByteOffset);
        Objects.requireNonNull(rowByteBuffer);
        Checks.isNumBytes(numBytes);

        if (DEBUG) {

            enter(b -> b.add("fileByteOffset", fileByteOffset).add("rowByteBuffer.length", rowByteBuffer.length).add("numBytes", numBytes).add("this.fileLength", fileLength));
        }

        if (fileByteOffset + numBytes > fileLength) {

            throw new IllegalArgumentException();
        }

        final RandomFileAccess randomFileAccess = getFile();

        randomFileAccess.seek(fileByteOffset);

        this.fileOffset = fileByteOffset;

        randomFileAccess.write(rowByteBuffer, 0, numBytes);

        this.fileOffset += numBytes;

        if (OPTIMIZE_LAST_BYTE && fileOffset == fileLength) {

            this.lastByte = rowByteBuffer[numBytes - 1];
        }

        if (DEBUG) {

            exit(b -> b.add("this.fileOffset", fileOffset));
        }
    }

    long getStartRowId() {
        return startRowId;
    }

    long getNumRows() {
        return numRows;
    }

    long getLastRowId() {

        return lastRowId;
    }

    String getFileName() {

        return constructTableFileName(FILE_NAME_PREFIX, sequenceNo);
    }

    void expandTo(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath, StorageTableFileSchema updatedStorageTableFileSchema,
            IByteArrayAllocator byteBufferAllocator) throws IOException {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(updatedStorageTableFileSchema);
        Objects.requireNonNull(byteBufferAllocator);

        if (DEBUG) {

            enter(b -> b.add("fileSystemAccess", fileSystemAccess).add("filePath", filePath).add("updatedStorageTableFileSchema", updatedStorageTableFileSchema)
                    .add("byteBufferAllocator", byteBufferAllocator).add("this.numRows", numRows));
        }

        try (DataOutputStream dataOutputStream = fileSystemAccess.openDataOutputStream(filePath)) {

            updatedStorageTableFileSchema.write(dataOutputStream);

            if (numRows > 0L) {

                final int maxBytes = 10 * 1000 * 1000;

                final int totalNumRowBytes = updatedStorageTableFileSchema.getTotalNumRowBytes();

                final long numTotalBytes = totalNumRowBytes * numRows;

                final long maxBatchRows = numTotalBytes > maxBytes
                        ? maxBytes / totalNumRowBytes
                        : numRows;

                final RandomFileAccess randomFileAccess = getFile();

                randomFileAccess.seek(storageTableFileSchema.getNumHeaderBytes());

                final int outputRowByteBufferCapacity = Integers.checkUnsignedLongToUnsignedInt(maxBatchRows * totalNumRowBytes);

                final int inputRowByteBufferCapacity = outputRowByteBufferCapacity;

                final byte[] inputTempByteBuffer = byteBufferAllocator.allocateByteArray(inputRowByteBufferCapacity);

                try {
                    final byte[] outputTempByteBuffer = byteBufferAllocator.allocateByteArray(outputRowByteBufferCapacity);

                    try {
                        copyFileContents(numRows, maxBatchRows, storageTableFileSchema, randomFileAccess, inputTempByteBuffer, updatedStorageTableFileSchema, dataOutputStream,
                                outputTempByteBuffer);
                    }
                    finally {

                        byteBufferAllocator.freeByteArray(outputTempByteBuffer);
                    }
                }
                finally {

                    byteBufferAllocator.freeByteArray(inputTempByteBuffer);
                }
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    private static final class CopyRowDataNumBits implements RowDataNumBitsGetter {

        private final StorageTableFileSchema storageTableFileSchema;
        private final byte[] inputBuffer;

        private long nullValueBitmapBitOffset;

        CopyRowDataNumBits(StorageTableFileSchema storageTableFileSchema, byte[] inputBuffer) {

            this.storageTableFileSchema = Objects.requireNonNull(storageTableFileSchema);
            this.inputBuffer = Objects.requireNonNull(inputBuffer);
        }

        void setNullValueBitmapBitOffset(long nullValueBitmapBitOffset) {

            this.nullValueBitmapBitOffset = Checks.isBufferBitsOffset(nullValueBitmapBitOffset);
        }

        @Override
        public int getNumColumns() {

            return storageTableFileSchema.getNumColumns();
        }

        @Override
        public boolean isNull(int columnIndex) {

            final int nullValueBitmapIndex = storageTableFileSchema.getNullValueBitmapIndex(columnIndex);

            return nullValueBitmapIndex != StorageTableFileSchema.NO_NULL_VALUE_BITMAP_INDEX
                    ? BitBufferUtil.isBitSet(inputBuffer, nullValueBitmapBitOffset + nullValueBitmapIndex)
                    : false;
        }

        @Override
        public int getNumBits(int columnIndex) {

            return storageTableFileSchema.getStorageColumn(columnIndex).getNumBits();
        }
    }

    private static void copyFileContents(long numRows, long maxBatchRows, StorageTableFileSchema inputStorageTableFileSchema, RandomFileAccess inputRandomFileAccess,
            byte[] inputTempByteBuffer, StorageTableFileSchema outputStorageTableFileSchema, DataOutput dataOutput, byte[] outputTempByteBuffer) throws IOException {

        final int numInputRowIdBits = inputStorageTableFileSchema.getNumRowIdBits();
        final int numInputDeleteMarkerBits = inputStorageTableFileSchema.getNumDeleteMarkerBits();
        final int numInputTransactionIdBits = inputStorageTableFileSchema.getNumTransactionIdBits();
        final int numInputNullValueBitmapBits = inputStorageTableFileSchema.getNumNullValueBitMapBits();
        final int numInputRowDataBits = inputStorageTableFileSchema.getNumRowDataBits();

        final int numOutputRowIdBits = outputStorageTableFileSchema.getNumRowIdBits();
        final int numOutputDeleteMarkerBits = outputStorageTableFileSchema.getNumDeleteMarkerBits();
        final int numOutputTransactionIdBits = outputStorageTableFileSchema.getNumTransactionIdBits();
        final int numOutputNullValueBitmapBits = outputStorageTableFileSchema.getNumNullValueBitMapBits();
        final int numOutputRowDataBits = outputStorageTableFileSchema.getNumRowDataBits();

        long remainingRows = numRows;

        final CopyRowDataNumBits copyRowDataNumBits = new CopyRowDataNumBits(inputStorageTableFileSchema, inputTempByteBuffer);

        do {
            final int numInputBytes = inputRandomFileAccess.read(inputTempByteBuffer);

            long inputByteBufferBitOffset = 0;
            long outputByteBufferBitOffset = 0;

            for (long i = 0; i < maxBatchRows && remainingRows != 0L; ++ i, -- remainingRows) {

                BitBufferUtil.copyBits(inputTempByteBuffer, inputByteBufferBitOffset, numInputRowIdBits, outputTempByteBuffer, outputByteBufferBitOffset, numOutputRowIdBits);

                inputByteBufferBitOffset += numInputRowIdBits;
                outputByteBufferBitOffset += numOutputRowIdBits;

                BitBufferUtil.copyBits(inputTempByteBuffer, inputByteBufferBitOffset, numInputDeleteMarkerBits, outputTempByteBuffer, outputByteBufferBitOffset,
                        numOutputDeleteMarkerBits);

                inputByteBufferBitOffset += numInputDeleteMarkerBits;
                outputByteBufferBitOffset += numOutputDeleteMarkerBits;

                BitBufferUtil.copyBits(inputTempByteBuffer, inputByteBufferBitOffset, numInputTransactionIdBits, outputTempByteBuffer, outputByteBufferBitOffset,
                        numOutputTransactionIdBits);

                inputByteBufferBitOffset += numInputTransactionIdBits;
                outputByteBufferBitOffset += numOutputTransactionIdBits;

                BitBufferUtil.copyBits(inputTempByteBuffer, inputByteBufferBitOffset, numInputNullValueBitmapBits, outputTempByteBuffer, outputByteBufferBitOffset,
                        numOutputNullValueBitmapBits);

                copyRowDataNumBits.setNullValueBitmapBitOffset(inputByteBufferBitOffset);

                inputByteBufferBitOffset += numInputNullValueBitmapBits;
                outputByteBufferBitOffset += numOutputNullValueBitmapBits;

                RowBitsUtil.addRowData(outputStorageTableFileSchema, inputTempByteBuffer, outputByteBufferBitOffset, outputTempByteBuffer, numInputBytes, copyRowDataNumBits);

                inputByteBufferBitOffset += numInputRowDataBits;
                outputByteBufferBitOffset += numOutputRowDataBits;
            }

            final int numBytes = Integers.checkUnsignedLongToUnsignedInt(outputByteBufferBitOffset >>> 3);

            dataOutput.write(outputTempByteBuffer, 0, numBytes);

            final int numLeftoverBits = BitBufferUtil.numLeftoverBits(outputByteBufferBitOffset);

            if (numLeftoverBits > 0) {

                outputTempByteBuffer[0] = outputTempByteBuffer[numBytes];

                outputByteBufferBitOffset = numLeftoverBits;
            }
            else {
                outputByteBufferBitOffset = 0L;
            }

        } while (remainingRows != 0L);
    }

    private static RandomFileAccess createRandomFileAccess(IRelativeFileSystemAccess fileSystemAccess, RelativeFilePath filePath, OpenMode openMode) throws IOException {

        Objects.requireNonNull(fileSystemAccess);
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(openMode);

        return fileSystemAccess.openRandomFileAccess(filePath, openMode);
    }

    private static long computeNumRows(long fileLength, int numBitsPerRow) {

        return (fileLength * 8) / numBitsPerRow;
    }

    private static long computeLastRowId(long startRowId, long numRows) {

        return startRowId + numRows - 1;
    }

    private static int parseSequenceNo(String fileName) {

        return parseSequenceNo(fileName, FILE_NAME_PREFIX);
    }
}
