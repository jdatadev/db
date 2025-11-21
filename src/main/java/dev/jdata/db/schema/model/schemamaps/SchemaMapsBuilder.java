package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

abstract class SchemaMapsBuilder<

                INDEX_LIST extends IBaseIndexList<SchemaObject>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SchemaObject, INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends SchemaMap<SchemaObject, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends IHeapSchemaMaps>

        extends BaseSchemaMapsBuilder<SchemaObject, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER, SCHEMA_MAPS, HEAP_SCHEMA_MAPS> {

    SchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(allocationType, createSchemaMapBuildersArray);
    }
}
