package dev.jdata.db.schema.allocators;

import java.util.Objects;

import dev.jdata.db.schema.allocators.common.heap.HeapSchemaObjectIndexListAllocators;
import dev.jdata.db.schema.allocators.databases.heap.HeapDatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.allocators.model.diff.dropped.heap.HeapDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.schemas.DatabaseSchemasAllocator;
import dev.jdata.db.schema.allocators.schemas.heap.HeapDatabaseSchemasAllocator;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IntToObjectMapAllocator;

/**
 * Root allocator for schema management.
 */
public final class SchemaManagementAllocators implements IAllocators {

    private final IntToObjectMapAllocator<MutableIntMaxDistanceNonBucketSet> intToObjectMapAllocator;
    private final HeapSchemaObjectIndexListAllocators<?> schemaObjectIndexListAllocators;
    private final HeapDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;
    private final DatabaseSchemasAllocator databaseSchemasAllocator;
    private final HeapDatabasesSchemaManagerAllocator schemaManagerAllocator;

    public SchemaManagementAllocators(IMutableIntSetAllocator<?> intSetAllocator) {

        Objects.requireNonNull(intSetAllocator);

        this.intToObjectMapAllocator = new IntToObjectMapAllocator<>(MutableIntMaxDistanceNonBucketSet[]::new);

        this.schemaObjectIndexListAllocators = HeapSchemaObjectIndexListAllocators.INSTANCE;
        this.droppedSchemaObjectsAllocator = new HeapDroppedSchemaObjectsAllocator(intSetAllocator, intToObjectMapAllocator);
        this.databaseSchemasAllocator = new HeapDatabaseSchemasAllocator(droppedSchemaObjectsAllocator);
        this.schemaManagerAllocator = new HeapDatabasesSchemaManagerAllocator(droppedSchemaObjectsAllocator, databaseSchemasAllocator);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("intToObjectMapAllocator", RefType.INSTANTIATED, MutableIntBucketSet.class, intToObjectMapAllocator);
        statisticsGatherer.addAllocators("schemaObjectIndexListAllocators", RefType.INSTANTIATED, schemaObjectIndexListAllocators);
        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.INSTANTIATED, droppedSchemaObjectsAllocator);
        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.INSTANTIATED, databaseSchemasAllocator);
        statisticsGatherer.addAllocators("schemaManagerAllocator", RefType.INSTANTIATED, schemaManagerAllocator);
    }

    public HeapDatabasesSchemaManagerAllocator getSchemaManagerAllocator() {
        return schemaManagerAllocator;
    }
}
