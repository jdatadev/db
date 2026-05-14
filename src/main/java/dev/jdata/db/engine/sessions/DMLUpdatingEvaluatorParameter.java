package dev.jdata.db.engine.sessions;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.transactions.TransactionSelect.ITransactionSelectAllocator;
import dev.jdata.db.utils.adt.arrays.IArrayAllocator;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArrayAllocator;
import dev.jdata.db.utils.adt.maps.ILongToIntMapView;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSet;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSetAllocator;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.niobuffers.IByteArrayByteBufferAllocator;

public final class DMLUpdatingEvaluatorParameter<T extends IMutableLongLargeArray, U extends IMutableLongLargeSet> extends BaseDMLUpdatingEvaluatorParameter<T> {

    private final ITransactionSelectAllocator transactionSelectAllocator;
    private final IMutableLongLargeSetAllocator<U> mutableLargeLongSetAllocator;

    private final InsertRow[] insertRowsArray;

    @Deprecated
    private SQLExpressionEvaluator expressionEvaluator;

    private ILongToIntMapView columnIndexByColumnName;

    private long numUpdated;

    public DMLUpdatingEvaluatorParameter(AllocationType allocationType, IArrayAllocator<SQLExpressionEvaluator> expressionEvaluatorArrayAllocator,
            INumStorageBitsGetter numStorageBitsGetter, IByteArrayByteBufferAllocator byteArrayByteBufferAllocator,
            IMutableLongLargeArrayAllocator<T> mutableLongLargeArrayAllocator, ITransactionSelectAllocator transactionSelectAllocator,
            IMutableLongLargeSetAllocator<U> largeLongSetAllocator) {
        super(allocationType, expressionEvaluatorArrayAllocator, numStorageBitsGetter, byteArrayByteBufferAllocator, mutableLongLargeArrayAllocator);

        this.transactionSelectAllocator = Objects.requireNonNull(transactionSelectAllocator);
        this.mutableLargeLongSetAllocator = Objects.requireNonNull(largeLongSetAllocator);

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
