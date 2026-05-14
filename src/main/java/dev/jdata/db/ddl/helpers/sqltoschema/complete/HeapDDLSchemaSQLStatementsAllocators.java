package dev.jdata.db.ddl.helpers.sqltoschema.complete;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemaobjects.IHeapSchemaObjects;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IHeapIntSetBuilder;

public final class HeapDDLSchemaSQLStatementsAllocators extends DDLSchemaSQLStatementsAllocators<

                IHeapIntSetBuilder,
                IHeapIndexListBuilder<Column>,
                IHeapSchemaObjects<SchemaObject>,
                IHeapCompleteSchemaMap,
                IHeapCompleteSchemaMap,
                IHeapCompleteSchemaMapBuilder> {

    public HeapDDLSchemaSQLStatementsAllocators() {
        super(IHeapCompleteSchemaMapBuilder[]::new);
    }

    @Override
    IHeapCompleteSchemaMapBuilder createSchemaMapBuilder() {

        return IHeapCompleteSchemaMapBuilder.create();
    }
}
