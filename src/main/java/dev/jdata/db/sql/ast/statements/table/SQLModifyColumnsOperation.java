package dev.jdata.db.sql.ast.statements.table;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

public final class SQLModifyColumnsOperation extends SQLAlterTableOperation {

    private final long modifyKeyword;
    private final ASTList<SQLModifyColumn> columns;

    public SQLModifyColumnsOperation(Context context, long modifyKeyword, List<SQLModifyColumn> columns) {
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
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onModifyColumns(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columns, recurseMode, iterator);
    }
}
