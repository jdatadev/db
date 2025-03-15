package dev.jdata.db.engine.database;

import dev.jdata.db.engine.database.Database.DMLEvaluatorParameterAllocator;
import dev.jdata.db.engine.database.Database.DMLPreparedStatementEvaluatorParameterAllocator;
import dev.jdata.db.utils.allocators.IListAllocator;

interface IDatabasesAllocators extends DMLEvaluatorParameterAllocator, DMLPreparedStatementEvaluatorParameterAllocator, IListAllocator {

}
