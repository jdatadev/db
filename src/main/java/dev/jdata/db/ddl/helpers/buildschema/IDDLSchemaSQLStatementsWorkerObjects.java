package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

public interface IDDLSchemaSQLStatementsWorkerObjects<T extends IAllCompleteSchemaMaps, U extends IHeapAllCompleteSchemaMaps, V extends IAllCompleteSchemaMapsBuilder<T, U, V>> {

    DDLSchemaSQLStatementsParameter allocateDDLSchemaSQLStatementsParameter();
    void freeDDLSchemaSQLStatementsParameter(DDLSchemaSQLStatementsParameter parameter);
}
