package dev.jdata.db.schema.allocators.model.schemamaps;

import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.IAllocators;

public interface ICompleteSchemaMapsBuilderAllocator<

                U extends CompleteSchemaMaps<?>,
                V extends CompleteSchemaMapsBuilder<?, U, V>> extends IAllocators {

    V allocateCompleteSchemaMapsBuilder();

    void freeCompleteSchemaMapsBuilder(V builder);
}
