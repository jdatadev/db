package dev.jdata.db.engine.database;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter.ILargeLongSetAllocator;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.engine.sessions.ILargeLongArrayAllocator;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.TransactionSelect.TransactionSelectAllocator;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.sets.MutableLargeLongBucketSet;
import dev.jdata.db.utils.allocators.ArrayAllocator;
import dev.jdata.db.utils.allocators.ByteArrayByteBufferAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.ObjectCache;

public final class DatabasesAllocators implements IDatabasesAllocators {

    private static final class UtilAllocators {

        private static final int LARGE_OUTER_INITIAL_CAPACITY = 1;
        private static final int LARGE_INNER_CAPACITY_EXPONENT = 16;

        private final ObjectCache<LargeLongArray> largeLongArrayCache;
        private final NodeObjectCache<TransactionSelect> transactionSelectCache;
        private final ObjectCache<MutableLargeLongBucketSet> largeLongSetCache;

        private final ArrayAllocator<SQLExpressionEvaluator> expressionEvaluatorArrayAllocator;
        private final ByteArrayByteBufferAllocator byteArrayByteBufferAllocator;
        private final ILargeLongArrayAllocator largeLongArrayAllocator;
        private final TransactionSelectAllocator transactionSelectAllocator;
        private final ILargeLongSetAllocator largeLongSetAllocator;
        private final ArrayAllocator<InsertRow> insertRowArrayAllocator;

        UtilAllocators() {

            this.largeLongArrayCache = new ObjectCache<>(() -> new LargeLongArray(LARGE_OUTER_INITIAL_CAPACITY, LARGE_INNER_CAPACITY_EXPONENT), LargeLongArray[]::new);
            this.transactionSelectCache = new NodeObjectCache<>(TransactionSelect::new);
            this.largeLongSetCache = new ObjectCache<>(() -> new MutableLargeLongBucketSet(LARGE_OUTER_INITIAL_CAPACITY, LARGE_INNER_CAPACITY_EXPONENT), MutableLargeLongBucketSet[]::new);

            this.expressionEvaluatorArrayAllocator = new ArrayAllocator<>(SQLExpressionEvaluator[]::new);
            this.byteArrayByteBufferAllocator = new ByteArrayByteBufferAllocator();

            this.largeLongArrayAllocator = new ILargeLongArrayAllocator() {

                @Override
                public LargeLongArray allocateLargeLongArray() {

                    return largeLongArrayCache.allocate();
                }

                @Override
                public void freeLargeLongArray(LargeLongArray largeLongArray) {

                    largeLongArrayCache.free(largeLongArray);
                }
            };

            this.transactionSelectAllocator = new TransactionSelectAllocator() {

                @Override
                public TransactionSelect allocateTransactionSelect() {

                    return transactionSelectCache.allocate();
                }

                @Override
                public void freeTransactionSelect(TransactionSelect transactionSelect) {

                    transactionSelectAllocator.freeTransactionSelect(transactionSelect);
                }
            };

            this.largeLongSetAllocator = new ILargeLongSetAllocator() {

                @Override
                public MutableLargeLongBucketSet allocateLargeLongSet() {

                    return largeLongSetCache.allocate();
                }

                @Override
                public void freeLargeLongSet(MutableLargeLongBucketSet largeLongSet) {

                    largeLongSetCache.free(largeLongSet);
                }
            };

            this.insertRowArrayAllocator = new ArrayAllocator<>(InsertRow[]::new);
        }
    }

    private final UtilAllocators utilAllocators;

    private final NodeObjectCache<DMLUpdatingEvaluatorParameter> evaluatorParameterCache;
    private final NodeObjectCache<DMLUpdatingPreparedEvaluatorParameter> preparedStatementevaluatorParameterCache;

    public DatabasesAllocators(INumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(numStorageBitsGetter);

        this.utilAllocators = new UtilAllocators();

        this.evaluatorParameterCache = new NodeObjectCache<>(() -> new DMLUpdatingEvaluatorParameter(utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators.largeLongArrayAllocator, utilAllocators.transactionSelectAllocator,
                utilAllocators.largeLongSetAllocator));

        this.preparedStatementevaluatorParameterCache = new NodeObjectCache<>(() -> new DMLUpdatingPreparedEvaluatorParameter(utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators. largeLongArrayAllocator, utilAllocators.insertRowArrayAllocator));
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
