package dev.jdata.db.engine.database.allocators;

import java.util.Objects;

import dev.jdata.db.dml.DMLInsertRows.InsertRow;
import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.transactions.TransactionSelect;
import dev.jdata.db.engine.transactions.TransactionSelect.ITransactionSelectAllocator;
import dev.jdata.db.utils.adt.arrays.CachedObjectArrayAllocator;
import dev.jdata.db.utils.adt.arrays.ICachedMutableLongLargeArrayAllocator;
import dev.jdata.db.utils.adt.sets.ICachedMutableLongLargeSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.jdk.niobuffers.CachedByteArrayByteBufferAllocator;

final class CachedUtilAllocators extends UtilAllocators {

    final ICachedMutableLongLargeArrayAllocator mutableLongLargeArrayAllocator;
    private final NodeObjectCache<TransactionSelect> transactionSelectCache;

    final ICachedMutableLongLargeSetAllocator mutableLongLargeSetAllocator;

    final CachedObjectArrayAllocator<SQLExpressionEvaluator> expressionEvaluatorArrayAllocator;
    final CachedByteArrayByteBufferAllocator byteArrayByteBufferAllocator;
    final ITransactionSelectAllocator transactionSelectAllocator;
    final CachedObjectArrayAllocator<InsertRow> insertRowArrayAllocator;

    CachedUtilAllocators() {

        this.mutableLongLargeArrayAllocator = ICachedMutableLongLargeArrayAllocator.create();
        this.transactionSelectCache = new NodeObjectCache<>(TransactionSelect::new);

        this.mutableLongLargeSetAllocator = ICachedMutableLongLargeSetAllocator.create();

        this.expressionEvaluatorArrayAllocator = new CachedObjectArrayAllocator<>(SQLExpressionEvaluator[]::new);
        this.byteArrayByteBufferAllocator = new CachedByteArrayByteBufferAllocator();

        this.transactionSelectAllocator = new ITransactionSelectAllocator() {

            @Override
            public TransactionSelect allocateTransactionSelect() {

                return transactionSelectCache.allocate();
            }

            @Override
            public void freeTransactionSelect(TransactionSelect transactionSelect) {

                Objects.requireNonNull(transactionSelect);

                transactionSelectCache.free(transactionSelect);
            }
        };

        this.insertRowArrayAllocator = new CachedObjectArrayAllocator<>(InsertRow[]::new);
    }
}
