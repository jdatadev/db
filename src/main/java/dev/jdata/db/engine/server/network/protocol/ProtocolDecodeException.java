package dev.jdata.db.engine.server.network.protocol;

import dev.jdata.db.DBException;
import dev.jdata.db.utils.checks.Checks;

public final class ProtocolDecodeException extends DBException  {

    private static final long serialVersionUID = 1L;

    private final int offset;

    public ProtocolDecodeException(int offset) {

        this.offset = Checks.isOffset(offset);
    }

    public int getOffset() {
        return offset;
    }
}
