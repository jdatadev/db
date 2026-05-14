package dev.jdata.db.schema.model.diff.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.allocators.BuilderAllocator;
import dev.jdata.db.utils.allocators.IImmutableInstanceBuilderAllocator;

abstract class SimpleDiffSchemaMapBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends BuilderAllocator<SCHEMA_MAP, SCHEMA_MAP_BUILDER>
        implements IImmutableInstanceBuilderAllocator<SCHEMA_MAP, SCHEMA_MAP_BUILDER> {

}
