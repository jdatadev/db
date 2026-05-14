package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;

abstract class NonDiffSchemaMapBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                HEAP_NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                NON_DIFF_SCHEMA_MAPS_BUILDER extends INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAPS_BUILDER>>

        extends SchemaMapBuilderAllocator<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAPS_BUILDER> {

}
