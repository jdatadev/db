package dev.jdata.db.schema.model.schemas;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IAllocators;

public interface IDatabaseSchemasAllocator<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>> extends IAllocators {

    SchemaDroppedElements<T, U> allocateSchemaDroppedElements();
    void freeSchemaDroppedElements(SchemaDroppedElements<T, U> droppedElementsSchemaObjects);

    SchemaDroppedElements<T, U> copyDroppedSchemaObjects(SchemaDroppedElements<T, U> toCopy);
}
