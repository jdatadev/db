package dev.jdata.db.storage.backend.tabledata.file;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import dev.jdata.db.storage.backend.RowDataNumBitsGetter;
import dev.jdata.db.storage.backend.StorageTableFileSchema;
import dev.jdata.db.storage.backend.file.BaseStorageFile;
import dev.jdata.db.utils.bits.BitBufferUtil;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.paths.PathUtil;
import dev.jdata.db.utils.scalars.Integers;

public final class FileTableStorageFile extends BaseStorageFile<RandomAccessFile> {

    private static final boolean OPTIMIZE_LAST_BYTE = Boolean.TRUE;

    private final int sequenceNo;
    private final StorageTableFileSchema storageTableFileSchema;
    private final long startRowId;

    private final int numBitsPerRow;

    private long fileOffset;
    private long fileLength;

    private long numRows;
    private long lastRowId;

//  private int numLeftOverBits;

    private byte lastByte;

    FileTableStorageFile(Path path, StorageTableFileSchema storageTableFileSchema, RandomAccessFile randomAccessFile, long startRowId) throws IOException {
        this(FileTableStorageFiles.parseSequenceNo(PathUtil.getFileName(path)), storageTableFileSchema, randomAccessFile, startRowId);
    }

    FileTableStorageFile(int sequenceNo, StorageTableFileSchema storageTableFileSchema, RandomAccessFile randomAccessFile, long startRowId) throws IOException {
        super(randomAccessFile);

        this.sequenceNo = Checks.isSequenceNo(sequenceNo);
        this.storageTableFileSchema = Objects.requireNonNull(storageTableFileSchema);
        this.startRowId = Checks.isRowId(startRowId);

        this.numBitsPerRow = storageTableFileSchema.getTotalNumRowBits();

/*
        this.numRows = Checks.isNotNegative(numRows);
        this.numLeftOverBits = Checks.isNumByteBitsOrZero(numLeftOverBits);
*/

        this.fileLength = randomAccessFile.length();

        if (OPTIMIZE_LAST_BYTE && fileLength != 0L) {

            final long atLastByte = fileLength - 1;

            randomAccessFile.seek(atLastByte);

            this.fileOffset = atLastByte;

            this.lastByte = randomAccessFile.readByte();

            ++ this.fileOffset;
        }
        else {
            final long endOfFile = fileLength;

            randomAccessFile.seek(fileOffset);

            this.fileOffset = endOfFile;
        }

        this.numRows = computeNumRows(fileLength, numBitsPerRow);
        this.lastRowId = computeLastRowId(startRowId, numRows);
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

        if (fileLength != 0L) {

            throw new IllegalStateException();
        }

        final byte result;

        if (OPTIMIZE_LAST_BYTE) {

            result = lastByte;
        }
        else {
            final long atLastByte = fileLength - 1;

            final RandomAccessFile randomAccessFile = getFile();

            randomAccessFile.seek(atLastByte);

            this.fileOffset = atLastByte;

            result = randomAccessFile.readByte();

            ++ this.fileOffset;
        }

        return result;
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

    public void append(int numAppendedRows, byte[] rowByteBuffer, int numBytes, boolean rewindOneByte) throws IOException {

        final long seekPosition;

        if (rewindOneByte) {

            if (fileLength == 0L) {

                throw new IllegalStateException();
            }

            seekPosition = fileLength - 1;
        }
        else {
            seekPosition = fileLength;
        }

        final RandomAccessFile randomAccessFile = getFile();

        randomAccessFile.seek(seekPosition);

        this.fileOffset = seekPosition;

        randomAccessFile.write(rowByteBuffer, 0, numBytes);

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

        return ((rowId - startRowId) * numBitsPerRow);
    }

    public byte readByteAtRowBitOffset(long rowBitOffset) throws IOException {

        Checks.isOffset(rowBitOffset);

        final long byteOffset = storageTableFileSchema.getNumHeaderBytes() + (numBitsPerRow / 8);

        if (byteOffset >= fileLength) {

            throw new IllegalArgumentException();
        }

        final RandomAccessFile randomAccessFile = getFile();

        randomAccessFile.seek(byteOffset);

        this.fileOffset = byteOffset;

        final byte result = randomAccessFile.readByte();

        ++ fileOffset;

        return result;
    }

    public void update(long fileByteOffset, byte[] rowByteBuffer, int numBytes) throws IOException {

        Checks.isOffset(fileByteOffset);
        Objects.requireNonNull(rowByteBuffer);
        Checks.isNumBytes(numBytes);

        if (fileByteOffset + numBytes > fileLength) {

            throw new IllegalArgumentException();
        }

        final RandomAccessFile randomAccessFile = getFile();

        randomAccessFile.seek(fileByteOffset);

        this.fileOffset = fileByteOffset;

        randomAccessFile.write(rowByteBuffer, 0, numBytes);

        this.fileOffset += numBytes;

        if (OPTIMIZE_LAST_BYTE && fileOffset == fileLength) {

            this.lastByte = rowByteBuffer[numBytes - 1];
        }
    }

    void expandTo(Path path, StorageTableFileSchema updatedStorageTableFileSchema, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Objects.requireNonNull(path);
        Objects.requireNonNull(updatedStorageTableFileSchema);
        Objects.requireNonNull(byteBufferAllocator);

        try (DataOutputStream dataOutputStream = new DataOutputStream(Files.newOutputStream(path))) {

            updatedStorageTableFileSchema.write(dataOutputStream);

            if (numRows > 0L) {

                final int maxBytes = 10 * 1000 * 1000;

                final int totalNumRowBytes = updatedStorageTableFileSchema.getTotalNumRowBytes();

                final long numTotalBytes = totalNumRowBytes * numRows;

                final long maxBatchRows = numTotalBytes > maxBytes
                        ? maxBytes / totalNumRowBytes
                        : numRows;

                final RandomAccessFile randomAccessFile = getFile();

                randomAccessFile.seek(storageTableFileSchema.getNumHeaderBytes());

                final int outputRowBufferCapacity = Integers.checkUnsignedLongToUnsignedInt(maxBatchRows * totalNumRowBytes);

                final int inputRowBufferCapacity = outputRowBufferCapacity;

                final byte[] inputTempBuffer = byteBufferAllocator.allocate(inputRowBufferCapacity);

                try {
                    final byte[] outputTempBuffer = byteBufferAllocator.allocate(outputRowBufferCapacity);

                    try {
                        copyFileContents(numRows, maxBatchRows, storageTableFileSchema, randomAccessFile, inputTempBuffer, updatedStorageTableFileSchema, dataOutputStream,
                                outputTempBuffer);
                    }
                    finally {

                        byteBufferAllocator.free(outputTempBuffer);
                    }
                }
                finally {

                    byteBufferAllocator.free(inputTempBuffer);
                }
            }
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

    private static void copyFileContents(long numRows, long maxBatchRows, StorageTableFileSchema inputStorageTableFileSchema, RandomAccessFile inputRandomAccessFile,
            byte[] inputTempBuffer, StorageTableFileSchema outputStorageTableFileSchema, DataOutput dataOutput, byte[] outputTempBuffer) throws IOException {

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

        final CopyRowDataNumBits copyRowDataNumBits = new CopyRowDataNumBits(inputStorageTableFileSchema, inputTempBuffer);

        do {
            final int numInputBytes = inputRandomAccessFile.read(inputTempBuffer);

            long inputByteBufferBitOffset = 0;
            long outputByteBufferBitOffset = 0;

            for (long i = 0; i < maxBatchRows && remainingRows != 0L; ++ i, -- remainingRows) {

                BitBufferUtil.copyBits(inputTempBuffer, inputByteBufferBitOffset, numInputRowIdBits, outputTempBuffer, outputByteBufferBitOffset, numOutputRowIdBits);

                inputByteBufferBitOffset += numInputRowIdBits;
                outputByteBufferBitOffset += numOutputRowIdBits;

                BitBufferUtil.copyBits(inputTempBuffer, inputByteBufferBitOffset, numInputDeleteMarkerBits, outputTempBuffer, outputByteBufferBitOffset,
                        numOutputDeleteMarkerBits);

                inputByteBufferBitOffset += numInputDeleteMarkerBits;
                outputByteBufferBitOffset += numOutputDeleteMarkerBits;

                BitBufferUtil.copyBits(inputTempBuffer, inputByteBufferBitOffset, numInputTransactionIdBits, outputTempBuffer, outputByteBufferBitOffset,
                        numOutputTransactionIdBits);

                inputByteBufferBitOffset += numInputTransactionIdBits;
                outputByteBufferBitOffset += numOutputTransactionIdBits;

                BitBufferUtil.copyBits(inputTempBuffer, inputByteBufferBitOffset, numInputNullValueBitmapBits, outputTempBuffer, outputByteBufferBitOffset,
                        numOutputNullValueBitmapBits);

                copyRowDataNumBits.setNullValueBitmapBitOffset(inputByteBufferBitOffset);

                inputByteBufferBitOffset += numInputNullValueBitmapBits;
                outputByteBufferBitOffset += numOutputNullValueBitmapBits;

                RowBitsUtil.addRowData(outputStorageTableFileSchema, inputTempBuffer, outputByteBufferBitOffset, outputTempBuffer, numInputBytes, copyRowDataNumBits);

                inputByteBufferBitOffset += numInputRowDataBits;
                outputByteBufferBitOffset += numOutputRowDataBits;
            }

            final int numBytes = Integers.checkUnsignedLongToUnsignedInt(outputByteBufferBitOffset / 8);

            dataOutput.write(outputTempBuffer, 0, numBytes);

            final int numLeftoverBits = BitBufferUtil.numLeftoverBits(outputByteBufferBitOffset);

            if (numLeftoverBits > 0) {

                outputTempBuffer[0] = outputTempBuffer[numBytes];

                outputByteBufferBitOffset = numLeftoverBits;
            }
            else {
                outputByteBufferBitOffset = 0L;
            }

        } while (remainingRows != 0L);
    }

    private static long computeNumRows(long fileLength, int numBitsPerRow) {

        return (fileLength * 8) / numBitsPerRow;
    }

    private static long computeLastRowId(long startRowId, long numRows) {

        return startRowId + numRows - 1;
    }
}
