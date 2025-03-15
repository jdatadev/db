package dev.jdata.db.engine.server.network.protocol;

import java.nio.ByteBuffer;
import java.sql.JDBCType;
import java.util.Objects;

import dev.jdata.db.data.RowDataNumBits;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.utils.checks.Checks;

abstract class ParameterPreparedStatementMessage extends PreparedStatementMessage implements PreparedStatementParameters {

    public interface JDBCTypeArrayAllocator {

        JDBCType[] allocateJDBCTypeArray(int numColumns);

        void allocateJDBCTypeArray(JDBCType[] jdbcType);
    }

    private int parametersNumRows;

    private RowDataNumBits parametersRowDataNumBits;
    private byte parametersReferredToNumBytes;

    private JDBCType[] parametersColumnTypes;

    private ByteBuffer parametersByteBuffer;
    private int parametersOffset;
    private int parametersLength;

    final void decodeParameters(ByteBuffer byteBuffer, int offset, int length, ProtocolAllocator protocolAllocator, byte referredToNumBytes) {

        Objects.requireNonNull(byteBuffer);
        Checks.isOffset(offset);
        Checks.isLengthAboveZero(length);
        Objects.requireNonNull(protocolAllocator);
        Checks.isAboveZero(referredToNumBytes);

        int currentOffset = offset;

        final int numColumns = Short.toUnsignedInt(byteBuffer.getShort(currentOffset));

        currentOffset += Short.BYTES;

        this.parametersColumnTypes = protocolAllocator.allocateJDBCTypeArray(numColumns);

        final RowDataNumBits rowDataNumBits = this.parametersRowDataNumBits = protocolAllocator.allocateRowDataNumBits();

        for (int i = 0; i < numColumns; ++ i) {

            final JDBCType jdbcType = parametersColumnTypes[i] = JDBCType.values()[byteBuffer.get(currentOffset)];

            final int numColumnBytes = getNumColumnBytes(jdbcType, referredToNumBytes);

            rowDataNumBits.addNumBits(numColumnBytes << 3);

            currentOffset += Byte.BYTES;
        }

        this.parametersNumRows = byteBuffer.getInt(currentOffset);

        this.parametersReferredToNumBytes = referredToNumBytes;

        currentOffset += Integer.BYTES;

        this.parametersByteBuffer = byteBuffer;
        this.parametersOffset = currentOffset;
        this.parametersLength = Checks.isLengthAboveZero(length - (currentOffset - offset));
    }

    @Override
    public final RowDataNumBits getParametersRowDataNumBits() {
        return parametersRowDataNumBits;
    }

    @Override
    public final int getParametersReferredToNumBytes() {
        return parametersReferredToNumBytes;
    }

    @Override
    public final int getParametersNumRows() {
        return parametersNumRows;
    }

    @Override
    public final ByteBuffer getParametersByteBuffer() {
        return parametersByteBuffer;
    }

    @Override
    public final int getParametersOffset() {
        return parametersOffset;
    }

    @Override
    public final int getParametersLength() {
        return parametersLength;
    }

    @Override
    public final JDBCType getParametersJDBCColumnType(int columnIndex) {

        return parametersColumnTypes[columnIndex];
    }

    @Override
    public int getParametersNumColumns() {

        return parametersRowDataNumBits.getNumColumns();
    }

    private static int getNumColumnBytes(JDBCType jdbcType, byte referredToBytes) {

        final int result;

        switch (jdbcType) {

        case TINYINT:

            result = Byte.BYTES;
            break;

        case SMALLINT:

            result = Short.BYTES;
            break;

        case INTEGER:

            result = Integer.BYTES;
            break;

        case BIGINT:

            result = Integer.BYTES;
            break;

        case FLOAT:

            result = Float.BYTES;
            break;

        case DOUBLE:

            result = Double.BYTES;
            break;

        case BIT:
        case BOOLEAN:

            result = Byte.BYTES;
            break;

        case DECIMAL:

            result = referredToBytes;
            break;

        case CHAR:
        case NCHAR:
        case VARCHAR:
        case LONGVARCHAR:
        case NVARCHAR:
        case LONGNVARCHAR:

            result = referredToBytes;
            break;

        case BLOB:
        case CLOB:
        case NCLOB:

            result = referredToBytes;
            break;

        case BINARY:
        case VARBINARY:
        case LONGVARBINARY:

            result = referredToBytes;
            break;

        case DATE:

            result = Integer.BYTES;
            break;

        case TIME:

            result = Long.BYTES;
            break;

        case TIME_WITH_TIMEZONE:

            result = referredToBytes;
            break;

        case TIMESTAMP:

            result = Long.BYTES;
            break;

        case TIMESTAMP_WITH_TIMEZONE:

            result = referredToBytes;
            break;

        case NULL:

            result = Byte.BYTES;
            break;

        default:
            throw new UnsupportedOperationException();
        }

        return result;
    }
}
