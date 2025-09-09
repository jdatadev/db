package dev.jdata.db.schema.allocators.model.schemamaps;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SimpleCompleteSchemaMapsBuilder;

public interface IAllCompleteSchemaMapsBuilderAllocator<

                T extends CompleteSchemaMaps<? extends SchemaMap<SchemaObject, ?, ?>>,
                U extends SimpleCompleteSchemaMapsBuilder<?, T, U>>

        extends ICompleteSchemaMapsBuilderAllocator<T, U> {
}
