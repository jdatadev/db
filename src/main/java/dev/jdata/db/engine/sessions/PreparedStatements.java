package dev.jdata.db.engine.sessions;

import java.util.Objects;

import dev.jdata.db.engine.descriptorables.BaseSingleTypeDescriptorables;
import dev.jdata.db.engine.sessions.PreparedStatement.PreparedStatementState;
import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.ISQLString;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.checks.Checks;

final class PreparedStatements extends BaseSingleTypeDescriptorables<PreparedStatementState, PreparedStatement> implements IClearable {

    PreparedStatements() {
        super(PreparedStatement[]::new);
    }

    @Override
    public void clear() {

        forEach(PreparedStatement::clear);
    }

    public int addPreparedStatement(BaseSQLStatement sqlStatement, ISQLString sqlString) {

        Objects.requireNonNull(sqlStatement);
        Objects.requireNonNull(sqlString);

        final PreparedStatement preparedStatement = addDescriptorable(sqlStatement, p -> new PreparedStatement());

        preparedStatement.initialize(sqlStatement, sqlString);

        return preparedStatement.getPreparedStatementId();
    }

    public PreparedStatement getPreparedStatement(int preparedStatementId) {

        Checks.isPreparedStatementId(preparedStatementId);

        return getDescriptorable(preparedStatementId);
    }

    public void removePreparedStatement(int preparedStatementId) {

        Checks.isPreparedStatementId(preparedStatementId);

        final PreparedStatement preparedStatement = getPreparedStatement(preparedStatementId);

        preparedStatement.clear();

        removeDescriptorable(preparedStatement);
    }
}
