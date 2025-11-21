package dev.jdata.db.schema.storage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBConstants;
import dev.jdata.db.schema.model.effective.IEffectiveDatabaseSchema;
import dev.jdata.db.schema.storage.IDatabaseSchemaStorageFactory.IDatabaseSchemaStorage;
import dev.jdata.db.schema.storage.sqloutputter.ISQLOutputter;
import dev.jdata.db.schema.storage.sqloutputter.TextToByteOutputPrerequisites;
import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.storage.file.FileStorage;
import dev.jdata.db.utils.file.access.IFileSystemAccess.OpenMode;
import dev.jdata.db.utils.file.access.IRelativeFileSystemAccess;
import dev.jdata.db.utils.file.access.RelativeDirectoryPath;
import dev.jdata.db.utils.file.access.RelativeFilePath;
import dev.jdata.db.utils.jdk.adt.strings.CharacterEncodingUtil;
import dev.jdata.db.utils.jdk.niobuffers.ByteArrayByteBufferAllocator;
import dev.jdata.db.utils.jdk.niobuffers.CharBufferAllocator;

final class DatabaseSchemaStorage extends FileStorage implements IDatabaseSchemaStorage<IOException> {

    private static final String SCHEMA_DIFF_PREFIX = "schemadiff";

    private static final int DIFF_NO_SEQUENCE_NO = DBConstants.FILE_NO_SEQUENCE_NO;
    private static final int DIFF_INITIAL_SEQUENCE_NO = DBConstants.FILE_INITIAL_SEQUENCE_NO;

    private RelativeDirectoryPath schemaDirectoryPath;
    private DatabaseSchemaStorageFactory schemaStorageFactory;

    private int diffSequenceNo;

    DatabaseSchemaStorage(AllocationType allocationType) {
        super(allocationType);
    }

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

        final IRelativeFileSystemAccess fileSystemAccess = schemaStorageFactory.getFileSystemAccess();

        final RelativeFilePath filePath = constructSchemaDiffPath(fileSystemAccess, schemaDirectoryPath, sequenceNo);

        final TextToByteOutputPrerequisites textToByteOutputPrerequisites = schemaStorageFactory.getTextToByteOutputPrerequisites();

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
