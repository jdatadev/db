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

    public static final class CacheDiffSchemaMapsBuilderAllocator

            extends DiffSchemaMapsBuilderAllocator<CachedSchemaMap<?>, CachedBuilder, CachedDiffSchemaMaps>
            implements IAllocators {

        private final NodeObjectCache<CachedBuilder> objectCache;

        public CacheDiffSchemaMapsBuilderAllocator() {

            this.objectCache = new NodeObjectCache<>(CachedBuilder::new);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addNodeObjectCache("objectCache", CachedBuilder.class, objectCache);
        }

        @Override
        CachedBuilder allocateDiffSchemaMapsBuilder() {

            return objectCache.allocate();
        }

        @Override
        void freeDiffSchemaMapsBuilder(CachedBuilder builder) {

            Objects.requireNonNull(builder);

            objectCache.free(builder);
        }
    }

    public static final class CachedBuilder extends DiffSchemaMaps.DiffSchemaMapsBuilder<CachedSchemaMap<?>, CachedDiffSchemaMaps, CachedBuilder> {

        public CachedBuilder() {
            super(CachedSchemaMap[]::new);
        }

        public CachedDiffSchemaMaps build() {

            checkIsAllocated();

            return new CachedDiffSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX), mapOrEmpty(DDLObjectType.TRIGGER),
                    mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }

        @Override
        protected SchemaMap<?, ?, ?, ?, ?> makeEmptySchema() {

            return CachedSchemaMap.empty();
        }
    }

    public CachedDiffSchemaMaps(CachedSchemaMap<Table> tables, CachedSchemaMap<View> views, CachedSchemaMap<Index> indices, CachedSchemaMap<Trigger> triggers,
            CachedSchemaMap<DBFunction> functions, CachedSchemaMap<Procedure> procedures) {
        super(CachedSchemaMap[]::new, tables, views, indices, triggers, functions, procedures);
    }
}
