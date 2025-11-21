package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;

abstract class BaseSchemaMapsBuilder<

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

        extends BaseSchemaMapBuilders<

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
                        SCHEMA_MAPS_BUILDER>

        implements ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER> {

    BaseSchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(allocationType, createSchemaMapBuildersArray);
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setTables(ISchemaMap<Table> tables) {

        checkSetSchemaMap(DDLObjectType.TABLE, tables);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setViews(ISchemaMap<View> views) {

        checkSetSchemaMap(DDLObjectType.VIEW, views);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setIndices(ISchemaMap<Index> indices) {

        checkSetSchemaMap(DDLObjectType.INDEX, indices);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setTriggers(ISchemaMap<Trigger> triggers) {

        checkSetSchemaMap(DDLObjectType.TRIGGER, triggers);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setFunctions(ISchemaMap<DBFunction> functions) {

        checkSetSchemaMap(DDLObjectType.FUNCTION, functions);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setProcedures(ISchemaMap<Procedure> procedures) {

        checkSetSchemaMap(DDLObjectType.PROCEDURE, procedures);

        return getThis();
    }

    private <T extends SchemaObject, R extends ISchemaMap<T>> void checkSetSchemaMap(DDLObjectType ddlObjectType, R value) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(value);

        final int index = ddlObjectType.ordinal();

        @SuppressWarnings("unchecked")
        final ISchemaMap<SCHEMA_OBJECT> schemaMap = (ISchemaMap<SCHEMA_OBJECT>)value;

        getMutable()[index].setSchemaMap(schemaMap);
    }

    @SuppressWarnings("unchecked")
    private SCHEMA_MAPS_BUILDER getThis() {

        return (SCHEMA_MAPS_BUILDER)this;
    }
}
