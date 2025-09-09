package dev.jdata.db.schema.allocators.databases;

import java.util.Objects;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.IAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.diff.dropped.DroppedElementsSchemaObjects;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.SimpleCompleteSchemaMapsBuilder;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DatabasesSchemaManagerAllocator<

                T extends SchemaMap<SchemaObject, ?, ?>,
                U extends CompleteSchemaMaps<T>,
                V extends SimpleCompleteSchemaMapsBuilder<T, U, V>>

        implements IAllCompleteSchemaMapsBuilderAllocator<U, V>, IAllocators {

    private final DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;
    private final IDatabaseSchemasAllocator databaseSchemasAllocator;

    private final IAllCompleteSchemaMapsBuilderAllocator<U, V> completeSchemaMapsBuilderAllocator;

    protected DatabasesSchemaManagerAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            IAllCompleteSchemaMapsBuilderAllocator<U, V> completeSchemaMapsBuilderAllocator) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
        this.databaseSchemasAllocator = Objects.requireNonNull(databaseSchemasAllocator);

        this.completeSchemaMapsBuilderAllocator = Objects.requireNonNull(completeSchemaMapsBuilderAllocator);
    }

    public final DroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator() {
        return droppedSchemaObjectsAllocator;
    }

    public final DroppedElementsSchemaObjects allocateDroppedElementsSchemaObjects() {

        return databaseSchemasAllocator.allocateDroppedElementsSchemaObjects();
    }

    public final void freeDroppedElementsSchemaObjects(DroppedElementsSchemaObjects droppedSchemaObjects) {

        databaseSchemasAllocator.freeDroppedElementsSchemaObjects(droppedSchemaObjects);
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
