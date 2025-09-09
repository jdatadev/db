package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;

public abstract class CompleteSchemaMapsBuilder<

                INDEX_LIST extends IndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IndexListBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SchemaObject, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends IHeapAllCompleteSchemaMaps>

        extends SchemaMapsBuilder<INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS>
        implements ICompleteSchemaMapsBuilder<SchemaObject, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS> {

    protected CompleteSchemaMapsBuilder(IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(createSchemaMapBuildersArray);
    }
}
