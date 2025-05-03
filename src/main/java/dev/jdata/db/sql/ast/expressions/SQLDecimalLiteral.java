package dev.jdata.db.sql.ast.expressions;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.utils.adt.decimals.MutableDecimal;

public final class SQLDecimalLiteral extends SQLLiteral {

    private final MutableDecimal decimal;

    public SQLDecimalLiteral(Context context, MutableDecimal decimal) {
        super(context);

        this.decimal = Objects.requireNonNull(decimal);
    }

    public MutableDecimal getDecimal() {
        return decimal;
    }

    @Override
    public <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onDecimalLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
