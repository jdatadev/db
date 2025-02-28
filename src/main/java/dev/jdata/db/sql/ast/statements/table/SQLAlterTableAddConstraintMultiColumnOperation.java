package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

abstract class SQLAlterTableAddConstraintMultiColumnOperation extends SQLAlterTableAddConstraintOperation {

    private final ASTSingle<SQLColumnNames> columnNames;

    SQLAlterTableAddConstraintMultiColumnOperation(Context context, long addKeyword, long constraintKeyword, SQLColumnNames columnNames, long name) {
        super(context, addKeyword, constraintKeyword, name);

        this.columnNames = makeSingle(columnNames);
    }

    public final SQLColumnNames getColumnNames() {
        return columnNames.get();
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnNames, recurseMode, iterator);
    }
}
