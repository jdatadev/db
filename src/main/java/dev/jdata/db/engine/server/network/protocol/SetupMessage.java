package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.server.network.protocol.strings.CharString;
import dev.jdata.db.engine.server.network.protocol.strings.MutableString;
import dev.jdata.db.utils.enums.StringEnum;

public final class SetupMessage extends ProtocolMessage {

    public static enum DatabaseCreateMode {

        NEVER("never"),
        IF_NOT_EXISTS("if-not-exists"),
        DROP_AND_CREATE("drop-and-create");

        private final String uriParameter;

        private DatabaseCreateMode(String uriParameter) {
            this.uriParameter = uriParameter;
        }

        public String getURIParameter() {
            return uriParameter;
        }

        public static DatabaseCreateMode findCreateMode(CharSequence uriParameter) {

            Objects.requireNonNull(uriParameter);

            return StringEnum.findEnum(values(), uriParameter, DatabaseCreateMode::getURIParameter);
        }
    }

    private final MutableString databaseName;
    private final MutableString charsetName;

    private DatabaseCreateMode createMode;

    public SetupMessage() {

        this.databaseName = new MutableString();
        this.charsetName = new MutableString();
    }

    @Override
    public ProtocolMessageType getMessageType() {

        return ProtocolMessageType.SETUP;
    }

    public CharString getDatabaseName() {
        return databaseName;
    }

    public CharString getCharsetName() {
        return charsetName;
    }

    public DatabaseCreateMode getCreateMode() {
        return createMode;
    }

    @Override
    void decode(ByteBuffer byteBuffer, int offset, int length, CharsetDecoder charsetDecoder, ProtocolAllocator allocator) throws ProtocolDecodeException {

        int currentOffset = offset;
        int currentLength = length;

        final int numDatabaseNameBytes = decodeZeroTerminatedASCIIString(byteBuffer, currentOffset, Math.min(currentLength, DBConstants.MAX_DB_NAME_LENGTH), databaseName,
                allocator);

        currentOffset += numDatabaseNameBytes;
        currentLength -= numDatabaseNameBytes;

        final int numCharsetNameBytes = decodeZeroTerminatedASCIIString(byteBuffer, currentOffset, Math.min(currentLength, DBConstants.MAX_CHARSET_NAME_LENGTH), databaseName,
                allocator);
    }
}
