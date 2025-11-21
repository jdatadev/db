package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;

abstract class BaseExecutePreparedStatementMessage extends ParameterPreparedStatementMessage {

    BaseExecutePreparedStatementMessage(AllocationType allocationType) {
        super(allocationType);
    }

    final void decodeParameters(ByteBuffer byteBuffer, int offset, int length, IProtocolAllocator protocolAllocator) {

        decodeParameters(byteBuffer, offset, length, protocolAllocator, (byte)Short.BYTES);
    }
}
