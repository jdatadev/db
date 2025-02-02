package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.descriptorables.BaseDescriptorables;
import dev.jdata.db.schema.Table;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;

public final class Transaction extends BaseDescriptorable<Transaction.TransactionState> implements TransactionOperations<Transaction.TransactionSharedState> {

    private static final boolean DEBUG = DebugConstants.DEBUG_TRANSACTION;

    private static final boolean ASSERT = AssertionContants.ASSERT_TRANSACTION;

    public static final class TransactionSharedState {

        private final Object[] sharedStates;

        public TransactionSharedState(Object[] sharedStates) {

            Objects.requireNonNull(sharedStates);

            this.sharedStates = Array.copyOf(sharedStates);
        }
    }

    static enum TransactionState implements State {

        CREATED(true),
        SELECTED(false),
        WRITTEN_TO(false),
        COMMITED(true),
        ROLLED_BACK(true);

        private final boolean initializable;

        private TransactionState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    public static final int NO_TRANSACTION_DESCRIPTOR = BaseDescriptorables.NO_DESCRIPTOR;

    private long globalTransactionId;

    private final TransactionMechanism<?>[] transactionMechanisms;

    Transaction(TransactionMechanism<?> ... transactionMechanisms) {
        super(TransactionState.CREATED, DEBUG);

        this.transactionMechanisms = Array.copyOf(transactionMechanisms);
    }

    final void initialize(int transactionDescriptor, long globalTransactionId) {

        Checks.isTransactionDescriptor(transactionDescriptor);
        Checks.isTransactionId(globalTransactionId);

        super.initialize(transactionDescriptor);

        this.globalTransactionId = globalTransactionId;
    }

    @Override
    public OperationResult insertRows(TransactionSharedState sharedState, Table table, int statementId, LargeLongArray rowIds, DMLInsertRows rows) {

        return executeTransactionMechanisms(sharedState, table, statementId, rowIds, rows, (m, s, t, i, p1, p2) -> m.insertRows(s, t, i, p1, p2));
    }

    @Override
    public OperationResult updateRows(TransactionSharedState sharedState, Table table, int statementId, LargeLongArray rowIds, DMLUpdateRows rows) {

        return executeTransactionMechanisms(sharedState, table, statementId, rowIds, rows, (m, s, t, i, p1, p2) -> m.updateRows(s, t, i, p1, p2));
    }

    @Override
    public OperationResult updateAllRows(TransactionSharedState sharedState, Table table, int statementId, DMLUpdateRows row) {

        return executeTransactionMechanisms(sharedState, table, statementId, row, null, (m, s, t, i, p1, p2) -> m.updateAllRows(s, t, i, p1));
    }

    @Override
    public OperationResult deleteRows(TransactionSharedState sharedState, Table table, int statementId, LargeLongArray rowIds) {

        return executeTransactionMechanisms(sharedState, table, statementId, rowIds, null, (m, s, t, i, p1, p2) -> m.deleteRows(s, t, i, p1));
    }

    @Override
    public OperationResult deleteAllRows(TransactionSharedState sharedState, Table table, int statementId) {

        return executeTransactionMechanisms(sharedState, table, statementId, null, null, (m, s, t, i, p1, p2) -> m.deleteAllRows(s, t, i));
    }

    @Override
    public final void commit(TransactionSharedState sharedState) {

        try {
            executeTransactionMechanisms(sharedState, null, -1, null, null, (m, s, t, i, p1, p2) -> {

                m.commit(s);

                return null;
            });
        }
        finally {

            releaseTransaction(sharedState);

            setState(TransactionState.COMMITED);
        }
    }

    @Override
    public final void rollback(TransactionSharedState sharedState) {

        try {
            executeTransactionMechanisms(sharedState, null, -1, null, null, (m, s, t, i, p1, p2) -> {

                m.rollback(s);

                return null;
            });
        }
        finally {

            releaseTransaction(sharedState);

            setState(TransactionState.ROLLED_BACK);
        }
    }

    private interface TransactionMechanismRunnable<R, T, P1, P2> {

        R run(TransactionMechanism<T> mechanism, T sharedState, Table table, int statementId, P1 parameter1, P2 parameter2);
    }

    private <R, T, P1, P2> R executeTransactionMechanisms(TransactionSharedState sharedState, Table table, int statementId, P1 parameter1, P2 parameter2,
            TransactionMechanismRunnable<R, T, P1, P2> runnable) {

        final int numMechanisms = transactionMechanisms.length;

        final Object[] sharedStates = sharedState.sharedStates;

        Checks.areEqual(numMechanisms, sharedStates.length);

        for (int i = 0; i < numMechanisms; ++ i) {

            @SuppressWarnings("unchecked")
            final TransactionMechanism<T> transactionMechanism = (TransactionMechanism<T>) transactionMechanisms[i];

            @SuppressWarnings("unchecked")
            final T transactionMechanismSharedState = (T)sharedStates[i];

            runnable.run(transactionMechanism, transactionMechanismSharedState, table, statementId, parameter1, parameter2);
        }

        return null;
    }

    final long getGlobalTransactionId() {
        return globalTransactionId;
    }

    final int getTransactionDescriptor() {

        return getDescriptor();
    }

    private void releaseTransaction(TransactionSharedState sharedState) {

    }
}
