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
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.schema.storage.sqloutputter.TextToByteOutputPrerequisites;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.storage.file.FileStorage;
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

public final class DatabaseSchemaStorageFactory implements IDatabaseSchemaStorageFactory<IOException> {

    private static final String SCHEMA_VERSION_PREFIX = "schemaversion";
    private static final String SCHEMA_DIFF_PREFIX = "schemadiff";

    private static final int DIFF_NO_SEQUENCE_NO = DBConstants.FILE_NO_SEQUENCE_NO;
    private static final int DIFF_INITIAL_SEQUENCE_NO = DBConstants.FILE_INITIAL_SEQUENCE_NO;

    private final IRelativeFileSystemAccess fileSystemAccess;
    private final IStringCache stringCache;
    private final TextToByteOutputPrerequisites textToByteOutputPrerequisites;

    private final NodeObjectCache<Storage> storageCache;

    private static final class Storage extends FileStorage implements IDatabaseSchemaStorage<IOException> {

        private RelativeDirectoryPath schemaDirectoryPath;
        private DatabaseSchemaStorageFactory schemaStorageFactory;

        private int diffSequenceNo;

        void initialize(RelativeDirectoryPath schemaDirectoryPath, DatabaseSchemaStorageFactory schemaStorageFactory) {

            this.schemaDirectoryPath = Objects.requireNonNull(schemaDirectoryPath);
            this.schemaStorageFactory = Objects.requireNonNull(schemaStorageFactory);

            this.diffSequenceNo = DIFF_INITIAL_SEQUENCE_NO;
        }

        @Override
        public void reset() {

            this.schemaDirectoryPath = null;
            this.schemaStorageFactory = null;
            this.diffSequenceNo = DIFF_NO_SEQUENCE_NO;
        }

        @Override
        public void storeSchemaDiffStatement(BaseSQLDDLOperationStatement sqlDDLStatement, ISQLString sqlString, StringResolver stringResolver) throws IOException {

            Objects.requireNonNull(sqlDDLStatement);
            Objects.requireNonNull(sqlString);
            Objects.requireNonNull(stringResolver);

            final int sequenceNo = allocateDiffSequenceNo();

            final IRelativeFileSystemAccess fileSystemAccess = schemaStorageFactory.fileSystemAccess;

            final RelativeFilePath filePath = constructSchemaDiffPath(fileSystemAccess, schemaDirectoryPath, sequenceNo);

            final TextToByteOutputPrerequisites textToByteOutputPrerequisites = schemaStorageFactory.textToByteOutputPrerequisites;

            final CharsetEncoder charsetEncoder = textToByteOutputPrerequisites.getCharsetEncoder();
            final CharBufferAllocator charBufferAllocator = textToByteOutputPrerequisites.getCharBufferAllocator();
            final ByteArrayByteBufferAllocator byteBufferAllocator = textToByteOutputPrerequisites.getByteBufferAllocator();

            final int numCharacters = 1000;
            final int numBytes = CharacterEncodingUtil.calculateNumEncodedBytes(charsetEncoder, numCharacters);

            final CharBuffer charBuffer = charBufferAllocator.allocateForEncodeCharacters(numCharacters);
            final ByteBuffer byteBuffer = byteBufferAllocator.allocateByteArrayByteBuffer(numBytes);

            try (FileChannel fileChannel = fileSystemAccess.openFileChannel(filePath, OpenMode.WRITE_ONLY_CREATE_FAIL_IF_EXISTS)) {

                sqlString.writeSQLString(fileChannel, charsetEncoder, charBuffer, byteBuffer, FileChannel::write);
            }
            finally {

                charBufferAllocator.freeCharBuffer(charBuffer);
                byteBufferAllocator.freeByteBuffer(byteBuffer);
            }
        }

        @Override
        public void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer schemaSerializer, StringResolver stringResolver,
                ISQLOutputter<IOException> sqlOutputter) throws IOException {

            Objects.requireNonNull(completeEffectiveDatabaseSchema);
            Objects.requireNonNull(schemaSerializer);
            Objects.requireNonNull(stringResolver);
            Objects.requireNonNull(sqlOutputter);

            schemaSerializer.serialize(completeEffectiveDatabaseSchema, stringResolver, sqlOutputter);

            schemaStorageFactory.onSchemaComplete(this);
        }

        private int allocateDiffSequenceNo() {

            return diffSequenceNo ++;
        }

        private static RelativeFilePath constructSchemaDiffPath(IRelativeFileSystemAccess fileSystemAccess, RelativeDirectoryPath directoryPath, int sequenceNo) {

            return constructPath(fileSystemAccess, directoryPath, SCHEMA_DIFF_PREFIX, sequenceNo);
        }
    }

    private DatabaseSchemaStorageFactory(AbsoluteDirectoryPath rootPath, IAbsoluteFileSystemAccess absoluteFileSystemAccess, IStringCache stringCache,
            TextToByteOutputPrerequisites textToByteOutputPrerequisites) {
        this(IRelativeFileSystemAccess.create(rootPath, absoluteFileSystemAccess), stringCache, textToByteOutputPrerequisites);
    }

    public DatabaseSchemaStorageFactory(IRelativeFileSystemAccess fileSystemAccess, IStringCache stringCache, TextToByteOutputPrerequisites textToByteOutputPrerequisites) {

        this.fileSystemAccess = Objects.requireNonNull(fileSystemAccess);
        this.stringCache = Objects.requireNonNull(stringCache);
        this.textToByteOutputPrerequisites = Objects.requireNonNull(textToByteOutputPrerequisites);

        this.storageCache = new NodeObjectCache<>(Storage::new);
    }

    @Override
    public IDatabaseSchemaStorage<IOException> createSchemaDiffStorage(DatabaseSchemaVersion databaseSchemaVersion) throws IOException {

        Objects.requireNonNull(databaseSchemaVersion);

        final RelativeDirectoryPath directoryPath = constructSchemaVersionPath(fileSystemAccess, databaseSchemaVersion, stringCache);

        fileSystemAccess.createDirectory(directoryPath);

        final NodeObjectCache<Storage> cache = storageCache;

        final Storage storage;

        synchronized (cache) {

            storage = storageCache.allocate();
        }

        storage.initialize(directoryPath, this);

        return storage;
    }

    private void onSchemaComplete(Storage storage) {

        final NodeObjectCache<Storage> cache = storageCache;

        storage.reset();

        synchronized (cache) {

            cache.free(storage);
        }
    }

    private static RelativeDirectoryPath constructSchemaVersionPath(IRelativeFileSystemAccess fileSystemAccess, DatabaseSchemaVersion databaseSchemaVersion,
            IStringCache stringCache) {

        final int schemaVersionNumber = databaseSchemaVersion.getVersionNumber();
        final String schemaVersionNumberString = stringCache.getOrAddString(SCHEMA_VERSION_PREFIX + schemaVersionNumber);

        return fileSystemAccess.directoryPathOf(schemaVersionNumberString);
    }
}
