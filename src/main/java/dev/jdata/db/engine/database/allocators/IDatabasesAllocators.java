package dev.jdata.db.engine.database.allocators;

import dev.jdata.db.engine.database.Database.IDMLEvaluatorParameterAllocator;
import dev.jdata.db.engine.database.Database.IDMLPreparedStatementEvaluatorParameterAllocator;
import dev.jdata.db.schema.allocators.databases.schemamanagement.IDatabaseSchemaManagementAllocators;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.sets.IMutableLongLargeSet;

public interface IDatabasesAllocators<T extends IMutableLongLargeArray, U extends IMutableLongLargeSet>

        extends IDMLEvaluatorParameterAllocator<T, U>, IDMLPreparedStatementEvaluatorParameterAllocator<T> {

    IDatabaseSchemaManagementAllocators getDatabaseSchemaManagementAllocators();
}
