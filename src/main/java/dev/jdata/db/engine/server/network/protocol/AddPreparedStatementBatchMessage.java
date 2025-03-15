package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class AddPreparedStatementBatchMessage extends BatchParameterPreparedStatementMessage {

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.ADD_PREPARED_STATEMENT_BATCH;
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
