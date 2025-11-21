package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class ExecutePreparedStatementRowCompletionMessage extends PreparedStatementMessage {

    ExecutePreparedStatementRowCompletionMessage(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.EXECUTE_PREPARED_STATEMENT_PARAMETER_ROW_COMPLETION;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, IProtocolAllocator allocator) throws ProtocolDecodeException {

        decodeStatementId(byteBuffer, offset, length);
    }
}
