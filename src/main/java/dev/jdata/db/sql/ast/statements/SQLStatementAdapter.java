package dev.jdata.db.sql.ast.statements;

import dev.jdata.db.sql.ast.statements.dml.BaseSQLSelectStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLDeleteStatement;
import dev.jdata.db.sql.ast.statements.dml.SQLInsertStatement;
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

public class SQLStatementAdapter<P, R, E extends Exception> implements SQLStatementVisitor<P, R, E> {

    protected SQLStatementAdapter() {

    }

    @Override
    public R onSelect(BaseSQLSelectStatement selectStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onInsert(SQLInsertStatement insertStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onUpdate(SQLUpdateStatement updateStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onDelete(SQLDeleteStatement deleteStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onCreateTable(SQLCreateTableStatement createTableStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onAlterTable(SQLAlterTableStatement alterTableStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onDropTable(SQLDropTableStatement dropTableStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onCreateIndex(SQLCreateIndexStatement createIndexStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onDropIndex(SQLDropIndexStatement dropIndexStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onCreateTrigger(SQLCreateTriggerStatement createTriggerStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onDropTrigger(SQLDropTriggerStatement dropTriggerStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onCreateProcedure(SQLCreateProcedureStatement createProcedureStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onDropProcedure(SQLDropProcedureStatement dropProcedureStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onCreateFunction(SQLCreateFunctionStatement createFunctionStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }

    @Override
    public R onDropFunction(SQLDropFunctionStatement dropFunctionStatement, P parameter) throws E {

        throw new UnsupportedOperationException();
    }
}
