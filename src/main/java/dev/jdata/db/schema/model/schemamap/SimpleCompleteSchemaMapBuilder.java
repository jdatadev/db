package dev.jdata.db.schema.model.schemamap;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;

abstract class SimpleCompleteSchemaMapBuilder<

                SCHEMA_OBJECTS extends ISchemaObjects<SchemaObject>,
                SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SchemaObject, SCHEMA_OBJECTS, ?>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap,
                HEAP_COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap & IHeapSchemaMapMarker,
                COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>>

        extends SimpleNonDiffSchemaMapBuilder<SchemaObject, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER, COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>
        implements ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER> {

    SimpleCompleteSchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECTS_BUILDER[]> createSchemaObjectsBuilderArray,
            IntFunction<SCHEMA_OBJECTS_BUILDER> createSchemaObjectsBuilder) {
        super(allocationType, createSchemaObjectsBuilderArray, createSchemaObjectsBuilder);
    }

    @Override
    public final COMPLETE_SCHEMA_MAP_BUILDER addTables(ISchemaObjects<Table> tables) {

        checkAddSchemaObjects(DDLObjectType.TABLE, tables);

        return getThis();
    }

    @Override
    public final COMPLETE_SCHEMA_MAP_BUILDER addViews(ISchemaObjects<View> views) {

        checkAddSchemaObjects(DDLObjectType.VIEW, views);

        return getThis();
    }

    @Override
    public final COMPLETE_SCHEMA_MAP_BUILDER addIndices(ISchemaObjects<Index> indices) {

        checkAddSchemaObjects(DDLObjectType.INDEX, indices);

        return getThis();
    }

    @Override
    public final COMPLETE_SCHEMA_MAP_BUILDER addTriggers(ISchemaObjects<Trigger> triggers) {

        checkAddSchemaObjects(DDLObjectType.TRIGGER, triggers);

        return getThis();
    }

    @Override
    public final COMPLETE_SCHEMA_MAP_BUILDER addFunctions(ISchemaObjects<DBFunction> functions) {

        checkAddSchemaObjects(DDLObjectType.FUNCTION, functions);

        return getThis();
    }

    @Override
    public final COMPLETE_SCHEMA_MAP_BUILDER addProcedures(ISchemaObjects<Procedure> procedures) {

        checkAddSchemaObjects(DDLObjectType.PROCEDURE, procedures);

        return getThis();
    }

    @SuppressWarnings("unchecked")
    private COMPLETE_SCHEMA_MAP_BUILDER getThis() {

        return (COMPLETE_SCHEMA_MAP_BUILDER)this;
    }
}
