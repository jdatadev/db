package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;

import dev.jdata.db.engine.server.network.protocol.ProtocolMessage.IProtocolMessageFreeable;

public abstract class SQLProtocolMessage extends ProtocolMessage implements IProtocolMessageFreeable {

    private CharBuffer sql;

    SQLProtocolMessage(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final void free(IProtocolAllocator allocator) {

        if (sql != null) {

            allocator.freeCharBuffer(sql);

            this.sql = null;
        }
    }

    public final CharBuffer getSQL() {
        return sql;
    }

    final void decodeSQL(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, IProtocolAllocator allocator) throws ProtocolDecodeException {

        this.sql = decodeCharBufferFromByteBuffer(byteBuffer, offset, length, charsetDecoder, allocator);
    }

    private static CharBuffer decodeCharBufferFromByteBuffer(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, IProtocolAllocator allocator)
            throws ProtocolDecodeException {

        final CharBuffer charBuffer = allocator.allocateForDecodeBytes(length);

        final ByteBuffer decodeByteBuffer = allocator.allocate(byteBuffer, offset, length);

        try {
            if (charsetDecoder.decode(byteBuffer, charBuffer, true).isError()) {

                throw new ProtocolDecodeException(offset);
            }
        }
        finally {

            allocator.freeByteBuffer(decodeByteBuffer);
        }

        return charBuffer;
    }
}
