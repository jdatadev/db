package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class ExecutePreparedStatementMessage extends BaseExecutePreparedStatementMessage {

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.EXECUTE_PREPARED_STATEMENT;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException {

        int currentOffset = offset;
        int currentLength = length;

        final int numStatementIdBytes = decodeStatementId(byteBuffer, currentOffset, currentLength);

        currentOffset += numStatementIdBytes;
        currentLength -= numStatementIdBytes;

        decodeParameters(byteBuffer, currentOffset, currentLength, allocator);
    }
}
