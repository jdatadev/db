package dev.jdata.db.schema.allocators.effective;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class EffectiveSchemaAllocators<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IBaseIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<SCHEMA_MAP>,
                COMPLETE_SCHEMA_MAPS_BUILDER extends SimpleCompleteSchemaMapsBuilder<SCHEMA_MAP, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>,
                COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        implements IAllocators {

    private final IBaseIndexListAllocator<Column, ?, ?> columnIndexListAllocator;
    private final IBaseMutableIntSetAllocator intSetAllocator;
    private final COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR completeSchemaMapsBuilderAllocator;
    private final SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator;

    EffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?> columnIndexListAllocator, IBaseMutableIntSetAllocator intSetAllocator,
            COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR completeSchemaMapsBuilderAllocator,
            SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator) {

        this.columnIndexListAllocator = Objects.requireNonNull(columnIndexListAllocator);
        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
        this.schemaMapBuilderAllocator = Objects.requireNonNull(schemaMapBuilderAllocator);
    }

    public final IBaseIndexListAllocator<Column, ?, ?> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    public final IBaseMutableIntSetAllocator getIntSetAllocator() {
        return intSetAllocator;
    }

    public final COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR getCompleteSchemaMapsBuilderAllocator() {
        return completeSchemaMapsBuilderAllocator;
    }

    public final SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>
    getSchemaMapBuilderAllocator() {

        return schemaMapBuilderAllocator;
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("columnIndexListAllocator", RefType.PASSED, Column.class, columnIndexListAllocator);

    }
}
