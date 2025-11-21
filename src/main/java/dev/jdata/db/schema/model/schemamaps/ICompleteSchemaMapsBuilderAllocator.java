package dev.jdata.db.schema.model.schemamaps;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.allocators.IImmutableInstanceBuilderAllocator;

public interface ICompleteSchemaMapsBuilderAllocator<T extends SchemaObject, U extends ICompleteSchemaMaps, V extends ICompleteSchemaMapsBuilder<T, U, ?, V>>

        extends IImmutableInstanceBuilderAllocator<U, V> {

}
