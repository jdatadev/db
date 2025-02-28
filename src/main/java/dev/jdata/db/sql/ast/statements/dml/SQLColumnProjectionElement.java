package dev.jdata.db.sql.ast.statements.dml;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.expressions.SQLColumnExpression;

public final class SQLColumnProjectionElement extends SQLProjectionElement {

    private final ASTSingle<SQLColumnExpression> columnExpresssion;

    public SQLColumnProjectionElement(Context context, SQLColumnExpression columnExpression) {
        super(context);

        this.columnExpresssion = makeSingle(columnExpression);
    }

    public SQLColumnExpression getColumnExpression() {
        return columnExpresssion.get();
    }

    @Override
    public <T, R> R visit(SQLProjectionElementVisitor<T, R> visitor, T parameter) {

        return visitor.onColumn(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(columnExpresssion, recurseMode, iterator);
    }
}
