package dev.jdata.db.sql.ast.expressions;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.ISQLAllocator;
import dev.jdata.db.sql.ast.ISQLFreeable;
import dev.jdata.db.utils.adt.numbers.integers.ICachedMutableLargeInteger;
import dev.jdata.db.utils.adt.numbers.integers.ILargeIntegerView;

public final class SQLLargeIntegerLiteral extends SQLLiteral implements ISQLFreeable {

    private ICachedMutableLargeInteger largeInteger;

    public SQLLargeIntegerLiteral(Context context, ICachedMutableLargeInteger largeInteger) {
        super(context);

        this.largeInteger = Objects.requireNonNull(largeInteger);
    }

    public ILargeIntegerView getLargeInteger() {
        return largeInteger;
    }

    @Override
    public void free(ISQLAllocator allocator) {

        Objects.requireNonNull(allocator);

        allocator.freeLargeInteger(largeInteger);

        this.largeInteger = null;
    }

    @Override
    public <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onLargeIntegerLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
