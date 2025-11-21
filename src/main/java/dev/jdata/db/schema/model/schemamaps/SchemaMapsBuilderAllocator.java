package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.allocators.BuilderAllocator;

abstract class SchemaMapsBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
                SCHEMA_MAPS_BUILDER extends ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>>

        extends BuilderAllocator<SCHEMA_MAPS, SCHEMA_MAPS_BUILDER> {

}
