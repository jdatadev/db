package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;

abstract class BatchParameterPreparedStatementMessage extends ParameterPreparedStatementMessage {

    BatchParameterPreparedStatementMessage(AllocationType allocationType) {
        super(allocationType);
    }

    final void decodeParameters(ByteBuffer byteBuffer, int offset, int length, IProtocolAllocator protocolAllocator) {

        decodeParameters(byteBuffer, offset, length, protocolAllocator, (byte)Integer.BYTES);
    }
}
