package dev.jdata.db.engine.database;

import dev.jdata.db.sql.ast.statements.BaseSQLStatement;
import dev.jdata.db.sql.parse.SQLString;

public interface IDatabasePreparedStatements extends IDatabaseExecutePreparedStatement, IDatabaseFreePreparedStatement {

    int prepareStatement(int databaseId, int sessionId, BaseSQLStatement sqlStatement, SQLString sqlString);
}
