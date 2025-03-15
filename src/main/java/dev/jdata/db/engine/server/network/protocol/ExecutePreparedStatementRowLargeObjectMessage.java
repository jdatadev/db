package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

@Deprecated // makes sense to decode? can be more than one ByteBuffer
public final class ExecutePreparedStatementRowLargeObjectMessage extends PreparedStatementMessage {

    private ByteBuffer largeObjectByteBuffer;
    private int largeObjectOffset;
    private int largeObjectLength;

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.EXECUTE_PREPARED_STATEMENT_PARAMETER_ROW_LARGE_OBJECT;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException {

        Objects.requireNonNull(byteBuffer);
        Checks.isOffset(offset);
        Checks.isLengthAboveZero(length);

        int currentOffset = offset;
        int currentLength = length;

        final int numStatementIdBytes = decodeStatementId(byteBuffer, currentOffset, currentLength);

        currentOffset += numStatementIdBytes;
        currentLength -= numStatementIdBytes;

        this.largeObjectByteBuffer = byteBuffer;
        this.largeObjectOffset = currentOffset;
        this.largeObjectLength = Checks.isLengthAboveZero(length - (currentOffset - offset));
    }

    public ByteBuffer getLargeObjectByteBuffer() {
        return largeObjectByteBuffer;
    }

    public int getLargeObjectOffset() {
        return largeObjectOffset;
    }

    public int getLargeObjectLength() {
        return largeObjectLength;
    }
}
