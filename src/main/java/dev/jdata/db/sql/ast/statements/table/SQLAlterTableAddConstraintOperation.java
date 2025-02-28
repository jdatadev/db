package dev.jdata.db.sql.ast.statements.table;

import org.jutils.parse.context.Context;

public abstract class SQLAlterTableAddConstraintOperation extends SQLAlterTableConstraintNameOperation {

    SQLAlterTableAddConstraintOperation(Context context, long addKeyword, long constraintKeyword, long name) {
        super(context, addKeyword, constraintKeyword, name);
    }
}
