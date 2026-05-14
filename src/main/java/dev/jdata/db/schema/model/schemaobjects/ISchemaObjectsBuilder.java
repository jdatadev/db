package dev.jdata.db.schema.model.schemaobjects;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedElementsBuilder;

public interface ISchemaObjectsBuilder<T extends SchemaObject, U extends ISchemaObjects<T>, V extends ISchemaObjects<T> & IHeapContainsMarker>

        extends IObjectUnorderedElementsBuilder<T, U, V> {

    @Deprecated // in use?
    void setSchemaMap(ISchemaObjects<T> schemaMap);
}
