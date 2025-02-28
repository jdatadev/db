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

    public ASTList<SQLSelectStatementPart> getParts() {
        return parts;
    }

    public ASTList<SQLUnion> getUnions() {
        return unions;
    }

    @Override
    public <T, R> R visit(SQLStatementVisitor<T, R> visitor, T parameter) {

        return visitor.onSelect(this, parameter);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(parts, recurseMode, iterator);
        doIterate(unions, recurseMode, iterator);
    }
}
