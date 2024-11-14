package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.storage.backend.StorageTableFileSchema;
import dev.jdata.db.storage.backend.file.BaseStorageFiles;
import dev.jdata.db.utils.adt.collections.Coll;
import dev.jdata.db.utils.checks.Checks;

public final class FileTableStorageFiles extends BaseStorageFiles<RandomAccessFile, FileTableStorageFile> {

    static final int INITIAL_SEQUENCE_COUNTER = 0;

    private static final String FILE_NAME_PREFIX = "tabledata";

    static int parseSequenceNo(String fileName) {

        return parseSequenceNo(fileName, FILE_NAME_PREFIX);
    }

    static RandomAccessFile createRandomAccessFile(Path path) throws IOException {

        Objects.requireNonNull(path);

        return new RandomAccessFile(path.toFile(), "rw");
    }

    private int sequenceCounter;

    FileTableStorageFiles(Path tableDirectoryPath, List<FileTableStorageFile> files) {
        super(tableDirectoryPath, files);

        this.sequenceCounter = Coll.max(files, INITIAL_SEQUENCE_COUNTER, FileTableStorageFile::getSequenceNo);
    }

    @Override
    protected String getFileNamePrefix() {

        return FILE_NAME_PREFIX;
    }

    FileTableStorageFile addFile(StorageTableFileSchema storageTableFileSchema, long rowId) throws IOException {

        Objects.requireNonNull(storageTableFileSchema);
        Checks.isRowId(rowId);

        final int sequenceNo = sequenceCounter ++;

        final Path path = constructPath(sequenceNo);

        final RandomAccessFile randomAccessFile = createRandomAccessFile(path);

        final FileTableStorageFile result = new FileTableStorageFile(sequenceNo, storageTableFileSchema, randomAccessFile, rowId);

        addFile(result);

        return result;
    }

    FileTableStorageFile updateFile(FileTableStorageFile currentFile, StorageTableFileSchema storageTableFileSchema, ByteBufferAllocator byteBufferAllocator) throws IOException {

        Objects.requireNonNull(currentFile);
        Objects.requireNonNull(storageTableFileSchema);
        Objects.requireNonNull(byteBufferAllocator);

        final int sequenceNo = currentFile.getSequenceNo();

        if (currentFile != getFile(sequenceNo)) {

            throw new IllegalArgumentException();
        }

        Objects.requireNonNull(storageTableFileSchema);

        final Path path = constructPath(sequenceNo);
        final Path tempPath = constructTempPath(sequenceNo);

        boolean ok = false;

        try {
            currentFile.expandTo(tempPath, storageTableFileSchema, byteBufferAllocator);

            currentFile.close();

            Files.move(tempPath, path, StandardCopyOption.ATOMIC_MOVE);

            ok = true;
        }
        finally {

            if (!ok) {

                Files.deleteIfExists(tempPath);
            }
        }

        final RandomAccessFile randomAccessFile = createRandomAccessFile(path);

        final FileTableStorageFile result = new FileTableStorageFile(sequenceNo, storageTableFileSchema, randomAccessFile, currentFile.getStartRowId());

        setFile(sequenceNo, result);

        return result;
    }

    public FileTableStorageFile getCurrent() {

        return getLastFile();
    }

    private static final RangeGetter<FileTableStorageFile> rangeGetter = new RangeGetter<FileTableStorageFile>() {

        @Override
        public long getBegin(FileTableStorageFile element) {

            return element.getStartRowId();
        }

        @Override
        public long getEnd(FileTableStorageFile element) {

            return element.getLastRowId();
        }
    };

    public FileTableStorageFile findFile(long rowId) {

        return binarySearch(getFiles(), rowId, rangeGetter);
    }

    private interface RangeGetter<T> {

        long getBegin(T element);

        long getEnd(T element);

        default boolean withinRange(T element, long toFind) {

            return toFind >= getBegin(element) && toFind <= getEnd(element);
        }
    }

    private static <T> T binarySearch(List<T> list, long toFind, RangeGetter<T> rangeGetter) {

        final T result;

        switch (list.size()) {

        case 0:

            result = null;
            break;

        case 1:

            final T element = list.get(0);

            result = rangeGetter.withinRange(element, toFind) ? element : null;
            break;

        default:

            result = binarySearch(list, 0, list.size(), toFind, rangeGetter);
            break;
        }

        return result;
    }

    private static <T> T binarySearch(List<T> list, int beginIndex, int numElements, long toFind, RangeGetter<T> rangeGetter) {

        if (numElements < 2) {

            throw new IllegalStateException();
        }

        final int midIndex = beginIndex + (numElements / 2);

        final T midElement = list.get(midIndex);

        final long elementBeginValue = rangeGetter.getBegin(midElement);

        final T result;

        if (toFind < elementBeginValue) {

            final int numSubElements = midIndex - beginIndex;

            if (numSubElements == 1) {

                final T subElement = list.get(beginIndex);

                result = rangeGetter.withinRange(subElement, toFind) ? subElement : null;
            }
            else {
                result = binarySearch(list, beginIndex, numSubElements, toFind, rangeGetter);
            }
        }
        else if (toFind < rangeGetter.getEnd(midElement)) {

            result = midElement;
        }
        else {
            final int numSubElements = numElements - midIndex;

            final int nextAfterMidIndex = midIndex + 1;

            if (numSubElements == 1) {

                final T subElement = list.get(nextAfterMidIndex);

                result = rangeGetter.withinRange(subElement, toFind) ? subElement : null;
            }
            else {
                result = binarySearch(list, nextAfterMidIndex, numSubElements, toFind, rangeGetter);
            }
        }

        return result;
    }
}
