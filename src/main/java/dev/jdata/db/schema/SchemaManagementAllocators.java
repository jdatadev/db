package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects.IDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IIntSetAllocator;
import dev.jdata.db.utils.allocators.IntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class SchemaManagementAllocators implements IAllocators {

    private final IntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator;
    private final SchemaObjectIndexListAllocators schemaObjectIndexListAllocators;
    private final DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;
    private final DatabaseSchemasAllocator databaseSchemasAllocator;
    private final DatabasesSchemaManagerAllocator schemaManagerAllocator;

    private static final class DatabaseSchemasAllocator implements IDatabaseSchemasAllocator {

        private final IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;

        private final NodeObjectCache<DroppedSchemaObjects> droppedSchemaObjectsCache;

        DatabaseSchemasAllocator(IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {

            this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);

            this.droppedSchemaObjectsCache = new NodeObjectCache<>(DroppedSchemaObjects::new);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
            statisticsGatherer.addNodeObjectCache("droppedSchemaObjectsCache", DroppedSchemaObjects.class, droppedSchemaObjectsCache);
        }

        @Override
        public IDroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator() {

            return droppedSchemaObjectsAllocator;
        }

        @Override
        public DroppedSchemaObjects allocateDroppedSchemaObjects() {

            return droppedSchemaObjectsCache.allocate();
        }

        @Override
        public void freeDroppedSchemaObjects(DroppedSchemaObjects droppedSchemaObjects) {

            Objects.requireNonNull(droppedSchemaObjects);

            droppedSchemaObjectsCache.free(droppedSchemaObjects);
        }
    };

    public SchemaManagementAllocators(IIntSetAllocator intSetAllocator) {

        Objects.requireNonNull(intSetAllocator);

        this.intToObjectMapAllocator = new IntToObjectMapAllocator<>(MutableIntBucketSet[]::new);

        this.schemaObjectIndexListAllocators = new SchemaObjectIndexListAllocators();
        this.droppedSchemaObjectsAllocator = new DroppedSchemaObjectsAllocator(intSetAllocator, intToObjectMapAllocator);
        this.databaseSchemasAllocator = new DatabaseSchemasAllocator(droppedSchemaObjectsAllocator);
        this.schemaManagerAllocator = new DatabasesSchemaManagerAllocator(droppedSchemaObjectsAllocator, databaseSchemasAllocator, schemaObjectIndexListAllocators);
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

    public DatabasesSchemaManagerAllocator getSchemaManagerAllocator() {
        return schemaManagerAllocator;
    }
}
