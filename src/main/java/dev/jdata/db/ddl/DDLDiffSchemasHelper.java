package dev.jdata.db.ddl;

import dev.jdata.db.schema.model.diff.schemamaps.HeapDiffSchemaMaps;
import dev.jdata.db.schema.model.diff.schemamaps.HeapDiffSchemaMaps.HeapDiffSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps.HeapCompleteSchemaMapsBuilder;

public class DDLDiffSchemasHelper extends DDLSchemasHelper {

    private static HeapDiffSchemaMaps buildDiffSchemaMaps(HeapCompleteSchemaMapsBuilder diffSchemaMapsBuilder, HeapDiffSchemaMapsBuilder[] schemaMapBuilders) {

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
