package dev.jdata.db.sql.ast.conditions;

import dev.jdata.db.sql.ast.ISQLElement;

public interface ISQLCondition extends ISQLElement {

    <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter);
}
