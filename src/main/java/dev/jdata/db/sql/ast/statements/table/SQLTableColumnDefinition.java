package dev.jdata.db.sql.ast.statements.table;

import java.util.Objects;

import org.jutils.ast.objects.ASTIterator;
import org.jutils.ast.objects.ASTRecurseMode;
import org.jutils.ast.objects.expression.Expression;
import org.jutils.ast.objects.list.ASTSingle;
import org.jutils.parse.context.Context;

import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.sql.ast.BaseSQLElement;

public final class SQLTableColumnDefinition extends BaseSQLElement {

    private final long name;
    private final long typeName;
    private final SchemaDataType type;
    private final long notKeyword;
    private final long nullKeyword;
    private final long defaultKeyword;
    private final ASTSingle<Expression> defaultExpression;

    public SQLTableColumnDefinition(Context context, long name, long typeName, SchemaDataType type, long notKeyword, long nullKeyword, long defaultKeyword,
            Expression defaultExpression) {
        super(context);

        this.name = checkIsString(name);
        this.typeName = checkIsString(typeName);
        this.type = Objects.requireNonNull(type);
        this.notKeyword = checkIsKeywordOrNoKeyword(notKeyword);
        this.nullKeyword = checkIsKeywordOrNoKeyword(nullKeyword);
        this.defaultKeyword = checkIsKeywordOrNoKeyword(defaultKeyword);
        this.defaultExpression = safeMakeSingle(defaultExpression);

        checkAreBothKeywordsOrNot(notKeyword, nullKeyword);
        checkAreBothOrNot(defaultKeyword, defaultExpression);
    }

    public long getName() {
        return name;
    }

    public long getTypeName() {
        return typeName;
    }

    public SchemaDataType getType() {
        return type;
    }

    public long getNotKeyword() {
        return notKeyword;
    }

    public long getNullKeyword() {
        return nullKeyword;
    }

    public long getDefaultKeyword() {
        return defaultKeyword;
    }

    public Expression getDefaultExpression() {

        return safeGet(defaultExpression);
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        if (defaultExpression != null) {

            doIterate(defaultExpression, recurseMode, iterator);
        }
    }
}
