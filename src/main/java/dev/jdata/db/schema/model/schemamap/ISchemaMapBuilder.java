package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedElementsBuilder;

public interface ISchemaMapBuilder<T extends SchemaObject, U extends ISchemaMap<T>, V extends ISchemaMap<T> & IHeapContainsMarker>

        extends IObjectUnorderedElementsBuilder<T, U, V> {

    void setSchemaMap(ISchemaMap<T> schemaMap);
}
