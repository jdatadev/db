package dev.jdata.db.schema.model.schemamap;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.allocators.IImmutableInstanceBuilderAllocator;

public interface INonDiffSchemaMapBuilderAllocator<T extends SchemaObject, U extends INonDiffSchemaMap, V extends INonDiffSchemaMapBuilder<T, U, ?, V>>

        extends IImmutableInstanceBuilderAllocator<U, V> {

}
