package dev.jdata.db.schema.allocators.databases.schemamanagement;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.ICompleteSchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.schemamap.IHeapSchemaMapMarker;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

abstract class DatabaseSchemaManagementAllocators<

                MUTABLE_INT_SET extends IMutableIntSet,
                MUTABLE_INT_TO_MUTABLE_INT_SET_MAP extends IMutableIntToObjectWithRemoveStaticMap<MUTABLE_INT_SET>,
                COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap,
                HEAP_COMPLETE_SCHEMA_MAP extends ICompleteSchemaMap & IHeapSchemaMapMarker,
                COMPLETE_SCHEMA_MAP_BUILDER extends ICompleteSchemaMapBuilder<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER>>

        implements IDatabaseSchemaManagementAllocators, ISchemaDroppedElementsAllocator<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP>, IAllocators {

    private final SchemaDroppedElementsAllocators<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> schemaDroppedElementsAllocators;
    private final ICompleteSchemaMapBuilderAllocator<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER> completeSchemaMapBuilderAllocator;

    DatabaseSchemaManagementAllocators(SchemaDroppedElementsAllocators<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> schemaDroppedElementsAllocators,
            ICompleteSchemaMapBuilderAllocator<COMPLETE_SCHEMA_MAP, HEAP_COMPLETE_SCHEMA_MAP, COMPLETE_SCHEMA_MAP_BUILDER> completeSchemaMapsBuilderAllocator) {

        this.schemaDroppedElementsAllocators = Objects.requireNonNull(schemaDroppedElementsAllocators);
        this.completeSchemaMapBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
    }

    @Override
    public final SchemaDroppedElements<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP>
    copySchemaDroppedElements(SchemaDroppedElements<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> toCopy) {

        Objects.requireNonNull(toCopy);

        final SchemaDroppedElements<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> copy = allocateSchemaDroppedElements();

        copy.initialize(copy, schemaDroppedElementsAllocators);

        return copy;
    }

    private COMPLETE_SCHEMA_MAP_BUILDER createAllCompleteSchemaMapBuilder() {

        return completeSchemaMapBuilderAllocator.createBuilder();
    }

    private void freeAllCompleteSchemaMapBuilder(COMPLETE_SCHEMA_MAP_BUILDER builder) {

        Objects.requireNonNull(builder);

        completeSchemaMapBuilderAllocator.freeBuilder(builder);
    }

    private void freeAllCompleteSchemaMapImmutable(COMPLETE_SCHEMA_MAP immutable) {

        Objects.requireNonNull(immutable);

        completeSchemaMapBuilderAllocator.freeImmutable(immutable);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, schemaDroppedElementsAllocators);
//        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.PASSED, databaseSchemasAllocator);
// fix
//        statisticsGatherer.addAllocators("completeSchemaMapsBuilderAllocator", RefType.PASSED, completeSchemaMapsBuilderAllocator);
        throw new UnsupportedOperationException();
    }
}
