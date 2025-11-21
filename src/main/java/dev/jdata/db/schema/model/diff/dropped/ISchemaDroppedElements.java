package dev.jdata.db.schema.model.diff.dropped;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;

public interface ISchemaDroppedElements {

    boolean isDroppedObject(DDLObjectType ddlObjectType, SchemaObject schemaObject);
    boolean isDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column);
}
