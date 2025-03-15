package dev.jdata.db.engine.database;

import java.util.List;
import java.util.Objects;

import dev.jdata.db.common.storagebits.NumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter.ILargeLongSetAllocator;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.engine.sessions.ILargeLongArrayAllocator;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.TransactionSelect.TransactionSelectAllocator;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.sets.LargeLongSet;
import dev.jdata.db.utils.allocators.ArrayAllocator;
import dev.jdata.db.utils.allocators.ByteArrayByteBufferAllocator;
import dev.jdata.db.utils.allocators.ListAllocator;
import dev.jdata.db.utils.allocators.ObjectCache;

public final class DatabasesAllocators implements IDatabasesAllocators {

    private static final class UtilAllocators {

        private static final int LARGE_OUTER_INITIAL_CAPACITY = 1;
        private static final int LARGE_INNER_CAPACITY_EXPONENT = 16;

        private final ObjectCache<LargeLongArray> largeLongArrayCache;
        private final ObjectCache<TransactionSelect> transactionSelectCache;
        private final ObjectCache<LargeLongSet> largeLongSetCache;

        private final ArrayAllocator<SQLExpressionEvaluator> expressionEvaluatorArrayAllocator;
        private final ByteArrayByteBufferAllocator byteArrayByteBufferAllocator;
        private final ILargeLongArrayAllocator largeLongArrayAllocator;
        private final TransactionSelectAllocator transactionSelectAllocator;
        private final ILargeLongSetAllocator largeLongSetAllocator;
        private final ArrayAllocator<InsertRow> insertRowArrayAllocator;

        UtilAllocators() {

            this.largeLongArrayCache = new ObjectCache<>(() -> new LargeLongArray(LARGE_OUTER_INITIAL_CAPACITY, LARGE_INNER_CAPACITY_EXPONENT), LargeLongArray[]::new);
            this.transactionSelectCache = new ObjectCache<>(TransactionSelect::new, TransactionSelect[]::new);
            this.largeLongSetCache = new ObjectCache<>(() -> new LargeLongSet(LARGE_OUTER_INITIAL_CAPACITY, LARGE_INNER_CAPACITY_EXPONENT), LargeLongSet[]::new);

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
                public LargeLongSet allocateLargeLongSet() {

                    return largeLongSetCache.allocate();
                }

                @Override
                public void freeLargeLongSet(LargeLongSet largeLongSet) {

                    largeLongSetCache.free(largeLongSet);
                }
            };

            this.insertRowArrayAllocator = new ArrayAllocator<>(InsertRow[]::new);
        }
    }

    private final UtilAllocators utilAllocators;

    private final ObjectCache<DMLUpdatingEvaluatorParameter> evaluatorParameterCache;
    private final ObjectCache<DMLUpdatingPreparedEvaluatorParameter> preparedStatementevaluatorParameterCache;
    private final ListAllocator listAllocator;

    public DatabasesAllocators(NumStorageBitsGetter numStorageBitsGetter) {

        Objects.requireNonNull(numStorageBitsGetter);

        this.utilAllocators = new UtilAllocators();

        this.evaluatorParameterCache = new ObjectCache<>(() -> new DMLUpdatingEvaluatorParameter(utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators.largeLongArrayAllocator, utilAllocators.transactionSelectAllocator,
                utilAllocators.largeLongSetAllocator), DMLUpdatingEvaluatorParameter[]::new);

        this.preparedStatementevaluatorParameterCache = new ObjectCache<>(() -> new DMLUpdatingPreparedEvaluatorParameter(utilAllocators.expressionEvaluatorArrayAllocator,
                numStorageBitsGetter, utilAllocators.byteArrayByteBufferAllocator, utilAllocators. largeLongArrayAllocator, utilAllocators.insertRowArrayAllocator),
                DMLUpdatingPreparedEvaluatorParameter[]::new);

        this.listAllocator = new ListAllocator(Object[]::new);
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

    @Override
    public <T> List<T> allocateList(int minimumCapacity) {

        return listAllocator.allocateList(minimumCapacity);
    }

    @Override
    public void freeList(List<?> list) {

        listAllocator.freeList(list);
    }
}
