package dev.jdata.db.schema.model.diff.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.schema.model.schemamaps.ISchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapsBuilder;
import dev.jdata.db.utils.allocators.BuilderAllocator;
import dev.jdata.db.utils.allocators.IImmutableInstanceBuilderAllocator;

abstract class SimpleDiffSchemaMapsBuilderAllocator<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
                SCHEMA_MAPS_BUILDER extends ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>>

        extends BuilderAllocator<SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>
        implements IImmutableInstanceBuilderAllocator<SCHEMA_MAPS, SCHEMA_MAPS_BUILDER> {

}
