package dev.jdata.db.sql.ast.conditions;

public interface SQLConditionVisitor<P, R> {

    @Deprecated
    R onComparison(SQLComparisonCondition comparisonCondition, P parameter);

    R onIsNull(SQLIsNullCondition isNullCondition, P parameter);
    R onIsNotNull(SQLIsNotNullCondition isNotNullCondition, P parameter);

    R onLike(SQLLikeCondition likeCondition, P parameter);
    R onNotLike(SQLNotLikeCondition notLikeCondition, P parameter);

    R onIn(SQLInCondition inCondition, P parameter);
    R onNotIn(SQLNotInCondition notInCondition, P parameter);
}
