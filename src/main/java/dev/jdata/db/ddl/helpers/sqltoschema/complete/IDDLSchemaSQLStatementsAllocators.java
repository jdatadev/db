package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IIntSetBuilder;

public interface IDDLSchemaSQLStatementsAllocators<

                INT_SET_BUILDER extends IIntSetBuilder<?, ?>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap,
                HEAP_COMPLETE_SCHEMA_MAP extends IHeapCompleteSchemaMap,
                COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>> {

    DDLSchemaSQLStatementsParameter<INT_SET_BUILDER, INDEX_LIST_BUILDER> allocateDDLSchemaSQLStatementsParameter();
    void freeDDLSchemaSQLStatementsParameter(DDLSchemaSQLStatementsParameter<INT_SET_BUILDER, INDEX_LIST_BUILDER> parameter);
}
