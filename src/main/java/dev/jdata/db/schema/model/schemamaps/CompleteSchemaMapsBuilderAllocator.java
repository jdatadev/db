package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;

abstract class CompleteSchemaMapsBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends SchemaMapsBuilderAllocator<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> {

}
