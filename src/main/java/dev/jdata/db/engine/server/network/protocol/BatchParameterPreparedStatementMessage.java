package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;

abstract class BatchParameterPreparedStatementMessage extends ParameterPreparedStatementMessage {

    final void decodeParameters(ByteBuffer byteBuffer, int offset, int length, ProtocolAllocator protocolAllocator) {

        decodeParameters(byteBuffer, offset, length, protocolAllocator, (byte)Integer.BYTES);
    }
}
