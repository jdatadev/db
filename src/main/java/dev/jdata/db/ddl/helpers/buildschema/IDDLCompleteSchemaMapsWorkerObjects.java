package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

public interface IDDLCompleteSchemaMapsWorkerObjects<COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps, HEAP_COMPLETE_SCHEMA_MAPS extends IHeapAllCompleteSchemaMaps> {

    ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> allocateCompleteSchemaMapBuilders();
    void freeCompleteSchemaMapBuilders(ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> completeSchemaMapsBuilder);
}
