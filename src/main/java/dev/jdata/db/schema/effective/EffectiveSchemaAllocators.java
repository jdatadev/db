package dev.jdata.db.schema.effective;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.schema.model.schemamaps.ISchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class EffectiveSchemaAllocators<

                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                MUTABLE_INT_SET extends IMutableIntSet,
                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>,
                COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps,
                HEAP_COMPLETE_SCHEMA_MAPS extends ICompleteSchemaMaps & IHeapSchemaMapsMarker,
                COMPLETE_SCHEMA_MAPS_BUILDER extends ICompleteSchemaMapsBuilder<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, HEAP_COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>,
                COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR extends ICompleteSchemaMapsBuilderAllocator<SCHEMA_OBJECT, COMPLETE_SCHEMA_MAPS, COMPLETE_SCHEMA_MAPS_BUILDER>>

        implements IAllocators {

    private final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator;
    private final IMutableIntSetAllocator<MUTABLE_INT_SET> intSetAllocator;
    private final COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR completeSchemaMapsBuilderAllocator;
    private final ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator;

    EffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator, IMutableIntSetAllocator<MUTABLE_INT_SET> intSetAllocator,
            COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR completeSchemaMapsBuilderAllocator,
            ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator) {

        this.columnIndexListAllocator = Objects.requireNonNull(columnIndexListAllocator);
        this.intSetAllocator = Objects.requireNonNull(intSetAllocator);
        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
        this.schemaMapBuilderAllocator = Objects.requireNonNull(schemaMapBuilderAllocator);
    }

    public final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    public final IMutableIntSetAllocator<MUTABLE_INT_SET> getIntSetAllocator() {
        return intSetAllocator;
    }

    public final COMPLETE_SCHEMA_MAPS_BUILDER_ALLOCATOR getCompleteSchemaMapsBuilderAllocator() {
        return completeSchemaMapsBuilderAllocator;
    }

    public final ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> getSchemaMapBuilderAllocator() {

        return schemaMapBuilderAllocator;
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("columnIndexListAllocator", RefType.PASSED, Column.class, columnIndexListAllocator);
    }
}
