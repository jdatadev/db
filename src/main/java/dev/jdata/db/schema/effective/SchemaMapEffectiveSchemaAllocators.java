package dev.jdata.db.schema.effective;

import java.util.Objects;

import dev.jdata.db.schema.common.SchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMap;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.INonDiffSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilderAllocator;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

abstract class SchemaMapEffectiveSchemaAllocators<

                COLUMN_INDEX_LIST_BUILDER extends IIndexListBuilder<Column, ?, ?>,
                MUTABLE_INT_SET extends IMutableIntSet,
                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECT_INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                SCHEMA_OBJECT_INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?>,
                SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, SCHEMA_OBJECT_INDEX_LIST, ?, SCHEMA_OBJECT_INDEX_LIST_BUILDER>,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, ?>,
                NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap,
                HEAP_NON_DIFF_SCHEMA_MAP extends INonDiffSchemaMap & IHeapSchemaMapMarker,
                NON_DIFF_SCHEMA_MAP_BUILDER extends INonDiffSchemaMapBuilder<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, HEAP_NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>,
                NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR extends INonDiffSchemaMapBuilderAllocator<SCHEMA_OBJECT, NON_DIFF_SCHEMA_MAP, NON_DIFF_SCHEMA_MAP_BUILDER>>

        extends EffectiveSchemaAllocators<

                        COLUMN_INDEX_LIST_BUILDER,
                        MUTABLE_INT_SET,
                        SCHEMA_OBJECT,
                        SCHEMA_OBJECTS,
                        SCHEMA_OBJECTS_BUILDER,
                        NON_DIFF_SCHEMA_MAP,
                        HEAP_NON_DIFF_SCHEMA_MAP,
                        NON_DIFF_SCHEMA_MAP_BUILDER,
                        NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR> {

    private final SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> schemaObjectIndexListAllocators;

    SchemaMapEffectiveSchemaAllocators(IIndexListAllocator<Column, ?, ?, COLUMN_INDEX_LIST_BUILDER> columnIndexListAllocator,
            IMutableIntSetAllocator<MUTABLE_INT_SET> mutableIntSetAllocator, NON_DIFF_SCHEMA_MAP_BUILDER_ALLOCATOR schemaMapBuilderAllocator,
            ISchemaObjectsBuilderAllocator<SCHEMA_OBJECT, SCHEMA_OBJECTS, SCHEMA_OBJECTS_BUILDER> schemaObjectsBuilderAllocator,
            SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> schemaObjectIndexListAllocators) {
        super(columnIndexListAllocator, mutableIntSetAllocator, schemaMapBuilderAllocator, schemaObjectsBuilderAllocator);

        this.schemaObjectIndexListAllocators = Objects.requireNonNull(schemaObjectIndexListAllocators);
    }

    final SchemaObjectIndexListAllocators<SCHEMA_OBJECT_INDEX_LIST_ALLOCATOR> getSchemaObjectIndexListAllocators() {
        return schemaObjectIndexListAllocators;
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        super.gatherStatistics(statisticsGatherer);

        statisticsGatherer.addAllocators("schemaObjectIndexListAllocators", RefType.PASSED, schemaObjectIndexListAllocators);
    }
}
