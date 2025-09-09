package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.Objects;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class CachedDiffSchemaMaps extends DiffSchemaMaps<CachedSchemaMap<?>> {

    public static final class CacheSimpleDiffSchemaMapsBuilderAllocator

            extends SimpleDiffSchemaMapsBuilderAllocator<CachedSchemaMap<?>, CachedDiffSchemaMapsBuilder, CachedDiffSchemaMaps>
            implements IAllocators {

        private final NodeObjectCache<CachedDiffSchemaMapsBuilder> objectCache;

        public CacheSimpleDiffSchemaMapsBuilderAllocator() {

            this.objectCache = new NodeObjectCache<>(CachedDiffSchemaMapsBuilder::new);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addNodeObjectCache("objectCache", CachedDiffSchemaMapsBuilder.class, objectCache);
        }

        @Override
        CachedDiffSchemaMapsBuilder allocateDiffSchemaMapsBuilder() {

            return objectCache.allocate();
        }

        @Override
        void freeDiffSchemaMapsBuilder(CachedDiffSchemaMapsBuilder builder) {

            Objects.requireNonNull(builder);

            objectCache.free(builder);
        }
    }

    public static final class CachedDiffSchemaMapsBuilder extends SimpleDiffSchemaMapsBuilder<CachedSchemaMap<?>, CachedDiffSchemaMaps, CachedDiffSchemaMapsBuilder> {

        public CachedDiffSchemaMapsBuilder() {
            super(CachedSchemaMap[]::new);
        }

        public CachedDiffSchemaMaps build() {

            checkIsAllocated();

            return new CachedDiffSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX), mapOrEmpty(DDLObjectType.TRIGGER),
                    mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }

        @Override
        protected SchemaMap<?, ?, ?> makeEmptySchema() {

            return CachedSchemaMap.empty();
        }
    }

    public CachedDiffSchemaMaps(CachedSchemaMap<Table> tables, CachedSchemaMap<View> views, CachedSchemaMap<Index> indices, CachedSchemaMap<Trigger> triggers,
            CachedSchemaMap<DBFunction> functions, CachedSchemaMap<Procedure> procedures) {
        super(CachedSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
