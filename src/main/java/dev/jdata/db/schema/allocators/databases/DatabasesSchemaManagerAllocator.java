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
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DatabasesSchemaManagerAllocator<

                T extends IAllCompleteSchemaMaps,
                U extends IAllCompleteSchemaMaps & IHeapSchemaMapsMarker,
                V extends IAllCompleteSchemaMapsBuilder<T, U, V>>

        extends AllCompleteSchemaMapsBuilderAllocator<T, U, V>
        implements IAllCompleteSchemaMapsBuilderAllocator<T, U, V>, IAllocators {

    private final SchemaDroppedElementsAllocators droppedSchemaObjectsAllocator;
    private final IDatabaseSchemasAllocator databaseSchemasAllocator;

    private final IAllCompleteSchemaMapsBuilderAllocator<T, U, V> completeSchemaMapsBuilderAllocator;

    protected DatabasesSchemaManagerAllocator(SchemaDroppedElementsAllocators droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            IAllCompleteSchemaMapsBuilderAllocator<T, U, V> completeSchemaMapsBuilderAllocator) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
        this.databaseSchemasAllocator = Objects.requireNonNull(databaseSchemasAllocator);

        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
    }

    public final SchemaDroppedElementsAllocators getDroppedSchemaObjectsAllocator() {
        return droppedSchemaObjectsAllocator;
    }

    public final DroppedElementsSchemaObjects allocateDroppedElementsSchemaObjects() {

        return databaseSchemasAllocator.allocateSchemaDroppedElements();
    }

    public final void freeDroppedElementsSchemaObjects(DroppedElementsSchemaObjects droppedSchemaObjects) {

        databaseSchemasAllocator.freeSchemaDroppedElements(droppedSchemaObjects);
    }

    @Override
    public final V createBuilder() {

        return completeSchemaMapsBuilderAllocator.createBuilder();
    }

    @Override
    public final void freeBuilder(V builder) {

        Objects.requireNonNull(builder);

        completeSchemaMapsBuilderAllocator.freeBuilder(builder);
    }

    @Override
    public final void freeImmutable(T immutable) {

        Objects.requireNonNull(immutable);

        completeSchemaMapsBuilderAllocator.freeImmutable(immutable);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.PASSED, databaseSchemasAllocator);
        statisticsGatherer.addAllocators("completeSchemaMapsBuilderAllocator", RefType.PASSED, completeSchemaMapsBuilderAllocator);

    }
}
