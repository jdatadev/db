package dev.jdata.db.sql.ast.statements.table;

import java.util.List;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

public final class SQLAddColumnsOperation extends SQLColumnOperation {

    private final long addKeyword;
    private final ASTList<SQLAddColumnDefinition> columnDefinition;

    public SQLAddColumnsOperation(Context context, long addKeyword, List<SQLAddColumnDefinition> addColumnOperation) {
        super(context);

        this.addKeyword = checkIsKeyword(addKeyword);
        this.columnDefinition = makeList(addColumnOperation);
    }

    public long getAddKeyword() {
        return addKeyword;
    }

    public ASTList<SQLAddColumnDefinition> getColumnDefinition() {
        return columnDefinition;
    }

    @Override
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onAddColumn(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnDefinition, recurseMode, iterator);
    }
}
