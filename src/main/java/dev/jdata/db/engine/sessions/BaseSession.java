package dev.jdata.db.engine.sessions;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.checks.AssertionContants;

abstract class BaseSession extends BaseDescriptorable<BaseSession.SessionState> implements Session {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_SESSION;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_SESSION;

    static enum SessionState implements State {

        CREATED(true),
        CONNECTED(false),
        DISCONNECTED(true);

        private final boolean initializable;

        private SessionState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    private int currentTransaction;

    BaseSession() {
        super(SessionState.CREATED, DEBUG);
    }

    final void commitTransaction() {

        clearTransaction();
    }

    final void rollbackTransaction() {

        clearTransaction();
    }

    private void clearTransaction() {

        if (currentTransaction == Transaction.NO_TRANSACTION_DESCRIPTOR) {

            throw new IllegalStateException();
        }

        this.currentTransaction = Transaction.NO_TRANSACTION_DESCRIPTOR;
    }
}
