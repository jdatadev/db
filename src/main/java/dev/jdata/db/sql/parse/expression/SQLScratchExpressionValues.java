package dev.jdata.db.sql.parse.expression;

public final class SQLScratchExpressionValues {

    private final SQLScratchIntegerValue integerValue;
    private final SQLScratchIntegerValue fractionValue;

    public SQLScratchExpressionValues() {

        this.integerValue = new SQLScratchIntegerValue();
        this.fractionValue = new SQLScratchIntegerValue();
    }

    void clear() {

        integerValue.clear();
        fractionValue.clear();
    }

    public SQLScratchIntegerValue getIntegerValue() {
        return integerValue;
    }

    public SQLScratchIntegerValue getFractionValue() {
        return fractionValue;
    }
}
