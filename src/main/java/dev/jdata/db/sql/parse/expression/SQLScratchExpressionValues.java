package dev.jdata.db.sql.parse.expression;

import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class SQLScratchExpressionValues extends ObjectCacheNode {

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
