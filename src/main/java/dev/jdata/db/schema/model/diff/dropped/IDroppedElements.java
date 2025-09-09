package dev.jdata.db.schema.model.diff.dropped;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;

public interface IDroppedElements {

    @Deprecated
    boolean isDroppedObject(SchemaObject schemaObject);

    boolean isDroppedColumn(SchemaObject schemaObject, Column column);
}
