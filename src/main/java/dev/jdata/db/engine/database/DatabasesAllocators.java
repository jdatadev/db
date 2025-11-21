package dev.jdata.db.engine.database;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.TransactionSelect.ITransactionSelectAllocator;
import dev.jdata.db.utils.adt.arrays.ICachedMutableLongLargeArrayAllocator;
import dev.jdata.db.utils.adt.arrays.ObjectArrayAllocator;
import dev.jdata.db.utils.adt.sets.ICachedMutableLongLargeSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.jdk.niobuffers.ByteArrayByteBufferAllocator;

public final class DatabasesAllocators implements IDatabasesAllocators {

    private static final class UtilAllocators {

        private static final int LARGE_OUTER_INITIAL_CAPACITY = 1;
        private static final int LARGE_INNER_CAPACITY_EXPONENT = 16;

        private final ICachedMutableLongLargeArrayAllocator mutableLongLargeArrayAllocator;
        private final NodeObjectCache<TransactionSelect> transactionSelectCache;

        private final ICachedMutableLongLargeSetAllocator mutableLongLargeSetAllocator;

        private final ObjectArrayAllocator<SQLExpressionEvaluator> expressionEvaluatorArrayAllocator;
        private final ByteArrayByteBufferAllocator byteArrayByteBufferAllocator;
        private final ITransactionSelectAllocator transactionSelectAllocator;
        private final ObjectArrayAllocator<InsertRow> insertRowArrayAllocator;

        UtilAllocators() {

            this.mutableLongLargeArrayAllocator = ICachedMutableLongLargeArrayAllocator.create();
            this.transactionSelectCache = new NodeObjectCache<>(TransactionSelect::new);

            this.mutableLongLargeSetAllocator = ICachedMutableLongLargeSetAllocator.create();

            this.expressionEvaluatorArrayAllocator = new ObjectArrayAllocator<>(SQLExpressionEvaluator[]::new);
            this.byteArrayByteBufferAllocator = new ByteArrayByteBufferAllocator();

            this.transactionSelectAllocator = new ITransactionSelectAllocator() {

                @Override
                public TransactionSelect allocateTransactionSelect() {

                    return transactionSelectCache.allocate();
                }

                @Override
                public void freeTransactionSelect(TransactionSelect transactionSelect) {

                    transactionSelectAllocator.freeTransactionSelect(transactionSelect);
                }
            };

            this.insertRowArrayAllocator = new ObjectArrayAllocator<>(InsertRow[]::new);
        }
    }

    private final UtilAllocators utilAllocators;

    private final NodeObjectCache<DMLUpdatingEvaluatorParameter> evaluatorParameterCache;
    private final NodeObjectCache<DMLUpdatingPreparedEvaluatorParameter> preparedStatementevaluatorParameterCache;

    public DatabasesAllocators(INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(numStorageBitsGetter);

        this.utilAllocators = new UtilAllocators();

        this.evaluatorParameterCache = new NodeObjectCache<>(a -> new DMLUpdatingEvaluatorParameter(a, utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators.mutableLongLargeArrayAllocator, utilAllocators.transactionSelectAllocator,
                utilAllocators.mutableLongLargeSetAllocator));

        this.preparedStatementevaluatorParameterCache = new NodeObjectCache<>(a -> new DMLUpdatingPreparedEvaluatorParameter(a, utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators.mutableLongLargeArrayAllocator, utilAllocators.insertRowArrayAllocator));
    }

    @Override
    public DMLUpdatingEvaluatorParameter allocateDMLEvaluatorParameter() {

        return evaluatorParameterCache.allocate();
    }

    @Override
    public void freeDMLEvaluatorParameter(DMLUpdatingEvaluatorParameter evaluatorParameter) {

        evaluatorParameterCache.free(evaluatorParameter);
    }

    @Override
    public DMLUpdatingPreparedEvaluatorParameter allocateDMLPreparedStatementEvaluatorParameter() {

        return preparedStatementevaluatorParameterCache.allocate();
    }

    @Override
    public void freeDMLPreparedStatementEvaluatorParameter(DMLUpdatingPreparedEvaluatorParameter evaluatorParameter) {

        preparedStatementevaluatorParameterCache.free(evaluatorParameter);
    }
}
