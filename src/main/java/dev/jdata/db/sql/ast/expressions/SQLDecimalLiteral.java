package dev.jdata.db.sql.ast.expressions;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.SQLAllocator;
import dev.jdata.db.sql.ast.SQLFreeable;
import dev.jdata.db.utils.adt.decimals.Decimal;

public final class SQLDecimalLiteral extends SQLLiteral implements SQLFreeable {

    private final Decimal decimal;

    public SQLDecimalLiteral(Context context, Decimal decimal) {
        super(context);

        this.decimal = Objects.requireNonNull(decimal);
    }

    @Override
    public void free(SQLAllocator allocator) {

        allocator.freeDecimal(decimal);
    }

    @Override
    public <P, R> R visit(SQLExpressionVisitor<P, R> visitor, P parameter) {

        return visitor.onDecimalLiteral(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
