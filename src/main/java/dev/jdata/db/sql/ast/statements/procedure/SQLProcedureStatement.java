package dev.jdata.db.sql.ast.statements.procedure;

import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.BaseSQLDDLOperationStatement;

abstract class SQLProcedureStatement extends BaseSQLDDLOperationStatement {

    SQLProcedureStatement(Context context, long operationKeyword, long procedureKeyword, long name) {
        super(context, operationKeyword, procedureKeyword, name);
    }
}
