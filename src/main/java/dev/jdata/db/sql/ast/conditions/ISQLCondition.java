package dev.jdata.db.sql.ast.conditions;

import dev.jdata.db.sql.ast.ISQLElement;

public interface ISQLCondition extends ISQLElement {

    <P, R> R visit(SQLConditionVisitor<P, R> visitor, P parameter);
}
