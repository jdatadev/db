package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

abstract class SimpleCompleteSchemaMapBuilder<

                SCHEMA_OBJECTS extends ISchemaObjects<SchemaObject>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap,
                HEAP_COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap & IHeapSchemaMapMarker,
                COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>>

        extends SimpleNonDiffSchemaMapBuilder<SchemaObject, SCHEMA_OBJECTS, COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>
        implements ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER> {

    SimpleCompleteSchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECTS[]> createSchemaObjectsArray) {
        super(allocationType, createSchemaObjectsArray);
    }
}
