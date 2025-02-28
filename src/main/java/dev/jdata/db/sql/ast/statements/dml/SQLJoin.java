package dev.jdata.db.sql.ast.statements.dml;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.parse.context.Context;

import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLJoin extends BaseSQLElement {

    private final long directionKeyword;
    private final long outerKeyword;
    private final long joinKeyword;
    private final SQLJoinType joinType;

    public SQLJoin(Context context, long joinKeyword, SQLJoinType joinType) {
        this(context, NO_KEYWORD, NO_KEYWORD, joinKeyword, joinType, false);

        if (joinType != SQLJoinType.INNER) {

            throw new IllegalArgumentException();
        }
    }

    public SQLJoin(Context context, long directionKeyword, long joinKeyword, SQLJoinType joinType) {
        this(context, checkIsKeyword(directionKeyword), NO_KEYWORD, joinKeyword, joinType, false);
    }

    public SQLJoin(Context context, long directionKeyword, long outerKeyword, long joinKeyword, SQLJoinType joinType) {
        this(context, checkIsKeyword(directionKeyword), checkIsKeyword(outerKeyword), joinKeyword, joinType, false);
    }

    private SQLJoin(Context context, long directionKeyword, long outerKeyword, long joinKeyword, SQLJoinType joinType, boolean disambiguate) {
        super(context);

        this.directionKeyword = checkIsKeywordOrNoKeyword(joinKeyword);
        this.outerKeyword = checkIsKeywordOrNoKeyword(outerKeyword);
        this.joinKeyword = checkIsKeyword(joinKeyword);
        this.joinType = Objects.requireNonNull(joinType);
    }

    public long getDirectionKeyword() {
        return directionKeyword;
    }

    public long getOuterKeyword() {
        return outerKeyword;
    }

    public long getJoinKeyword() {
        return joinKeyword;
    }

    public SQLJoinType getJoinType() {
        return joinType;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

    }
}
