package dev.jdata.db.ddl.helpers.buildschema;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

abstract class DDLSchemaSQLStatementsWorkerObjects<

                INDEX_LIST extends IIndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SchemaObject, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SchemaObject, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SchemaObject>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends IHeapAllCompleteSchemaMaps,
                COMPLETE_SCHEMA_MAPS_BUILDER extends IAllCompleteSchemaMapsBuilder<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends DDLCompleteSchemaMapsWorkerObjects<
                        INDEX_LIST,
                        INDEX_LIST_BUILDER,
                        INDEX_LIST_ALLOCATOR,
                        SCHEMA_MAP,
                        COMPLETE_SCHEMA_MAPS,
                        HEAP_COMPLETE_SCHEMA_MAPS,
                        COMPLETE_SCHEMA_MAPS_BUILDER>

        implements IDDLSchemaSQLStatementsWorkerObjects<COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> {

    private final NodeObjectCache<DDLSchemaSQLStatementsParameter> ddlSchemaSQLStatementsParameterCache;

    DDLSchemaSQLStatementsWorkerObjects(IntFunction<COMPLETE_SCHEMA_MAPS_BUILDER[]> createCompleteSchemaMapsArray) {
        super(createCompleteSchemaMapsArray);

        this.ddlSchemaSQLStatementsParameterCache = new NodeObjectCache<>(DDLSchemaSQLStatementsParameter::new);
    }

    @Override
    public final DDLSchemaSQLStatementsParameter allocateDDLSchemaSQLStatementsParameter() {

        return ddlSchemaSQLStatementsParameterCache.allocate();
    }

    @Override
    public final void freeDDLSchemaSQLStatementsParameter(DDLSchemaSQLStatementsParameter parameter) {

        ddlSchemaSQLStatementsParameterCache.free(parameter);
    }
}
