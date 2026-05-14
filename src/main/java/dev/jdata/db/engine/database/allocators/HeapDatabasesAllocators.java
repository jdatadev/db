package dev.jdata.db.engine.database.allocators;

import java.util.Objects;

import dev.jdata.db.common.storagebits.INumStorageBitsGetter;
import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.sessions.DMLUpdatingEvaluatorParameter;
import dev.jdata.db.engine.sessions.DMLUpdatingPreparedEvaluatorParameter;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.TransactionSelect.ITransactionSelectAllocator;
import dev.jdata.db.schema.allocators.databases.schemamanagement.HeapDatabaseSchemaManagementAllocators;
import dev.jdata.db.schema.model.diff.dropped.HeapSchemaDroppedElementsAllocators;
import dev.jdata.db.utils.adt.arrays.HeapObjectArrayAllocator;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArrayAllocator;
import dev.jdata.db.utils.adt.maps.IHeapMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IHeapMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IHeapMutableIntSet;
import dev.jdata.db.utils.adt.sets.IHeapMutableIntSetAllocator;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongLargeSet;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongLargeSetAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.jdk.niobuffers.HeapByteArrayByteBufferAllocator;

public final class HeapDatabasesAllocators extends DatabasesAllocators<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> {

    private static final HeapObjectArrayAllocator<SQLExpressionEvaluator> SQL_EXPRESSION_ARRAY_ALLOCATOR = new HeapObjectArrayAllocator<>(SQLExpressionEvaluator[]::new);
    private static final HeapByteArrayByteBufferAllocator BYTE_ARRAY_BYTE_BUFFER_ALLOCATOR = HeapByteArrayByteBufferAllocator.INSTANCE;
    private static final IHeapMutableLongLargeArrayAllocator MUTABLE_LONG_LARGE_ARRAY_ALLOCATOR = IHeapMutableLongLargeArrayAllocator.create();

    private final INumStorageBitsGetter numStorageBitsGetter;

    private final ITransactionSelectAllocator transactionSelectAllocator;

    public HeapDatabasesAllocators(INumStorageBitsGetter numStorageBitsGetter) {
        super(createDatabaseSchemaManagementAllocators());

        this.numStorageBitsGetter = Objects.requireNonNull(numStorageBitsGetter);

        this.transactionSelectAllocator = new ITransactionSelectAllocator() {

            @Override
            public TransactionSelect allocateTransactionSelect() {

                return new TransactionSelect(AllocationType.HEAP_ALLOCATOR);
            }

            @Override
            public void freeTransactionSelect(TransactionSelect transactionSelect) {

                Objects.requireNonNull(transactionSelect);
            }
        };
    }

    private static HeapDatabaseSchemaManagementAllocators<IHeapMutableIntSet, IHeapMutableIntToObjectWithRemoveStaticMap<IHeapMutableIntSet>>
    createDatabaseSchemaManagementAllocators() {

        return new HeapDatabaseSchemaManagementAllocators<>(
                new HeapSchemaDroppedElementsAllocators<>(
                        IHeapMutableIntSetAllocator.create(),
                        IHeapMutableIntToObjectWithRemoveStaticMapAllocator.create(IHeapMutableIntSet[]::new)));
    }

    @Override
    public DMLUpdatingPreparedEvaluatorParameter<IHeapMutableLongLargeArray> allocateDMLPreparedStatementEvaluatorParameter() {

        return new DMLUpdatingPreparedEvaluatorParameter<IHeapMutableLongLargeArray>(AllocationType.HEAP_ALLOCATOR, SQL_EXPRESSION_ARRAY_ALLOCATOR, numStorageBitsGetter,
                BYTE_ARRAY_BYTE_BUFFER_ALLOCATOR, MUTABLE_LONG_LARGE_ARRAY_ALLOCATOR, new HeapObjectArrayAllocator<>(InsertRow[]::new));
    }

    @Override
    public void freeDMLPreparedStatementEvaluatorParameter(DMLUpdatingPreparedEvaluatorParameter<IHeapMutableLongLargeArray> evaluatorParameter) {

        Objects.requireNonNull(evaluatorParameter);
    }

    @Override
    public DMLUpdatingEvaluatorParameter<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> allocateDMLEvaluatorParameter() {

        return new DMLUpdatingEvaluatorParameter<>(AllocationType.HEAP_ALLOCATOR, SQL_EXPRESSION_ARRAY_ALLOCATOR, numStorageBitsGetter, BYTE_ARRAY_BYTE_BUFFER_ALLOCATOR,
                MUTABLE_LONG_LARGE_ARRAY_ALLOCATOR, transactionSelectAllocator, IHeapMutableLongLargeSetAllocator.create());
    }

    @Override
    public void freeDMLEvaluatorParameter(DMLUpdatingEvaluatorParameter<IHeapMutableLongLargeArray, IHeapMutableLongLargeSet> evaluatorParameter) {

        Objects.requireNonNull(evaluatorParameter);
    }
}
