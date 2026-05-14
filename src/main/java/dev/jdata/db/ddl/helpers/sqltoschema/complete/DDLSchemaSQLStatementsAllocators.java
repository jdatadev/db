package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache;

abstract class DDLSchemaSQLStatementsAllocators<

                INT_SET_BUILDER extends IIntSetBuilder<?, ?>,
                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                SCHEMA_OBJECTS extends ISchemaObjects<SchemaObject>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap,
                HEAP_COMPLETE_SCHEMA_MAP extends IHeapCompleteSchemaMap,
                COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>>

        extends DDLNonDiffSchemaMapAllocators<SCHEMA_OBJECTS, COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>
        implements IDDLSchemaSQLStatementsAllocators<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER, COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER> {

    private final NodeObjectCache<DDLSchemaSQLStatementsParameter<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER>> ddlSchemaSQLStatementsParameterCache;

    DDLSchemaSQLStatementsAllocators(IntFunction<COMPLETE_SCHEMA_MAP_BUILDER[]> createCompleteSchemaMapsArray) {
        super(createCompleteSchemaMapsArray);

        this.ddlSchemaSQLStatementsParameterCache = new NodeObjectCache<>(DDLSchemaSQLStatementsParameter::new);
    }

    @Override
    public final DDLSchemaSQLStatementsParameter<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> allocateDDLSchemaSQLStatementsParameter() {

        return ddlSchemaSQLStatementsParameterCache.allocate();
    }

    @Override
    public final void freeDDLSchemaSQLStatementsParameter(DDLSchemaSQLStatementsParameter<INT_SET_BUILDER, COLUMN_INDEX_LIST_BUILDER> parameter) {

        Objects.requireNonNull(parameter);

        ddlSchemaSQLStatementsParameterCache.free(parameter);
    }
}
