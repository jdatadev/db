package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;

abstract class AllSimpleCompleteSchemaMapsBuilder<

                SCHEMA_MAP extends ISchemaMap<SchemaObject>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends IAllCompleteSchemaMapsBuilder<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends SimpleCompleteSchemaMapsBuilder<SchemaObject, SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>
        implements IAllCompleteSchemaMapsBuilder<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> {

    protected AllSimpleCompleteSchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP[]> createSchemaMapsArray) {
        super(allocationType, createSchemaMapsArray);
    }
}
