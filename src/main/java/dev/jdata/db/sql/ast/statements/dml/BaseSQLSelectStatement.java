package dev.jdata.db.sql.ast.statements.dml;

import java.util.List;
import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.list.ASTList;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.statements.SQLStatementVisitor;

public abstract class BaseSQLSelectStatement extends SQLDMLStatement {

    private final ASTList<SQLSelectStatementPart> parts;
    private final ASTList<SQLUnion> unions;

    public BaseSQLSelectStatement(Context context, List<SQLSelectStatementPart> parts, List<SQLUnion> unions) {
        super(context);

        Objects.requireNonNull(parts);
        Objects.requireNonNull(unions);

        if (unions.size() != parts.size()) {

            throw new IllegalArgumentException();
        }

        this.parts = makeList(parts);
        this.unions = makeList(unions);
    }

    public final ASTList<SQLSelectStatementPart> getParts() {
        return parts;
    }

    public final ASTList<SQLUnion> getUnions() {
        return unions;
    }

    @Override
    public final <P, R, E extends Exception> R visit(SQLStatementVisitor<P, R, E> visitor, P parameter) throws E {

        return visitor.onSelect(this, parameter);
    }

    @Override
    protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(parts, recurseMode, iterator);
        doIterate(unions, recurseMode, iterator);
    }
}
