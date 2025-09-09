package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ISchemaMapsBuilder<SCHEMA_OBJECT extends SchemaObject, SCHEMA_MAPS extends ISchemaMaps, HEAP_SCHEMA_MAPS extends IHeapSchemaMaps>

        extends ISchemaMapBuilders<SCHEMA_OBJECT> {

    SCHEMA_MAPS build();

    HEAP_SCHEMA_MAPS buildHeapAllocated();
}
