package dev.jdata.db.schema.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.IStringCache;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.SQLParser.SQLString;
import dev.jdata.db.storage.file.FileStorage;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.strings.CharacterEncodingUtil;
import dev.jdata.db.utils.allocators.ByteArrayByteBufferAllocator;
import dev.jdata.db.utils.allocators.CharBufferAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.file.access.AbsoluteDirectoryPath;
import dev.jdata.db.utils.file.access.IAbsoluteFileSystemAccess;
import dev.jdata.db.utils.file.access.IFileSystemAccess.OpenMode;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.function.CheckedExceptionBiConsumer;

public final class DatabaseSchemaStorage implements IDatabaseSchemaStorageFactory<IOException> {

    private static final String SCHEMA_DIFF_PREFIX = "schemadiff";

    private static final int DIFF_NO_SEQUENCE_NO = DBConstants.FILE_NO_SEQUENCE_NO;
    private static final int DIFF_INITIAL_SEQUENCE_NO = DBConstants.FILE_INITIAL_SEQUENCE_NO;

    private final IStringCache stringCache;

    private CharsetEncoder charsetEncoder;
    private final IRelativeFileSystemAccess fileSystemAccess;
    private CharBufferAllocator charBufferAllocator;
    private ByteArrayByteBufferAllocator byteBufferAllocator;

    private final NodeObjectCache<Storage> storageCache;

    private static final class Storage extends FileStorage implements IDatabaseSchemaStorage<IOException>, IResettable {

        private DatabaseSchemaVersion schemaVersion;
        private RelativeDirectoryPath schemaDirectoryPath;
        private DatabaseSchemaStorage schemaStorage;

        private int diffSequenceNo;

        void initialize(DatabaseSchemaVersion schemaVersion, RelativeDirectoryPath schemaDirectoryPath, DatabaseSchemaStorage schemaStorage) {

            this.schemaVersion = Objects.requireNonNull(schemaVersion);
            this.schemaDirectoryPath = Objects.requireNonNull(schemaDirectoryPath);
            this.schemaStorage = Objects.requireNonNull(schemaStorage);

            this.diffSequenceNo = DIFF_INITIAL_SEQUENCE_NO;
        }

        @Override
        public void reset() {

            this.schemaVersion = null;
            this.schemaDirectoryPath = null;
            this.schemaStorage = null;
            this.diffSequenceNo = DIFF_NO_SEQUENCE_NO;
        }

        @Override
        public void storeSchemaDiffStatement(BaseSQLDDLOperationStatement sqlDDLStatement, SQLString sqlString, StringResolver stringResolver) throws IOException {

            Objects.requireNonNull(sqlDDLStatement);
            Objects.requireNonNull(sqlString);
            Objects.requireNonNull(stringResolver);

            final int sequenceNo = allocateDiffSequenceNo();

            final IRelativeFileSystemAccess fileSystemAccess = schemaStorage.fileSystemAccess;

            final RelativeFilePath filePath = constructPath(fileSystemAccess, schemaDirectoryPath, SCHEMA_DIFF_PREFIX, sequenceNo);

            final CharsetEncoder encoder = schemaStorage.charsetEncoder;
            final CharBufferAllocator charBufferAllocator = schemaStorage.charBufferAllocator;
            final ByteArrayByteBufferAllocator byteBufferAllocator = schemaStorage.byteBufferAllocator;

            final int numCharacters = 1000;
            final int numBytes = CharacterEncodingUtil.calculateNumEncodedBytes(encoder, numCharacters);

            final CharBuffer charBuffer = charBufferAllocator.allocateForEncodeCharacters(numCharacters);
            final ByteBuffer byteBuffer = byteBufferAllocator.allocateByteArrayByteBuffer(numBytes);

            try (FileChannel fileChannel = fileSystemAccess.openFileChannel(filePath, OpenMode.WRITE_ONLY_CREATE_FAIL_IF_EXISTS)) {

                writeSQLString(sqlString, fileChannel, encoder, charBuffer, byteBuffer, FileChannel::write);
            }
            finally {

                charBufferAllocator.freeCharBuffer(charBuffer);
                byteBufferAllocator.freeByteBuffer(byteBuffer);
            }
        }

        private static <T, E extends Exception> void writeSQLString(SQLString sqlString, T output, CharsetEncoder charsetEncoder, CharBuffer charBuffer, ByteBuffer byteBuffer,
                CheckedExceptionBiConsumer<T, ByteBuffer, E> consumer) throws E {

            long sqlStringOffset = 0L;

            final int charBufferCapacity = charBuffer.capacity();

            for (;;) {

                charBuffer.clear();

                final long numCharactersRetrieved = sqlString.writeToCharBuffer(charBuffer, sqlStringOffset);

                if (numCharactersRetrieved <= 0L) {

                    break;
                }

                byteBuffer.clear();

                final boolean endOfInput = numCharactersRetrieved < charBufferCapacity;

                if (charsetEncoder.encode(charBuffer, byteBuffer, endOfInput).isError()) {

                    throw new IllegalArgumentException();
                }

                consumer.accept(output, byteBuffer);

                sqlStringOffset += numCharactersRetrieved;
            }
        }

        @Override
        public void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer<IOException> schemaSerializer) throws IOException {

            Objects.requireNonNull(completeEffectiveDatabaseSchema);
            Objects.requireNonNull(schemaSerializer);

            schemaSerializer.serialize(completeEffectiveDatabaseSchema);

            schemaStorage.onSchemaComplete(this);
        }

        private int allocateDiffSequenceNo() {

            return diffSequenceNo ++;
        }
    }

    public DatabaseSchemaStorage(AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess absoluteFileSystemAccess, IStringCache stringCache) {

        Objects.requireNonNull(rootPath);
        Objects.requireNonNull(absoluteFileSystemAccess);
        Objects.requireNonNull(stringCache);

        this.stringCache = stringCache;

        this.fileSystemAccess = IRelativeFileSystemAccess.create(rootPath, absoluteFileSystemAccess);

        this.storageCache = new NodeObjectCache<>(Storage::new);
    }

    @Override
    public IDatabaseSchemaStorage<IOException> storeSchemaDiff(DatabaseSchemaVersion databaseSchemaVersion) throws IOException {

        Objects.requireNonNull(databaseSchemaVersion);

        final int schemaVersionNumber = databaseSchemaVersion.getVersionNumber();
        final String schemaVersionNumberString = stringCache.getString(schemaVersionNumber);
        final RelativeDirectoryPath directoryPath = fileSystemAccess.directoryPathOf(schemaVersionNumberString);

        fileSystemAccess.createDirectory(directoryPath);

        final NodeObjectCache<Storage> cache = storageCache;

        final Storage storage;

        synchronized (cache) {

            storage = storageCache.allocate();
        }

        storage.initialize(databaseSchemaVersion, directoryPath, this);

        return storage;
    }

    private void onSchemaComplete(Storage storage) {

        final NodeObjectCache<Storage> cache = storageCache;

        storage.reset();

        synchronized (cache) {

            cache.free(storage);
        }
    }
}
