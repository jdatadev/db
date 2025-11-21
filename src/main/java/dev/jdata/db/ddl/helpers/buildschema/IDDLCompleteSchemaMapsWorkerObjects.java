package dev.jdata.db.ddl.helpers.buildschema;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;

public interface IDDLCompleteSchemaMapsWorkerObjects<

                T extends ICompleteSchemaMaps,
                U extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
                V extends ICompleteSchemaMapsBuilder<SchemaObject, T, U, V>> {

    V allocateCompleteSchemaMapsBuilder();
    void freeCompleteSchemaMapsBuilder(V completeSchemaMapsBuilder);
}
