package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.mutability.IInstanceBuilder;

public interface ISchemaMapsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
                SCHEMA_MAPS_BUILDER extends ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>>

        extends IInstanceBuilder<SCHEMA_MAPS, HEAP_SCHEMA_MAPS>, ISchemaMapBuilders<SCHEMA_OBJECT, SCHEMA_MAPS_BUILDER>, IContains {

    SCHEMA_MAPS_BUILDER setTables(ISchemaMap<Table> tables);
    SCHEMA_MAPS_BUILDER setViews(ISchemaMap<View> views);
    SCHEMA_MAPS_BUILDER setIndices(ISchemaMap<Index> indices);
    SCHEMA_MAPS_BUILDER setTriggers(ISchemaMap<Trigger> triggers);
    SCHEMA_MAPS_BUILDER setFunctions(ISchemaMap<DBFunction> functions);
    SCHEMA_MAPS_BUILDER setProcedures(ISchemaMap<Procedure> procedures);
}
