package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;

import dev.jdata.db.data.RowDataNumBits.IRowDataNumBitsAllocator;
import dev.jdata.db.engine.server.network.protocol.ParameterPreparedStatementMessage.IJDBCTypeArrayAllocator;
import dev.jdata.db.engine.server.network.protocol.strings.MutableString;
import dev.jdata.db.engine.server.network.protocol.strings.MutableString.ICharArrayAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.jdk.niobuffers.ICharBufferAllocator;
import dev.jdata.db.utils.jdk.niobuffers.ICopyByteBufferAllocator;

public abstract class ProtocolMessage extends ObjectCacheNode {

    public interface IProtocolAllocator extends ICopyByteBufferAllocator, ICharBufferAllocator, ICharArrayAllocator, IJDBCTypeArrayAllocator, IRowDataNumBitsAllocator {

    }

    public interface IProtocolMessageFreeable {

        void free(IProtocolAllocator allocator);
    }

    abstract void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, IProtocolAllocator allocator) throws ProtocolDecodeException;

    public abstract ProtocolMessageType getMessageType();

    static int decodeZeroTerminatedASCIIString(ByteBuffer byteBuffer, int offset, int maxLength, MutableString mutableString, ICharArrayAllocator allocator)
            throws ProtocolDecodeException {

        mutableString.clear();

        return appendZeroTerminatedString(byteBuffer, offset, maxLength, mutableString, allocator) + 1;
    }

    static int appendZeroTerminatedString(ByteBuffer byteBuffer, int offset, int maxLength, MutableString appendable, ICharArrayAllocator allocator)
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

    ProtocolMessage(AllocationType allocationType) {
        super(allocationType);
    }
}
