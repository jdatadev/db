package dev.jdata.db.engine.sessions;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.transactions.TransactionSelect.TransactionSelectAllocator;
import dev.jdata.db.utils.adt.maps.ILongToIntMapView;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSetAllocator;
import dev.jdata.db.utils.allocators.IArrayAllocator;
import dev.jdata.db.utils.allocators.IByteArrayByteBufferAllocator;
import dev.jdata.db.utils.checks.Checks;

public final class DMLUpdatingEvaluatorParameter extends BaseDMLUpdatingEvaluatorParameter {

    private final TransactionSelectAllocator transactionSelectAllocator;
    private final IMutableLongLargeSetAllocator largeLongSetAllocator;

    private final InsertRow[] insertRowsArray;

    @Deprecated
    private SQLExpressionEvaluator expressionEvaluator;

    private ILongToIntMapView columnIndexByColumnName;

    private long numUpdated;

    public DMLUpdatingEvaluatorParameter(IArrayAllocator<SQLExpressionEvaluator> expressionEvaluatorArrayAllocator, INumStorageBitsGetter numStorageBitsGetter,
            IByteArrayByteBufferAllocator byteArrayByteBufferAllocator, ILargeLongArrayAllocator largeLongArrayAllocator, TransactionSelectAllocator transactionSelectAllocator,
            IMutableLongLargeSetAllocator largeLongSetAllocator) {
        super(expressionEvaluatorArrayAllocator, numStorageBitsGetter, byteArrayByteBufferAllocator, largeLongArrayAllocator);

        this.transactionSelectAllocator = Objects.requireNonNull(transactionSelectAllocator);
        this.largeLongSetAllocator = Objects.requireNonNull(largeLongSetAllocator);

        this.insertRowsArray = new InsertRow[] {

                new InsertRow()
        };
    }

    @Override
    void evaluateParameterByIndex(int parameterIndex, SQLExpressionEvaluator dst) {

        throw new UnsupportedOperationException();
    }

    InsertRow[] getInsertRowsArray() {
        return insertRowsArray;
    }

    @Deprecated
    SQLExpressionEvaluator getExpressionEvaluator() {
        return expressionEvaluator;
    }

    @Deprecated
    void setExpressionEvaluator(SQLExpressionEvaluator expressionEvaluator) {

        this.expressionEvaluator = Objects.requireNonNull(expressionEvaluator);
    }

    long getNumUpdated() {
        return numUpdated;
    }

    void setNumUpdated(long numUpdated) {

        this.numUpdated = Checks.isNotNegative(numUpdated);
    }
}
