package dev.jdata.db.ddl;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public class DDLDiffSchemasHelper extends DDLSchemasHelper {

    private static DiffSchemaMaps buildDiffSchemaMaps(DiffSchemaMaps.Builder diffSchemaMapsBuilder, SchemaMap.Builder<?>[] schemaMapBuilders) {

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final SchemaMap<?> schemaMap = schemaMapBuilders[ddlObjectType.ordinal()].build();

            diffSchemaMapsBuilder.setSchemaMap(ddlObjectType, schemaMap);
        }

        return diffSchemaMapsBuilder.build();
    }
}
