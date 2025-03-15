package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class FreePreparedStatementMessage extends PreparedStatementMessage {

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.FREE_PREPARED_STATEMENT;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException {

        decodeStatementId(byteBuffer, offset, length);
    }
}
