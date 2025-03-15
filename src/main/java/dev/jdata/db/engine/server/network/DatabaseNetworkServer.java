package dev.jdata.db.engine.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.jdata.db.engine.database.DatabaseParameters;
import dev.jdata.db.engine.database.ExecuteException;
import dev.jdata.db.engine.database.IDatabaseExecuteOperations.DataWriter;
import dev.jdata.db.engine.database.IDatabaseExecuteOperations.SelectResultWriter;
import dev.jdata.db.engine.server.NoSuchDatabaseException;
import dev.jdata.db.engine.server.SQLDatabaseServer;
import dev.jdata.db.engine.server.SQLDatabaseServer.ExecuteSQLResultWriter;
import dev.jdata.db.engine.server.network.protocol.ExecutePreparedStatementMessage;
import dev.jdata.db.engine.server.network.protocol.ExecuteSQLMessage;
import dev.jdata.db.engine.server.network.protocol.FreePreparedStatementMessage;
import dev.jdata.db.engine.server.network.protocol.PrepareStatementMessage;
import dev.jdata.db.engine.server.network.protocol.Protocol;
import dev.jdata.db.engine.server.network.protocol.ProtocolDecodeException;
import dev.jdata.db.engine.server.network.protocol.ProtocolMessage;
import dev.jdata.db.engine.server.network.protocol.ProtocolMessage.ProtocolAllocator;
import dev.jdata.db.engine.server.network.protocol.ProtocolMessageType;
import dev.jdata.db.engine.server.network.protocol.SetupMessage;
import dev.jdata.db.engine.server.network.protocol.SetupMessage.DatabaseCreateMode;
import dev.jdata.db.engine.server.network.protocol.strings.CharString;
import dev.jdata.db.utils.allocators.ObjectCache;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseNetworkServer {

    static final class LargeObjectState {

        private int preparedStatementId;
        private long length;
        private long largeObjectRef;
        private long numBytesProcessed;

        void initialize(int preparedStatementId, long length, long largeObjectRef, int numBytesProcessed) {

            Checks.isPreparedStatementId(preparedStatementId);
            Checks.isNumBytes(length);
            Checks.isLargeObjectRef(largeObjectRef);
            Checks.isNotNegative(numBytesProcessed);
            Checks.isLessThanOrEqualTo(numBytesProcessed, length);

            this.preparedStatementId = preparedStatementId;
            this.length = length;
            this.largeObjectRef = largeObjectRef;
            this.numBytesProcessed = numBytesProcessed;
        }

        void addToNumBytesProcessed(int toAdd) {

            this.numBytesProcessed += Checks.isNumBytes(toAdd);
        }

        int getPreparedStatementId() {
            return preparedStatementId;
        }

        long getLength() {
            return length;
        }

        long getLargeObjectRef() {
            return largeObjectRef;
        }

        long getNumBytesProcessed() {
            return numBytesProcessed;
        }
    }

    private final SQLDatabaseServer serverDelegate;
    private final ProtocolAllocator protocolAllocator;

    private final Protocol protocol;

    private final ObjectCache<NetworkClient> networkClientCache;
    private final Map<Charset, CharsetDecoder> decoderByCharset;

    public DatabaseNetworkServer(SQLDatabaseServer serverDelegate, ProtocolAllocator protocolAllocator) {

        this.serverDelegate = Objects.requireNonNull(serverDelegate);
        this.protocolAllocator = Objects.requireNonNull(protocolAllocator);

        this.protocol = new Protocol();

        this.networkClientCache = new ObjectCache<>(() -> new NetworkClient(this), NetworkClient[]::new);
        this.decoderByCharset = new HashMap<>(3);
    }

    NetworkClient onConnect() {

        final NetworkClient result;

        synchronized (networkClientCache) {

            result = networkClientCache.allocate();
        }

        return result;
    }

    void onDisconnect(NetworkClient networkClient) {

        Objects.requireNonNull(networkClient);

        serverDelegate.closeSession(networkClient.getDatabaseId(), networkClient.getSessionId());

        synchronized (networkClientCache) {

            networkClientCache.free(networkClient);
        }
    }

    void onSetup(ByteBuffer byteBuffer, int offset, int length, NetworkClient networkClient) throws ProtocolDecodeException, NoSuchDatabaseException {

        final ProtocolMessage protocolMessage = protocol.decode(byteBuffer, offset, length, null, protocolAllocator);

        final SetupMessage setupMessage = (SetupMessage)protocolMessage;

        final CharString databaseName = setupMessage.getDatabaseName();

        final Charset charset = findCharset(setupMessage.getCharsetName());

        final int setupDatabaseId;

        final int existingDatabaseId = serverDelegate.getDatabase(databaseName);

        final DatabaseCreateMode databaseCreateMode = setupMessage.getCreateMode();

        if (Checks.checkIsDatabaseId(existingDatabaseId)) {

            if (databaseCreateMode != null) {

                switch (databaseCreateMode) {

                case NEVER:
                case IF_NOT_EXISTS:

                    setupDatabaseId = existingDatabaseId;
                    break;

                case DROP_AND_CREATE:

                    serverDelegate.dropDatabase(existingDatabaseId);

                    setupDatabaseId = serverDelegate.createDatabase(databaseName, makeDatabaseParameters(setupMessage));
                    break;

                default:
                    throw new UnsupportedOperationException();
                }
            }
            else {
                setupDatabaseId = existingDatabaseId;
            }
        }
        else {
            if (databaseCreateMode != DatabaseCreateMode.NEVER) {

                setupDatabaseId = serverDelegate.createDatabase(databaseName, makeDatabaseParameters(setupMessage));
            }
            else {
                throw new NoSuchDatabaseException();
            }
        }

        final int setupSessionId = serverDelegate.addSession(setupDatabaseId, charset);

        networkClient.initialize(setupDatabaseId, setupSessionId);
    }

    int isCompleteMessage(ByteBuffer byteBuffer, int offset, int length) {

        Objects.requireNonNull(byteBuffer);
        Checks.checkBuffer(byteBuffer, offset, length);

        return Protocol.isCompleteMessage(byteBuffer, offset, length);
    }

    <E extends Exception> boolean onMessage(int databaseId, int sessionId, ByteBuffer byteBuffer, int offset, int length, LargeObjectState largeObjectState,
            ProtocolMessageResponseWriter<E> responseWriter) throws ProtocolDecodeException, NoSuchDatabaseException, ParserException, ExecuteException, E, IOException {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Objects.requireNonNull(byteBuffer);
        Checks.checkBuffer(byteBuffer, offset, length);
        Objects.requireNonNull(largeObjectState);
        Objects.requireNonNull(responseWriter);

        final boolean completeMessage;

        int currentOffset = offset;

        final CharsetDecoder charsetDecoder = Checks.checkIsDatabaseId(databaseId) ? getCharsetDecoder(databaseId, sessionId) : null;

        final ProtocolMessageType protocolMessageType = protocol.decodeMessageType(byteBuffer, currentOffset ++, length);

        if (protocolMessageType == ProtocolMessageType.EXECUTE_PREPARED_STATEMENT_PARAMETER_ROW_LARGE_OBJECT) {

            final long messageLength = byteBuffer.getLong(currentOffset);

            currentOffset += Long.BYTES;

            final int preparedStatementId = byteBuffer.getInt(currentOffset);

            currentOffset += Integer.BYTES;

            final int skipped = (currentOffset - offset);

            final long largeObjectLength = messageLength - skipped;

            final long largeObjectRef = serverDelegate.createPreparedStatementLargeObject(databaseId, sessionId, preparedStatementId, length);

            final int remainingInByteBuffer = length - skipped;

            if (remainingInByteBuffer != 0) {

                final boolean isFinal = remainingInByteBuffer >= largeObjectLength;

                serverDelegate.storePreparedStatementLargeObjectPart(databaseId, sessionId, preparedStatementId, largeObjectRef, isFinal, byteBuffer, currentOffset,
                        remainingInByteBuffer);

                completeMessage = isFinal;
            }
            else {
                if (largeObjectLength == 0) {

                    throw new IllegalArgumentException();
                }

                completeMessage = false;
            }

            if (!completeMessage) {

                largeObjectState.initialize(preparedStatementId, largeObjectLength, largeObjectRef, remainingInByteBuffer);
            }
        }
        else {
            final ProtocolMessage protocolMessage = protocol.decode(byteBuffer, offset, length, charsetDecoder, protocolAllocator);

            final DataWriter<E> dataWriter = responseWriter.getDataWriter();

            try {
                switch (protocolMessage.getMessageType()) {

                case EXECUTE_SQL:

                    final ExecuteSQLMessage executeSQLMessage = (ExecuteSQLMessage)protocolMessage;

                    protocol.writeExecuteSQLResponseHeader(dataWriter);

                    final long executeSQLResult = serverDelegate.executeSQL(databaseId, sessionId, executeSQLMessage.getSQL(), responseWriter);

                    protocol.writeExecuteSQLResponseCompletion(dataWriter, executeSQLResult);
                    break;

                case PREPARE_STATEMENT: {

                    final PrepareStatementMessage prepareStatementMessage = (PrepareStatementMessage)protocolMessage;

                    final int preparedStatementId = serverDelegate.prepareStatement(databaseId, sessionId, prepareStatementMessage.getSQL());

                    protocol.writePrepareStatementResponse(dataWriter, preparedStatementId);
                    break;
                }

                case EXECUTE_PREPARED_STATEMENT: {

                    final ExecutePreparedStatementMessage executePreparedStatementMessage = (ExecutePreparedStatementMessage)protocolMessage;

                    final int preparedStatementId = executePreparedStatementMessage.getStatementId();

                    final long executePreparedStatementResult = serverDelegate.executePreparedStatement(databaseId, sessionId, preparedStatementId,
                            executePreparedStatementMessage, responseWriter);

                    protocol.writeExecuteStatementResponse(dataWriter, preparedStatementId, executePreparedStatementResult);
                    break;
                }

                case EXECUTE_PREPARED_STATEMENT_PARAMETER_ROW_START:

                    break;

                case EXECUTE_PREPARED_STATEMENT_PARAMETER_ROW_COMPLETION:

                    break;

                case FREE_PREPARED_STATEMENT:

                    final FreePreparedStatementMessage freePreparedStatementMessage = (FreePreparedStatementMessage)protocolMessage;

                    serverDelegate.freePreparedStatement(databaseId, sessionId, freePreparedStatementMessage.getStatementId());

                    protocol.writeFreePreparedStatementResponse(dataWriter, freePreparedStatementMessage.getStatementId());
                    break;

                default:
                    throw new UnsupportedOperationException();
                }

                completeMessage = true;
            }
            finally {

                protocol.free(protocolMessage);
            }
        }

        return completeMessage;
    }

    boolean onMessageContinuation(int databaseId, int sessionId, ByteBuffer byteBuffer, int offset, int length, LargeObjectState largeObjectState) throws IOException {

        Checks.isDatabaseId(databaseId);
        Checks.isSessionDescriptor(sessionId);
        Checks.checkBuffer(byteBuffer, offset, length);
        Objects.requireNonNull(largeObjectState);

        final long largeObjectLength = largeObjectState.getLength();
        final long totalRecievedNumBytes = largeObjectState.getNumBytesProcessed() + length;

        final boolean isFinal;

        if (totalRecievedNumBytes < largeObjectLength) {

            isFinal = false;
        }
        else if (totalRecievedNumBytes == largeObjectLength) {

            isFinal = true;
        }
        else {
            throw new IllegalStateException();
        }

        serverDelegate.storePreparedStatementLargeObjectPart(databaseId, sessionId, offset, length, isFinal, byteBuffer, offset, length);

        return isFinal;
    }

    @Deprecated
    private static DatabaseParameters makeDatabaseParameters(SetupMessage setupMessage) {

        throw new UnsupportedOperationException();
    }

    @Deprecated // remove allocation with string cache?
    private static Charset findCharset(CharString charsetName) {

        return Charset.forName(null);
    }

    @FunctionalInterface
    private interface SQLCharBufferFunction {

        int processSQL(SQLDatabaseServer server, int databaseId, int sessionId, CharBuffer charBuffer);
    }

    @Deprecated // is synchronization necessary?
    private CharsetDecoder getCharsetDecoder(int databaseId, int sessionId) {

        final Charset charset = serverDelegate.getSessionCharset(databaseId, sessionId);

        CharsetDecoder charsetDecoder;

        synchronized (this) {

            charsetDecoder = decoderByCharset.get(charset);

            if (charsetDecoder == null) {

                charsetDecoder = charset.newDecoder();
            }

            decoderByCharset.put(charset, charsetDecoder);
        }

        return charsetDecoder;
    }

    private static final class ByteBufferSelectResultWriter extends ByteBufferDataWriter implements SelectResultWriter<RuntimeException> {

        @Override
        public void startRow() {

            throw new UnsupportedOperationException();
        }

        @Override
        public void endRow() {

            throw new UnsupportedOperationException();
        }
    }

    public interface ProtocolMessageResponseWriter<E extends Exception> extends ExecuteSQLResultWriter<E> {

        DataWriter<E> getDataWriter();
    }

    private static final class ProtocolMessageResponseWriterImpl<E extends Exception> implements ProtocolMessageResponseWriter<E> {

        private final SelectResultWriter<E> selectResultWriter;
        private final DataWriter<E> dataWriter;

        ProtocolMessageResponseWriterImpl(SelectResultWriter<E> selectResultWriter, DataWriter<E> dataWriter) {

            this.selectResultWriter = Objects.requireNonNull(selectResultWriter);
            this.dataWriter = Objects.requireNonNull(dataWriter);
        }

        @Override
        public SelectResultWriter<E> getSelectResultWriter() {

            return selectResultWriter;
        }

        @Override
        public DataWriter<E> getDataWriter() {
            return dataWriter;
        }
    }
}
