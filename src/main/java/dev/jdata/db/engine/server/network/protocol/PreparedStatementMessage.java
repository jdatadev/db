package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;

abstract class PreparedStatementMessage extends ProtocolMessage {

    private int statementId;

    PreparedStatementMessage(AllocationType allocationType) {
        super(allocationType);
    }

    public int getStatementId() {
        return statementId;
    }

    final int decodeStatementId(ByteBuffer byteBuffer, int offset, int length) throws ProtocolDecodeException {

        final int numStatementIdBytes = Integer.BYTES;

        if (length < numStatementIdBytes) {

            throw new ProtocolDecodeException(offset);
        }

        this.statementId = byteBuffer.getInt(offset);

        return numStatementIdBytes;
    }
}
