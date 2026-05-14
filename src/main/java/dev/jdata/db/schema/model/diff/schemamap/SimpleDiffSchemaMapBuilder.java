package dev.jdata.db.schema.model.diff.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.BaseSimpleSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

abstract class SimpleDiffSchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends BaseSimpleSchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>
        implements IDiffSchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER> {

    SimpleDiffSchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECTS[]> createSchemaObjectsArray) {
        super(allocationType, createSchemaObjectsArray);
    }
}
