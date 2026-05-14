package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;

abstract class SimpleNonDiffSchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                HEAP_NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                NON_DIFF_SCHEMA_MAP_BUILDER extends INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>>

        extends BaseSimpleSchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>
        implements INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER> {

    SimpleNonDiffSchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECTS[]> createSchemaObjectsArray) {
        super(allocationType, createSchemaObjectsArray);
    }
}
