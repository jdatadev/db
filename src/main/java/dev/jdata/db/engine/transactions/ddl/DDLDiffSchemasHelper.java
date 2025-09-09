package dev.jdata.db.engine.transactions.ddl;

import dev.jdata.db.ddl.helpers.DDLSchemasHelper;
import dev.jdata.db.schema.model.diff.schemamaps.HeapDiffSchemaMaps;
import dev.jdata.db.schema.model.diff.schemamaps.HeapDiffSchemaMaps.HeapDiffSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.HeapAllSimpleCompleteSchemaMapsBuilder;

@Deprecated // currently not in use
class DDLDiffSchemasHelper extends DDLSchemasHelper {

    private static HeapDiffSchemaMaps buildDiffSchemaMaps(HeapAllSimpleCompleteSchemaMapsBuilder diffSchemaMapsBuilder, HeapDiffSchemaMapsBuilder[] schemaMapBuilders) {

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
