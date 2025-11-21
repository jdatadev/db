package dev.jdata.db.sql.ast.expressions;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.ISQLFreeable;
import dev.jdata.db.utils.adt.numbers.decimals.ICachedMutableDecimal;
import dev.jdata.db.utils.adt.numbers.decimals.IDecimalView;

public final class SQLDecimalLiteral extends SQLLiteral implements ISQLFreeable {

    private ICachedMutableDecimal decimal;

    public SQLDecimalLiteral(Context context, ICachedMutableDecimal decimal) {
        super(context);

        this.decimal = Objects.requireNonNull(decimal);
    }

    public IDecimalView getDecimal() {
        return decimal;
    }

    @Override
    public void free(ISQLAllocator allocator) {

        Objects.requireNonNull(allocator);

        allocator.freeDecimal(decimal);

        this.decimal = null;
    }

    @Override
    public <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onDecimalLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
