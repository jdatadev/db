package dev.jdata.db.sql.ast.conditions;

import dev.jdata.db.sql.ast.SQLElement;

public interface SQLCondition extends SQLElement {

    <T, R> R visit(SQLConditionVisitor<T, R> visitor, T parameter);
}
