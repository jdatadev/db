package dev.jdata.db.schema.allocators.databases;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.ICompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DatabasesSchemaManagerAllocator<T extends SchemaMap<?, ?, ?, ?, ?>, U extends CompleteSchemaMaps<T>, V extends CompleteSchemaMapsBuilder<T, U, V>>

        implements ICompleteSchemaMapsBuilderAllocator<U, V>, IAllocators {

    private final DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;
    private final IDatabaseSchemasAllocator databaseSchemasAllocator;

    private final ICompleteSchemaMapsBuilderAllocator<U, V> completeSchemaMapsBuilderAllocator;

    protected DatabasesSchemaManagerAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            ICompleteSchemaMapsBuilderAllocator<U, V> completeSchemaMapsBuilderAllocator) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
        this.databaseSchemasAllocator = Objects.requireNonNull(databaseSchemasAllocator);

        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
    }

    public final DroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator() {
        return droppedSchemaObjectsAllocator;
    }

    public final DroppedSchemaObjects allocateDroppedSchemaObjects() {

        return databaseSchemasAllocator.allocateDroppedSchemaObjects();
    }

    public final void freeDroppedSchemaObjects(DroppedSchemaObjects droppedSchemaObjects) {

        databaseSchemasAllocator.freeDroppedSchemaObjects(droppedSchemaObjects);
    }

    @Override
    public final V allocateCompleteSchemaMapsBuilder() {

        return completeSchemaMapsBuilderAllocator.allocateCompleteSchemaMapsBuilder();
    }

    @Override
    public final void freeCompleteSchemaMapsBuilder(V builder) {

        Objects.requireNonNull(builder);

        completeSchemaMapsBuilderAllocator.freeCompleteSchemaMapsBuilder(builder);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.PASSED, databaseSchemasAllocator);
        statisticsGatherer.addAllocators("completeSchemaMapsBuilderAllocator", RefType.PASSED, completeSchemaMapsBuilderAllocator);

    }
}
