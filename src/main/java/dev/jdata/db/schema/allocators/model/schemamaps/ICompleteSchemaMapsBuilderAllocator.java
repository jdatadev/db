package dev.jdata.db.schema.allocators.model.schemamaps;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.IAllocators;

public interface ICompleteSchemaMapsBuilderAllocator<

                U extends CompleteSchemaMaps<? extends SchemaMap<? extends SchemaObject, ?, ?>>,
                V extends SimpleCompleteSchemaMapsBuilder<?, U, V>>

        extends IAllocators {

    V allocateCompleteSchemaMapsBuilder();

    void freeCompleteSchemaMapsBuilder(V builder);
}
