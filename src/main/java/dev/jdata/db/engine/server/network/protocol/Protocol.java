package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.Objects;

import dev.jdata.db.engine.database.IDatabaseExecuteOperations.DataWriter;
import dev.jdata.db.engine.server.network.protocol.ProtocolMessage.ProtocolAllocator;
import dev.jdata.db.utils.allocators.ObjectCache;
import dev.jdata.db.utils.checks.Checks;

public final class Protocol {

    private static final int MAX_MESSAGE_BYTE_BUFFER_LENGTH = 1 << 20;

    public static final int MESSAGE_READ_MORE_DATA = -1;
    public static final int MESSAGE_PROCESS_WITH_BLOCKING_READ = -2;


    private final ObjectCache<SetupMessage> setupMessageCache;
    private final ObjectCache<ExecuteSQLMessage> executeSQLMessageCache;
    private final ObjectCache<PrepareStatementMessage> prepareStatementMessageCache;
    private final ObjectCache<ExecutePreparedStatementMessage> executePreparedStatementMessageCache;
    private final ObjectCache<AddPreparedStatementBatchMessage> addPreparedStatementBatchMessageCache;
    private final ObjectCache<ExecutePreparedStatementBatchMessage> executePreparedStatementBatchMessageCache;
    private final ObjectCache<FreePreparedStatementMessage> freePreparedStatementMessageCache;

    public Protocol() {

        this.setupMessageCache = new ObjectCache<>(SetupMessage::new, SetupMessage[]::new);
        this.executeSQLMessageCache = new ObjectCache<>(ExecuteSQLMessage::new, ExecuteSQLMessage[]::new);
        this.prepareStatementMessageCache = new ObjectCache<>(PrepareStatementMessage::new, PrepareStatementMessage[]::new);
        this.executePreparedStatementMessageCache = new ObjectCache<>(ExecutePreparedStatementMessage::new, ExecutePreparedStatementMessage[]::new);
        this.addPreparedStatementBatchMessageCache = new ObjectCache<>(AddPreparedStatementBatchMessage::new, AddPreparedStatementBatchMessage[]::new);
        this.executePreparedStatementBatchMessageCache = new ObjectCache<>(ExecutePreparedStatementBatchMessage::new, ExecutePreparedStatementBatchMessage[]::new);
        this.freePreparedStatementMessageCache = new ObjectCache<>(FreePreparedStatementMessage::new, FreePreparedStatementMessage[]::new);
    }

    public static int isCompleteMessage(ByteBuffer byteBuffer, int offset, int length) {

        Objects.requireNonNull(byteBuffer);
        Checks.checkBuffer(byteBuffer, offset, length);

        final int result;

        int currentOffset = offset;

        final byte messageTypeByte = byteBuffer.get(currentOffset ++);

        final int messageLength = byteBuffer.getInt(currentOffset);

        switch (ProtocolMessageType.fromByte(messageTypeByte)) {

        case SETUP:
        case PREPARE_STATEMENT:

            result = getCompleteMessageLength(messageLength, length);
            break;

        default:

            result = messageLength > MAX_MESSAGE_BYTE_BUFFER_LENGTH ? MESSAGE_PROCESS_WITH_BLOCKING_READ : getCompleteMessageLength(messageLength, length);
            break;
        }

        return result;
    }

    private static int getCompleteMessageLength(int messageLength, int length) {

        return messageLength <= length ? messageLength : MESSAGE_READ_MORE_DATA;
    }

    public ProtocolMessageType decodeMessageType(ByteBuffer byteBuffer, int offset, int length) {

        final byte messageTypeByte = byteBuffer.get(offset);

        return ProtocolMessageType.fromByte(messageTypeByte);
    }

    public ProtocolMessage decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException {

        int currentOffset = offset;

        final ProtocolMessage result;

        final ProtocolMessageType messageType = decodeMessageType(byteBuffer, currentOffset ++, length);

        int currentLength = length - 1;

        switch (messageType) {

        case SETUP:

            final SetupMessage setupMessage = setupMessageCache.allocate();

            setupMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = setupMessage;
            break;

        case EXECUTE_SQL:

            final ExecuteSQLMessage executeSQLMessage = executeSQLMessageCache.allocate();

            executeSQLMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = executeSQLMessage;
            break;

        case PREPARE_STATEMENT:

            final PrepareStatementMessage prepareStatementMessage = prepareStatementMessageCache.allocate();

            prepareStatementMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = prepareStatementMessage;
            break;

        case EXECUTE_PREPARED_STATEMENT:

            final ExecutePreparedStatementMessage executePreparedStatementMessage = executePreparedStatementMessageCache.allocate();

            executePreparedStatementMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = executePreparedStatementMessage;
            break;

        case ADD_PREPARED_STATEMENT_BATCH:

            final AddPreparedStatementBatchMessage addPreparedStatementBatchMessage = addPreparedStatementBatchMessageCache.allocate();

            addPreparedStatementBatchMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = addPreparedStatementBatchMessage;
            break;

        case EXECUTE_PREPARED_STATEMENT_BATCH:

            final ExecutePreparedStatementBatchMessage executePreparedStatementBatchMessage = executePreparedStatementBatchMessageCache.allocate();

            executePreparedStatementBatchMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = executePreparedStatementBatchMessage;
            break;

        case FREE_PREPARED_STATEMENT:

            final FreePreparedStatementMessage freePreparedStatementMessage = freePreparedStatementMessageCache.allocate();

            freePreparedStatementMessage.decode(byteBuffer, currentOffset, currentLength, charsetDecoder, allocator);

            result = freePreparedStatementMessage;
            break;

         default:
             throw new UnsupportedOperationException();
        }

        return result;
    }

    public void free(ProtocolMessage protocolMessage) {

        Objects.requireNonNull(protocolMessage);

        switch (protocolMessage.getMessageType()) {

        case SETUP:

            setupMessageCache.free((SetupMessage)protocolMessage);
            break;

        case EXECUTE_SQL:

            executeSQLMessageCache.free((ExecuteSQLMessage)protocolMessage);
            break;

        case PREPARE_STATEMENT:

            prepareStatementMessageCache.free((PrepareStatementMessage)protocolMessage);
            break;

        case EXECUTE_PREPARED_STATEMENT:

            executePreparedStatementMessageCache.free((ExecutePreparedStatementMessage)protocolMessage);
            break;

        case ADD_PREPARED_STATEMENT_BATCH:

            addPreparedStatementBatchMessageCache.free((AddPreparedStatementBatchMessage)protocolMessage);
            break;

        case EXECUTE_PREPARED_STATEMENT_BATCH:

            executePreparedStatementBatchMessageCache.free((ExecutePreparedStatementBatchMessage)protocolMessage);
            break;

        case FREE_PREPARED_STATEMENT:

            freePreparedStatementMessageCache.free((FreePreparedStatementMessage)protocolMessage);
            break;
        }
    }

    public <E extends Exception> void writeExecuteSQLResponseHeader(DataWriter<E> dataWriter) throws E {

        Objects.requireNonNull(dataWriter);

        throw new UnsupportedOperationException();
    }

    public <E extends Exception> void writeExecuteSQLResponseCompletion(DataWriter<E> dataWriter, long resultValue) throws E {

        Objects.requireNonNull(dataWriter);

        throw new UnsupportedOperationException();
    }

    public <E extends Exception> void writePrepareStatementResponse(DataWriter<E> dataWriter, int prepredStatementId) throws E {

        Objects.requireNonNull(dataWriter);
        Checks.isPreparedStatementId(prepredStatementId);

        throw new UnsupportedOperationException();
    }

    public <E extends Exception> void writeExecuteStatementResponse(DataWriter<E> dataWriter, int prepredStatementId, long resultValue) throws E {

        Objects.requireNonNull(dataWriter);
        Checks.isPreparedStatementId(prepredStatementId);

        throw new UnsupportedOperationException();
    }

    public <E extends Exception> void writeFreePreparedStatementResponse(DataWriter<E> dataWriter, int prepredStatementId) throws E {

        Objects.requireNonNull(dataWriter);
        Checks.isPreparedStatementId(prepredStatementId);

        throw new UnsupportedOperationException();
    }
}
