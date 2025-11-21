package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.descriptorables.BaseDescriptorables;
import dev.jdata.db.engine.transactions.Transaction.TransactionState;
import dev.jdata.db.engine.transactions.TransactionDMLOperations.OperationResult;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongByIndexOrderedElementsView;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSet;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;

public final class Transaction extends BaseDescriptorable<TransactionState> {

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

    public Transaction(AllocationType allocationType, TransactionMechanism<?> ... transactionMechanisms) {
        super(allocationType, TransactionState.CREATED, DEBUG);

        this.transactionMechanisms = Array.copyOf(transactionMechanisms);
    }

    final void initialize(int transactionDescriptor, long globalTransactionId) {

        Checks.isTransactionDescriptor(transactionDescriptor);
        Checks.isTransactionId(globalTransactionId);

        super.initialize(transactionDescriptor);

        this.globalTransactionId = globalTransactionId;
    }

    public boolean select(TransactionSelect select, IMutableLongLargeSet addedRowIdsDst, IMutableLongLargeSet removedRowIdsDst) {

        throw new UnsupportedOperationException();
    }

    private TransactionSharedState getSharedState() {

        throw new UnsupportedOperationException();
    }

    private int generateStatementId() {

        throw new UnsupportedOperationException();
    }

    public OperationResult insertRows(Table table, ILongByIndexOrderedElementsView rowIds, DMLInsertRows rows) {

        return executeTransactionMechanisms(getSharedState(), table, generateStatementId(), rowIds, rows, (m, s, t, i, p1, p2) -> m.insertRows(s, t, i, p1, p2));
    }

    public OperationResult updateRows(Table table, ILongByIndexOrderedElementsView rowIds, DMLUpdateRows rows) {

        return executeTransactionMechanisms(getSharedState(), table, generateStatementId(), rowIds, rows, (m, s, t, i, p1, p2) -> m.updateRows(s, t, i, p1, p2));
    }

    public OperationResult updateAllRows(Table table, DMLUpdateRows row) {

        return executeTransactionMechanisms(getSharedState(), table, generateStatementId(), row, null, (m, s, t, i, p1, p2) -> m.updateAllRows(s, t, i, p1));
    }

    public OperationResult deleteRows(Table table, ILongByIndexOrderedElementsView rowIds) {

        return executeTransactionMechanisms(getSharedState(), table, generateStatementId(), rowIds, null, (m, s, t, i, p1, p2) -> m.deleteRows(s, t, i, p1));
    }

    public OperationResult deleteAllRows(Table table) {

        return executeTransactionMechanisms(getSharedState(), table, generateStatementId(), null, null, (m, s, t, i, p1, p2) -> m.deleteAllRows(s, t, i));
    }

    public final void commit() {

        final TransactionSharedState sharedState = getSharedState();

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

    public final void rollback() {

        final TransactionSharedState sharedState = getSharedState();

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
