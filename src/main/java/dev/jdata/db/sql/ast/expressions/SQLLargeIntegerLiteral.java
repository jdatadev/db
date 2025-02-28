package dev.jdata.db.sql.ast.expressions;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.SQLFreeable;
import dev.jdata.db.sql.parse.expression.LargeInteger;

public final class SQLLargeIntegerLiteral extends SQLLiteral implements SQLFreeable {

    private final LargeInteger integer;

    public SQLLargeIntegerLiteral(Context context, LargeInteger integer) {
        super(context);

        this.integer = Objects.requireNonNull(integer);
    }

    @Override
    public void free(SQLAllocator allocator) {

        allocator.freeLargeInteger(integer);
    }

    @Override
    public <P, R> R visit(SQLExpressionVisitor<P, R> visitor, P parameter) {

        return visitor.onLargeIntegerLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
