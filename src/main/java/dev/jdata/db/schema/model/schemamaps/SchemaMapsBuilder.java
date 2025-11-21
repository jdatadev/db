package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

abstract class SchemaMapsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
/*
                SCHEMA_MAP_BUILDERS extends IImmutable,
                HEAP_SCHEMA_MAP_BUILDERS extends IImmutable & IHeapTypeMarker,
*/
                SCHEMA_MAPS_BUILDER extends ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>>

        extends BaseSchemaMapsBuilder<

                        SCHEMA_OBJECT,
                        INDEX_LIST,
                        INDEX_LIST_BUILDER,
                        INDEX_LIST_ALLOCATOR,
                        SCHEMA_MAP,
                        SCHEMA_MAP_BUILDER,
                        SCHEMA_MAPS,
                        HEAP_SCHEMA_MAPS,
/*
                        SCHEMA_MAP_BUILDERS,
                        HEAP_SCHEMA_MAP_BUILDERS,
*/
                        SCHEMA_MAPS_BUILDER> {

    SchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(allocationType, createSchemaMapBuildersArray);
    }
}
