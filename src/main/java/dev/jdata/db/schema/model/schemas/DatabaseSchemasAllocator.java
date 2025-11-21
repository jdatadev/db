package dev.jdata.db.schema.model.schemas;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

public abstract class DatabaseSchemasAllocator<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>> implements IDatabaseSchemasAllocator<T, U> {

    private final SchemaDroppedElementsAllocators<T, U> droppedSchemaObjectsAllocator;

    protected DatabaseSchemasAllocator(SchemaDroppedElementsAllocators<T, U> droppedSchemaObjectsAllocator) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
    }

    @Override
    public final SchemaDroppedElements<T, U> copyDroppedSchemaObjects(SchemaDroppedElements<T, U> toCopy) {

        Objects.requireNonNull(toCopy);

        final SchemaDroppedElements<T, U> copy = allocateSchemaDroppedElements();

        copy.initialize(copy, droppedSchemaObjectsAllocator);

        return copy;
    }
}
