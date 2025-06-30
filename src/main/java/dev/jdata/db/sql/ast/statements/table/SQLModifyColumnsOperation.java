package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

public final class SQLModifyColumnsOperation extends SQLAlterTableOperation {

    private final long modifyKeyword;
    private final ASTList<SQLModifyColumn> columns;

    public SQLModifyColumnsOperation(Context context, long modifyKeyword, IIndexListGetters<SQLModifyColumn> columns) {
        super(context);

        this.modifyKeyword = checkIsKeyword(modifyKeyword);
        this.columns = makeList(columns);
    }

    public long getModifyKeyword() {
        return modifyKeyword;
    }

    public ASTList<SQLModifyColumn> getColumns() {
        return columns;
    }

    @Override
    public <T, R, E extends Exception> R visit(SQLAlterTableOperationVisitor<T, R, E> visitor, T parameter) throws E {

        return visitor.onModifyColumns(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columns, recurseMode, iterator);
    }
}
