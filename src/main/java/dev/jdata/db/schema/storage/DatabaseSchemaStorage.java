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
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.schema.storage.sqloutputter.TextToByteOutputPrerequisites;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.SQLString;
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

public final class DatabaseSchemaStorage<T extends CompleteSchemaMaps<?>> implements IDatabaseSchemaStorageFactory<T, IOException> {

    private static final String SCHEMA_DIFF_PREFIX = "schemadiff";

    private static final int DIFF_NO_SEQUENCE_NO = DBConstants.FILE_NO_SEQUENCE_NO;
    private static final int DIFF_INITIAL_SEQUENCE_NO = DBConstants.FILE_INITIAL_SEQUENCE_NO;

    private final IStringCache stringCache;

    private TextToByteOutputPrerequisites textOutputPrerequisites;
    private final IRelativeFileSystemAccess fileSystemAccess;

    private final NodeObjectCache<Storage<T>> storageCache;

    private static final class Storage<T extends CompleteSchemaMaps<?>> extends FileStorage implements IDatabaseSchemaStorage<T, IOException> {

        private RelativeDirectoryPath schemaDirectoryPath;
        private DatabaseSchemaStorage<T> schemaStorage;

        private int diffSequenceNo;

        void initialize(RelativeDirectoryPath schemaDirectoryPath, DatabaseSchemaStorage<T> schemaStorage) {

            this.schemaDirectoryPath = Objects.requireNonNull(schemaDirectoryPath);
            this.schemaStorage = Objects.requireNonNull(schemaStorage);

            this.diffSequenceNo = DIFF_INITIAL_SEQUENCE_NO;
        }

        @Override
        public void reset() {

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

            final TextToByteOutputPrerequisites textOutputPrerequisites = schemaStorage.textOutputPrerequisites;

            final CharsetEncoder charsetEncoder = textOutputPrerequisites.getCharsetEncoder();
            final CharBufferAllocator charBufferAllocator = textOutputPrerequisites.getCharBufferAllocator();
            final ByteArrayByteBufferAllocator byteBufferAllocator = textOutputPrerequisites.getByteBufferAllocator();

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
        public void completeSchemaDiff(IEffectiveDatabaseSchema completeEffectiveDatabaseSchema, IDatabaseSchemaSerializer<T> schemaSerializer,
                StringResolver stringResolver, ISQLOutputter<IOException> sqlOutputter) throws IOException {

            Objects.requireNonNull(completeEffectiveDatabaseSchema);
            Objects.requireNonNull(schemaSerializer);
            Objects.requireNonNull(stringResolver);
            Objects.requireNonNull(sqlOutputter);

            schemaSerializer.serialize(completeEffectiveDatabaseSchema, stringResolver, sqlOutputter);

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
    public IDatabaseSchemaStorage<T, IOException> storeSchemaDiff(DatabaseSchemaVersion databaseSchemaVersion) throws IOException {

        Objects.requireNonNull(databaseSchemaVersion);

        final int schemaVersionNumber = databaseSchemaVersion.getVersionNumber();
        final String schemaVersionNumberString = stringCache.getString(schemaVersionNumber);
        final RelativeDirectoryPath directoryPath = fileSystemAccess.directoryPathOf(schemaVersionNumberString);

        fileSystemAccess.createDirectory(directoryPath);

        final NodeObjectCache<Storage<T>> cache = storageCache;

        final Storage<T> storage;

        synchronized (cache) {

            storage = storageCache.allocate();
        }

        storage.initialize(directoryPath, this);

        return storage;
    }

    private void onSchemaComplete(Storage<T> storage) {

        final NodeObjectCache<Storage<T>> cache = storageCache;

        storage.reset();

        synchronized (cache) {

            cache.free(storage);
        }
    }
}
