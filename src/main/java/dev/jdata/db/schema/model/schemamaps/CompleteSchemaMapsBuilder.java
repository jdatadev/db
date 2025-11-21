package dev.jdata.db.schema.model.schemamaps;

import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

abstract class CompleteSchemaMapsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>,
                COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
/*
                SCHEMA_MAP_BUILDERS extends IImmutable,
                HEAP_SCHEMA_MAP_BUILDERS extends IImmutable & IHeapTypeMarker,
*/
                COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends SchemaMapsBuilder<

                        SCHEMA_OBJECT,
                        INDEX_LIST,
                        INDEX_LIST_BUILDER,
                        INDEX_LIST_ALLOCATOR,
                        SCHEMA_MAP,
                        SCHEMA_MAP_BUILDER,
                        COMPLETE_SCHEMA_MAPS,
                        HEAP_COMPLETE_SCHEMA_MAPS,
/*
                        SCHEMA_MAP_BUILDERS,
                        HEAP_SCHEMA_MAP_BUILDERS,
*/
                        COMPLETE_SCHEMA_MAPS_BUILDER>

        implements ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> {

    protected CompleteSchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(allocationType, createSchemaMapBuildersArray);
    }
}
