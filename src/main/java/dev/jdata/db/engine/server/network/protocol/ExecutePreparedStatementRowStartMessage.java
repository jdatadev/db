package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class ExecutePreparedStatementRowStartMessage extends BaseExecutePreparedStatementMessage {

    ExecutePreparedStatementRowStartMessage(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.EXECUTE_PREPARED_STATEMENT_PARAMETER_ROW_START;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, IProtocolAllocator allocator) throws ProtocolDecodeException {

        int currentOffset = offset;
        int currentLength = length;

        final int numStatementIdBytes = decodeStatementId(byteBuffer, currentOffset, currentLength);

        currentOffset += numStatementIdBytes;
        currentLength -= numStatementIdBytes;

        decodeParameters(byteBuffer, currentOffset, currentLength, allocator);
    }
}
