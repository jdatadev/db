package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

public final class SQLDropColumnsOperation extends SQLColumnOperation {

    private final long dropKeyword;
    private final ASTSingle<SQLColumnNames> names;

    public SQLDropColumnsOperation(Context context, long dropKeyword, SQLColumnNames names) {
        super(context);

        this.dropKeyword = checkIsKeyword(dropKeyword);
        this.names = makeSingle(names);
    }

    public long getDropKeyword() {
        return dropKeyword;
    }

    public SQLColumnNames getNames() {
        return names.get();
    }

    @Override
    public <T, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onDropColumn(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(names, recurseMode, iterator);
    }
}
