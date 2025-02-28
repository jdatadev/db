package dev.jdata.db.sql.ast.conditions;

public interface SQLConditionVisitor<T, R> {

    @Deprecated
    R onComparison(SQLComparisonCondition comparisonCondition, T parameter);

    R onIsNull(SQLIsNullCondition isNullCondition, T parameter);
    R onIsNotNull(SQLIsNotNullCondition isNotNullCondition, T parameter);

    R onLike(SQLLikeCondition likeCondition, T parameter);
    R onNotLike(SQLNotLikeCondition notLikeCondition, T parameter);

    R onIn(SQLInCondition inCondition, T parameter);
    R onNotIn(SQLNotInCondition notInCondition, T parameter);
}
