package dev.jdata.db.engine.database;

import dev.jdata.db.engine.database.Database.DMLEvaluatorParameterAllocator;
import dev.jdata.db.engine.database.Database.DMLPreparedStatementEvaluatorParameterAllocator;

interface IDatabasesAllocators extends DMLEvaluatorParameterAllocator, DMLPreparedStatementEvaluatorParameterAllocator {

}
