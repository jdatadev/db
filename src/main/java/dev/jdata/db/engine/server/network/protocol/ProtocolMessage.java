package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

import dev.jdata.db.data.RowDataNumBits.RowDataNumBitsAllocator;
import dev.jdata.db.engine.server.network.protocol.ParameterPreparedStatementMessage.JDBCTypeArrayAllocator;
import dev.jdata.db.engine.server.network.protocol.strings.MutableString;
import dev.jdata.db.engine.server.network.protocol.strings.MutableString.CharArrayAllocator;
import dev.jdata.db.utils.allocators.ICharBufferAllocator;
import dev.jdata.db.utils.allocators.ICopyByteBufferAllocator;

public abstract class ProtocolMessage {

    public interface ProtocolAllocator extends ICopyByteBufferAllocator, ICharBufferAllocator, CharArrayAllocator, JDBCTypeArrayAllocator, RowDataNumBitsAllocator {

    }

    public interface ProtocolMessageFreeable {

        void free(ProtocolAllocator allocator);
    }

    abstract void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException;

    public abstract ProtocolMessageType getMessageType();

    static int decodeZeroTerminatedASCIIString(ByteBuffer byteBuffer, int offset, int maxLength, MutableString mutableString, CharArrayAllocator allocator)
            throws ProtocolDecodeException {

        mutableString.clear();

        return appendZeroTerminatedString(byteBuffer, offset, maxLength, mutableString, allocator) + 1;
    }

    static int appendZeroTerminatedString(ByteBuffer byteBuffer, int offset, int maxLength, MutableString appendable, CharArrayAllocator allocator)
            throws ProtocolDecodeException {

        int currentOffset = offset;

        int characterCount = 0;

        for (;;) {

            final byte b = byteBuffer.get(currentOffset);

            if (b < 0) {

                throw new ProtocolDecodeException(currentOffset);
            }
            else if (b == 0) {

                break;
            }
            else {
                ++ characterCount;

                if (characterCount > maxLength) {

                    throw new ProtocolDecodeException(currentOffset);
                }

                final char c = (char)b;

                appendable.append(c, allocator);

                ++ currentOffset;
            }
        }

        return characterCount;
    }
}
