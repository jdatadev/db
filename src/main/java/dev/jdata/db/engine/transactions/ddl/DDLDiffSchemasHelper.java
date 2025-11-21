package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.ddl.helpers.DDLSchemasHelper;
import dev.jdata.db.schema.model.diff.schemamaps.IHeapDiffSchemaMaps;
import dev.jdata.db.schema.model.diff.schemamaps.IHeapDiffSchemaMapsBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;

@Deprecated // currently not in use
class DDLDiffSchemasHelper extends DDLSchemasHelper {

    private static IHeapDiffSchemaMaps buildDiffSchemaMaps(IHeapAllCompleteSchemaMapsBuilder diffSchemaMapsBuilder,
            IHeapDiffSchemaMapsBuilder<SchemaObject>[] schemaMapBuilders) {

        throw new UnsupportedOperationException();
/*
        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final HeapSchemaMap<?> schemaMap = schemaMapBuilders[ddlObjectType.ordinal()].build();

            diffSchemaMapsBuilder.setSchemaMap(ddlObjectType, schemaMap);
        }

        return diffSchemaMapsBuilder.build();
*/
    }
}
