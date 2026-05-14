package dev.jdata.db.schema.effective;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMap;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

abstract class EffectiveSchemaAllocators<

                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                MUTABLE_INT_SET extends IMutableIntSet,
                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, ?>,
                NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                HEAP_NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                NON_DIFF_SCHEMA_MAP_BUILDER extends INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>,
                NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR extends INonDiffSchemaMapBuilderAllocator<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>>

        implements IAllocators {

    private final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator;
    private final IMutableIntSetAllocator<MUTABLE_INT_SET> mutableIntSetAllocator;
    private final NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR schemaMapBuilderAllocator;
    private final ISchemaObjectsBuilderAllocator<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER> schemaObjectsBuilderAllocator;

    EffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator,
            IMutableIntSetAllocator<MUTABLE_INT_SET> mutableIntSetAllocator, NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR schemaMapBuilderAllocator,
            ISchemaObjectsBuilderAllocator<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER> schemaObjectsBuilderAllocator) {

        this.columnIndexListAllocator = Objects.requireNonNull(columnIndexListAllocator);
        this.mutableIntSetAllocator = Objects.requireNonNull(mutableIntSetAllocator);
        this.schemaMapBuilderAllocator = Objects.requireNonNull(schemaMapBuilderAllocator);
        this.schemaObjectsBuilderAllocator = Objects.requireNonNull(schemaObjectsBuilderAllocator);
    }

    final IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> getColumnIndexListAllocator() {
        return columnIndexListAllocator;
    }

    final IMutableIntSetAllocator<MUTABLE_INT_SET> getMutableIntSetAllocator() {
        return mutableIntSetAllocator;
    }

    final NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR getSchemaMapBuilderAllocator() {
        return schemaMapBuilderAllocator;
    }

    final ISchemaObjectsBuilderAllocator<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER> getSchemaObjectsBuilderAllocator() {

        return schemaObjectsBuilderAllocator;
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("columnIndexListAllocator", RefType.PASSED, Column.class, columnIndexListAllocator);
        statisticsGatherer.addInstanceAllocator("mutableIntSetAllocator", RefType.PASSED, IMutableIntSet.class, mutableIntSetAllocator);
/*
        statisticsGatherer.addInstanceAllocator("schemaMapBuilderAllocator", RefType.PASSED, INonDiffSchemaMapBuilder.class, schemaMapBuilderAllocator);
        statisticsGatherer.addInstanceAllocator("schemaObjectsBuilderAllocator", RefType.PASSED, ISchemaObjects.class, schemaObjectsBuilderAllocator);
*/
        throw new UnsupportedOperationException();
    }
}
