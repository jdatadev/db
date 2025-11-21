package dev.jdata.db.schema.allocators;

import java.util.Objects;

import dev.jdata.db.schema.allocators.databases.heap.HeapDatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.common.HeapSchemaObjectIndexListAllocators;
import dev.jdata.db.schema.model.diff.dropped.HeapDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.schemas.DatabaseSchemasAllocator;
import dev.jdata.db.schema.model.schemas.HeapDatabaseSchemasAllocator;
import dev.jdata.db.utils.adt.maps.ICachedMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

/**
 * Root allocator for schema management.
 */
public final class SchemaManagementAllocators implements IAllocators {

    private final IMutableIntToObjectWithRemoveStaticMapAllocator<? extends IMutableIntSet, ?> mutableIntToObjectMapAllocator;
    private final HeapSchemaObjectIndexListAllocators<?> schemaObjectIndexListAllocators;
    private final HeapDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;
    private final DatabaseSchemasAllocator databaseSchemasAllocator;
    private final HeapDatabasesSchemaManagerAllocator schemaManagerAllocator;

    public SchemaManagementAllocators(IMutableIntSetAllocator<?> intSetAllocator) {

        Objects.requireNonNull(intSetAllocator);

        this.mutableIntToObjectMapAllocator = ICachedMutableIntToObjectWithRemoveStaticMapAllocator.create(IMutableIntSet[]::new);

        this.schemaObjectIndexListAllocators = HeapSchemaObjectIndexListAllocators.INSTANCE;
        this.droppedSchemaObjectsAllocator = new HeapDroppedSchemaObjectsAllocator(intSetAllocator, mutableIntToObjectMapAllocator);
        this.databaseSchemasAllocator = new HeapDatabaseSchemasAllocator(droppedSchemaObjectsAllocator);
        this.schemaManagerAllocator = new HeapDatabasesSchemaManagerAllocator(droppedSchemaObjectsAllocator, databaseSchemasAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("muableIntToObjectMapAllocator", RefType.INSTANTIATED, IMutableIntSet.class, mutableIntToObjectMapAllocator);
        statisticsGatherer.addAllocators("schemaObjectIndexListAllocators", RefType.INSTANTIATED, schemaObjectIndexListAllocators);
        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.INSTANTIATED, droppedSchemaObjectsAllocator);
        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.INSTANTIATED, databaseSchemasAllocator);
        statisticsGatherer.addAllocators("schemaManagerAllocator", RefType.INSTANTIATED, schemaManagerAllocator);
    }

    public HeapDatabasesSchemaManagerAllocator getSchemaManagerAllocator() {
        return schemaManagerAllocator;
    }
}
