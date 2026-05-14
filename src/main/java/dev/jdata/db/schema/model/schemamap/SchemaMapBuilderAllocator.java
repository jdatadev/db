package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.allocators.BuilderAllocator;

abstract class SchemaMapBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends BuilderAllocator<SCHEMA_MAP, SCHEMA_MAP_BUILDER> {

}
