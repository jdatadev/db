package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamaps.BaseSimpleSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.schema.model.schemamaps.ISchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapsBuilder;

abstract class SimpleDiffSchemaMapsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
                SCHEMA_MAPS_BUILDER extends ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>>

        extends BaseSimpleSchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>
        implements IDiffSchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER> {

    SimpleDiffSchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP[]> createSchemaMapsArray) {
        super(allocationType, createSchemaMapsArray);

        initialize();
    }

    final void initialize() {

        diffSchemaMaps.checkIsNotAllocated();
    }
}
