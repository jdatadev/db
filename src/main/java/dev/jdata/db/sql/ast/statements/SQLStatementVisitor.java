package dev.jdata.db.sql.ast.statements;

import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLInsertStatement;
import dev.jdata.db.sql.ast.statements.dml.BaseSQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLUpdateStatement;
import dev.jdata.db.sql.ast.statements.function.SQLCreateFunctionStatement;
import dev.jdata.db.sql.ast.statements.function.SQLDropFunctionStatement;
import dev.jdata.db.sql.ast.statements.index.SQLCreateIndexStatement;
import dev.jdata.db.sql.ast.statements.index.SQLDropIndexStatement;
import dev.jdata.db.sql.ast.statements.procedure.SQLCreateProcedureStatement;
import dev.jdata.db.sql.ast.statements.procedure.SQLDropProcedureStatement;
import dev.jdata.db.sql.ast.statements.table.SQLAlterTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLCreateTableStatement;
import dev.jdata.db.sql.ast.statements.table.SQLDropTableStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLCreateTriggerStatement;
import dev.jdata.db.sql.ast.statements.trigger.SQLDropTriggerStatement;

public interface SQLStatementVisitor<T, R> {

    R onSelect(BaseSQLSelectStatement selectStatement, T parameter);
    R onInsert(SQLInsertStatement insertStatement, T parameter);
    R onUpdate(SQLUpdateStatement updateStatement, T parameter);
    R onDelete(SQLDeleteStatement deleteStatement, T parameter);

    R onCreateTable(SQLCreateTableStatement createTableStatement, T parameter);
    R onAlterTable(SQLAlterTableStatement alterTableStatement, T parameter);
    R onDropTable(SQLDropTableStatement dropTableStatement, T parameter);

    R onCreateIndex(SQLCreateIndexStatement createIndexStatement, T parameter);
    R onDropIndex(SQLDropIndexStatement dropIndexStatement, T parameter);

    R onCreateTrigger(SQLCreateTriggerStatement createTriggerStatement, T parameter);
    R onDropTrigger(SQLDropTriggerStatement dropTriggerStatement, T parameter);

    R onCreateProcedure(SQLCreateProcedureStatement createProcedureStatement, T parameter);
    R onDropProcedure(SQLDropProcedureStatement dropProcedureStatement, T parameter);

    R onCreateFunction(SQLCreateFunctionStatement createFunctionStatement, T parameter);
    R onDropFunction(SQLDropFunctionStatement dropFunctionStatement, T parameter);
}
