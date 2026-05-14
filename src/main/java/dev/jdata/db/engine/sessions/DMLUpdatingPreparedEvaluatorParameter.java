package dev.jdata.db.engine.sessions;

import java.nio.ByteBuffer;
import java.sql.JDBCType;
import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.sessions.Session.PreparedStatementParameters;
import dev.jdata.db.utils.adt.arrays.IArrayAllocator;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArrayAllocator;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.niobuffers.IByteArrayByteBufferAllocator;

public final class DMLUpdatingPreparedEvaluatorParameter<T extends IMutableLongLargeArray> extends BaseDMLUpdatingEvaluatorParameter<T> {

    private final IArrayAllocator<InsertRow> insertRowArrayAllocator;

    private PreparedStatementParameters preparedStatementParameters;
    private int numBytesPerRow;
    private int rowIndex;

    private long result;

    public DMLUpdatingPreparedEvaluatorParameter(AllocationType allocationType, IArrayAllocator<SQLExpressionEvaluator> arrayAllocator,
            INumStorageBitsGetter numStorageBitsGetter, IByteArrayByteBufferAllocator byteArrayByteBufferAllocator,
            IMutableLongLargeArrayAllocator<T> mutableLongLargeArrayAllocator, IArrayAllocator<InsertRow> insertRowArrayAllocator) {
        super(allocationType, arrayAllocator, numStorageBitsGetter, byteArrayByteBufferAllocator, mutableLongLargeArrayAllocator);

        this.insertRowArrayAllocator = Objects.requireNonNull(insertRowArrayAllocator);
    }

    @Override
    void evaluateParameterByIndex(int parameterIndex, SQLExpressionEvaluator dst) {

        Checks.isNotNegative(parameterIndex);
        Objects.requireNonNull(dst);

        final ByteBuffer byteBuffer = preparedStatementParameters.getParametersByteBuffer();

        final int byteBufferOffset = rowIndex * numBytesPerRow;

        final JDBCType jdbcType = preparedStatementParameters.getParametersJDBCColumnType(parameterIndex);

        dst.setValue(byteBuffer, byteBufferOffset, jdbcType, parameterIndex);
    }

    void setPreparedStatementParameters(PreparedStatementParameters preparedStatementParameters) {

        this.preparedStatementParameters = Objects.requireNonNull(preparedStatementParameters);
        this.numBytesPerRow = preparedStatementParameters.computeNumBytesPerRow();
    }

    PreparedStatementParameters getPreparedStatementParameters() {
        return preparedStatementParameters;
    }

    void setRowIndex(int rowIndex) {

        this.rowIndex = Checks.isIntIndex(rowIndex);
    }

    long getResult() {
        return result;
    }

    void setResult(long result) {
        this.result = result;
    }

    InsertRow[] allocateInsertRowsArray(int minimumCapacity) {

        return insertRowArrayAllocator.allocateArray(minimumCapacity);
    }

    void freeInsertRowsArray(InsertRow[] insertRowArray) {

        insertRowArrayAllocator.freeArray(insertRowArray);
    }
}
