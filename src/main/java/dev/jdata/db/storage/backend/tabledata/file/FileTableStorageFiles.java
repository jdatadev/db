package dev.jdata.db.storage.backend.tabledata.file;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.storage.backend.file.BaseStorageFiles;
import dev.jdata.db.utils.adt.collections.Coll;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.file.access.RandomFileAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.file.access.RelativeFileSystemAccess;

public final class FileTableStorageFiles extends BaseStorageFiles<RandomFileAccess, FileTableStorageFile> {

    static final int INITIAL_SEQUENCE_NO = 0;

    private int sequenceNoAllocator;

    FileTableStorageFiles(RelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath tableDirectoryPath, List<FileTableStorageFile> files) {
        super(fileSystemAccess, tableDirectoryPath, files);

        this.sequenceNoAllocator = Coll.max(files, INITIAL_SEQUENCE_NO, FileTableStorageFile::getSequenceNo);
    }

    @Override
    protected String getFileNamePrefix() {

        return FileTableStorageFile.FILE_NAME_PREFIX;
    }

    FileTableStorageFile addFile(StorageTableFileSchema storageTableFileSchema, long rowId) throws IOException {

        Objects.requireNonNull(storageTableFileSchema);
        Checks.isRowId(rowId);

        final int sequenceNo = sequenceNoAllocator ++;

        final RelativeFilePath filePath = constructPath(sequenceNo);

        final FileTableStorageFile result = FileTableStorageFile.addNewFile(getFileSystemAccess(), filePath, sequenceNo, storageTableFileSchema, rowId);

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

        final RelativeFilePath filePath = constructPath(sequenceNo);
        final RelativeFilePath tempFilePath = constructTempPath(sequenceNo);

        final RelativeFileSystemAccess fileSystemAccess = getFileSystemAccess();

        boolean ok = false;

        try {
            currentFile.expandTo(fileSystemAccess, tempFilePath, storageTableFileSchema, byteBufferAllocator);

            currentFile.close();

            fileSystemAccess.moveAtomically(tempFilePath, filePath);

            ok = true;
        }
        finally {

            if (!ok) {

                fileSystemAccess.deleteIfExists(tempFilePath);
            }
        }

        final FileTableStorageFile result = FileTableStorageFile.openExistingFileFromStorageTableFileSchema(fileSystemAccess, filePath, sequenceNo, storageTableFileSchema, currentFile.getStartRowId());

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
