package dev.jdata.db.engine.server;

import java.nio.ByteBuffer;

@Deprecated
public interface ProtocolMessageVisitor<P, R> {

    R onConnect(ByteBuffer byteBuffer, P parameter);
}
