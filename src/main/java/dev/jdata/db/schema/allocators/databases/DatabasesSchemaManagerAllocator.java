package dev.jdata.db.schema.allocators.databases;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.schemamaps.AllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemamaps.IAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapSchemaMapsMarker;
import dev.jdata.db.schema.model.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DatabasesSchemaManagerAllocator<

                MUTABLE_INT_SET extends IMutableIntSet,
                MUTABLE_INT_TO_MUTABLE_INT_SET_MAP extends IMutableIntToObjectWithRemoveStaticMap<MUTABLE_INT_SET>,
                ALL_COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps,
                HEAP_ALL_COMPLETE_SCHEMA_MAPS extends IAllCompleteSchemaMaps & IHeapSchemaMapsMarker,
                ALL_COMPLETE_SCHEMA_MAPS_BUILDER extends IAllCompleteSchemaMapsBuilder<ALL_COMPLETE_SCHEMA_MAPS, HEAP_ALL_COMPLETE_SCHEMA_MAPS, ALL_COMPLETE_SCHEMA_MAPS_BUILDER>>

        extends AllCompleteSchemaMapsBuilderAllocator<ALL_COMPLETE_SCHEMA_MAPS, HEAP_ALL_COMPLETE_SCHEMA_MAPS, ALL_COMPLETE_SCHEMA_MAPS_BUILDER>
        implements IAllCompleteSchemaMapsBuilderAllocator<ALL_COMPLETE_SCHEMA_MAPS, HEAP_ALL_COMPLETE_SCHEMA_MAPS, ALL_COMPLETE_SCHEMA_MAPS_BUILDER>, IAllocators {

    private final SchemaDroppedElementsAllocators<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> droppedSchemaObjectsAllocator;
    private final IDatabaseSchemasAllocator<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> databaseSchemasAllocator;

    private final IAllCompleteSchemaMapsBuilderAllocator<ALL_COMPLETE_SCHEMA_MAPS, HEAP_ALL_COMPLETE_SCHEMA_MAPS, ALL_COMPLETE_SCHEMA_MAPS_BUILDER>
            completeSchemaMapsBuilderAllocator;

    protected DatabasesSchemaManagerAllocator(SchemaDroppedElementsAllocators<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> droppedSchemaObjectsAllocator,
            IDatabaseSchemasAllocator<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> databaseSchemasAllocator,
            IAllCompleteSchemaMapsBuilderAllocator<ALL_COMPLETE_SCHEMA_MAPS, HEAP_ALL_COMPLETE_SCHEMA_MAPS, ALL_COMPLETE_SCHEMA_MAPS_BUILDER> completeSchemaMapsBuilderAllocator) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
        this.databaseSchemasAllocator = Objects.requireNonNull(databaseSchemasAllocator);

        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
    }

    public final SchemaDroppedElementsAllocators<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> getDroppedSchemaObjectsAllocator() {
        return droppedSchemaObjectsAllocator;
    }

    public final SchemaDroppedElements<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> allocateDroppedElementsSchemaObjects() {

        return databaseSchemasAllocator.allocateSchemaDroppedElements();
    }

    public final void freeDroppedElementsSchemaObjects(SchemaDroppedElements<MUTABLE_INT_SET, MUTABLE_INT_TO_MUTABLE_INT_SET_MAP> schemaDroppedElements) {

        Objects.requireNonNull(schemaDroppedElements);

        databaseSchemasAllocator.freeSchemaDroppedElements(schemaDroppedElements);
    }

    @Override
    public final ALL_COMPLETE_SCHEMA_MAPS_BUILDER createBuilder() {

        return completeSchemaMapsBuilderAllocator.createBuilder();
    }

    @Override
    public final void freeBuilder(ALL_COMPLETE_SCHEMA_MAPS_BUILDER builder) {

        Objects.requireNonNull(builder);

        completeSchemaMapsBuilderAllocator.freeBuilder(builder);
    }

    @Override
    public final void freeImmutable(ALL_COMPLETE_SCHEMA_MAPS immutable) {

        Objects.requireNonNull(immutable);

        completeSchemaMapsBuilderAllocator.freeImmutable(immutable);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.PASSED, databaseSchemasAllocator);
// fix
//        statisticsGatherer.addAllocators("completeSchemaMapsBuilderAllocator", RefType.PASSED, completeSchemaMapsBuilderAllocator);
        throw new UnsupportedOperationException();
    }
}
