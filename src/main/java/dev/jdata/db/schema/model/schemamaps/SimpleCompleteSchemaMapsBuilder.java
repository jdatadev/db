package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;

abstract class SimpleCompleteSchemaMapsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends BaseSimpleSchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>
        implements ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> {

    SimpleCompleteSchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP[]> createSchemaMapsArray) {
        super(allocationType, createSchemaMapsArray);
    }
}
