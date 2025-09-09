package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

public interface IDDLSchemaSQLStatementsWorkerObjects<COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps, HEAP_COMPLETE_SCHEMA_MAPS extends IHeapAllCompleteSchemaMaps>

        extends IDDLCompleteSchemaMapsWorkerObjects<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> {

    DDLSchemaSQLStatementsParameter allocateDDLSchemaSQLStatementsParameter();
    void freeDDLSchemaSQLStatementsParameter(DDLSchemaSQLStatementsParameter parameter);
}
