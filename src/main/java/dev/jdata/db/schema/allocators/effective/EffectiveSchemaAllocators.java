package dev.jdata.db.schema.allocators.effective;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;

public abstract class EffectiveSchemaAllocators<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<?>,
                COMPLETE_SCHEMA_MAPS_BUILDER extends CompleteSchemaMapsBuilder<?, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        implements IAllocators {

    private final IndexListAllocator<Column, ?, ?, ?> columnIndexListAllocator;
    private final IMutableIntSetAllocator intSetAllocator;
    private final ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator;
    private final SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator;

    EffectiveSchemaAllocators(IndexListAllocator<Column, ?, ?, ?> columnIndexListAllocator, IMutableIntSetAllocator intSetAllocator,
            ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator,
            SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator) {

        this.columnIndexListAllocator = Objects.requireNonNull(columnIndexListAllocator);
        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
        this.schemaMapBuilderAllocator = Objects.requireNonNull(schemaMapBuilderAllocator);
    }

    public final IndexListAllocator<Column, ?, ?, ?> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    public final IMutableIntSetAllocator getIntSetAllocator() {
        return intSetAllocator;
    }

    public final ICompleteSchemaMapsBuilderAllocator<COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER> getCompleteSchemaMapsBuilderAllocator() {
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
