package dev.jdata.db.engine.sessions;

import java.util.Objects;

import dev.jdata.db.engine.descriptorables.BaseDescriptorable;
import dev.jdata.db.engine.sessions.PreparedStatement.PreparedStatementState;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.State;
import dev.jdata.db.utils.adt.IClearable;

final class PreparedStatement extends BaseDescriptorable<PreparedStatementState> implements IClearable {

    public static enum PreparedStatementState implements State {

        CREATED(true);

        private final boolean initializable;

        private PreparedStatementState(boolean initializable) {

            this.initializable = initializable;
        }

        @Override
        public boolean isInitializable() {
            return initializable;
        }
    }

    private BaseSQLStatement sqlStatement;
    private ISQLString sqlString;

    PreparedStatement(AllocationType allocationType) {
        super(allocationType, PreparedStatementState.CREATED, false);
    }

    @Override
    public void clear() {

        this.sqlStatement = null;
    }

    void initialize(BaseSQLStatement sqlStatement, ISQLString sqlString) {

        this.sqlStatement = Objects.requireNonNull(sqlStatement);
        this.sqlString = Objects.requireNonNull(sqlString);
    }

    int getPreparedStatementId() {

        return getDescriptor();
    }

    BaseSQLStatement getSQLStatement() {
        return sqlStatement;
    }

    ISQLString getSQLString() {
        return sqlString;
    }
}
