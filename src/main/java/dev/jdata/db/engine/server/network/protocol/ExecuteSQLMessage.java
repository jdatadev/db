package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class ExecuteSQLMessage extends SQLProtocolMessage {

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.EXECUTE_SQL;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException {

        decodeSQL(byteBuffer, offset, length, charsetDecoder, allocator);
    }
}
