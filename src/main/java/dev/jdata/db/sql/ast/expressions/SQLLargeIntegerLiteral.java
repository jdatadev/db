package dev.jdata.db.sql.ast.expressions;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.SQLFreeable;
import dev.jdata.db.utils.adt.integers.ILargeInteger;
import dev.jdata.db.utils.adt.integers.MutableLargeInteger;

public final class SQLLargeIntegerLiteral extends SQLLiteral implements SQLFreeable {

    private final MutableLargeInteger largeInteger;

    public SQLLargeIntegerLiteral(Context context, MutableLargeInteger largeInteger) {
        super(context);

        this.largeInteger = Objects.requireNonNull(largeInteger);
    }

    public ILargeInteger getLargeInteger() {
        return largeInteger;
    }

    @Override
    public void free(SQLAllocator allocator) {

        allocator.freeLargeInteger(largeInteger);
    }

    @Override
    public <P, R, E extends Exception> R visitSQLExpression(SQLExpressionVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onLargeIntegerLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
