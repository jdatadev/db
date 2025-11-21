package dev.jdata.db.engine.sessions;

import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.utils.adt.arrays.IArrayAllocator;

public final class DMLSelectEvaluatorParameter extends BaseDMLEvaluatorParameter {

    public DMLSelectEvaluatorParameter(AllocationType allocationType, IArrayAllocator<SQLExpressionEvaluator> arrayAllocator) {
        super(allocationType, arrayAllocator);
    }
}
