package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

public final class ExecuteSQLMessage extends SQLProtocolMessage {

    ExecuteSQLMessage(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.EXECUTE_SQL;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, IProtocolAllocator allocator) throws ProtocolDecodeException {

        decodeSQL(byteBuffer, offset, length, charsetDecoder, allocator);
    }
}
