package dev.jdata.db.sql.ast.statements.table;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.ast.objects.list.IIndexListGetters;
import org.jutils.parse.context.Context;

public final class SQLAddColumnsOperation extends SQLColumnOperation {

    private final long addKeyword;
    private final ASTList<SQLAddColumnDefinition> columnDefinitions;

    public SQLAddColumnsOperation(Context context, long addKeyword, IIndexListGetters<SQLAddColumnDefinition> addColumnDefinitions) {
        super(context);

        this.addKeyword = checkIsKeyword(addKeyword);
        this.columnDefinitions = makeList(addColumnDefinitions);
    }

    public long getAddKeyword() {
        return addKeyword;
    }

    public ASTList<SQLAddColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    @Override
    public <T, R> R visit(SQLAlterTableOperationVisitor<T, R> visitor, T parameter) {

        return visitor.onAddColumn(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnDefinitions, recurseMode, iterator);
    }
}
