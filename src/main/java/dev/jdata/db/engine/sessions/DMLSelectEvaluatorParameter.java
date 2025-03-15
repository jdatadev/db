package dev.jdata.db.engine.sessions;

import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.utils.allocators.IArrayAllocator;

public final class DMLSelectEvaluatorParameter extends BaseDMLEvaluatorParameter {

    public DMLSelectEvaluatorParameter(IArrayAllocator<SQLExpressionEvaluator> arrayAllocator) {
        super(arrayAllocator);
    }
}
