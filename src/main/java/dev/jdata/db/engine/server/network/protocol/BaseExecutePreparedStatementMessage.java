package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;

abstract class BaseExecutePreparedStatementMessage extends ParameterPreparedStatementMessage {

    final void decodeParameters(ByteBuffer byteBuffer, int offset, int length, ProtocolAllocator protocolAllocator) {

        decodeParameters(byteBuffer, offset, length, protocolAllocator, (byte)Short.BYTES);
    }
}
