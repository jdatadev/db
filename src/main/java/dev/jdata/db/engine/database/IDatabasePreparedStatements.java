package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.sql.ast.statements.BaseSQLStatement;

public interface IDatabasePreparedStatements extends IDatabaseExecutePreparedStatement, IDatabaseFreePreparedStatement {

    int prepareStatement(int databaseId, int sessionId, BaseSQLStatement sqlStatement, long sqlString, StringResolver parserStringResolver);
}
