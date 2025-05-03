package dev.jdata.db.schema.model.diff.schemamaps;

import java.util.Objects;
import java.util.function.Function;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamaps.BaseSchemaMaps;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.NodeObjectCache;

public final class DiffSchemaMaps extends BaseSchemaMaps<SchemaMap<?>> {

    public static abstract class DiffSchemaMapsBuilderAllocator {

        abstract Builder allocateDiffSchemaMapsBuilder();

        abstract void freeDiffSchemaMapsBuilder(Builder builder);
    }

    public static final class HeapDiffSchemaMapsBuilderAllocator extends DiffSchemaMapsBuilderAllocator {

        public static final HeapDiffSchemaMapsBuilderAllocator INSTANCE = new HeapDiffSchemaMapsBuilderAllocator();

        private HeapDiffSchemaMapsBuilderAllocator() {

        }

        @Override
        Builder allocateDiffSchemaMapsBuilder() {

            return new Builder(this);
        }

        @Override
        void freeDiffSchemaMapsBuilder(Builder builder) {

            Objects.requireNonNull(builder);
        }
    }

    public static final class CacheDiffSchemaMapsBuilderAllocator extends DiffSchemaMapsBuilderAllocator implements IAllocators {

        private final NodeObjectCache<Builder> objectCache;

        public CacheDiffSchemaMapsBuilderAllocator() {

            this.objectCache = new NodeObjectCache<>(Builder::new);
        }

        @Override
        public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            statisticsGatherer.addNodeObjectCache("objectCache", Builder.class, objectCache);
        }

        @Override
        Builder allocateDiffSchemaMapsBuilder() {

            return objectCache.allocate();
        }

        @Override
        void freeDiffSchemaMapsBuilder(Builder builder) {

            Objects.requireNonNull(builder);

            objectCache.free(builder);
        }
    }

    public static Builder createBuilder(DiffSchemaMapsBuilderAllocator allocator) {

        return allocator.allocateDiffSchemaMapsBuilder();
    }

    public static final class Builder extends BaseBuilder<Builder> {

        private Builder() {

        }

        private Builder(DiffSchemaMapsBuilderAllocator builderAllocator) {

            initialize(builderAllocator);
        }

        void initialize(DiffSchemaMapsBuilderAllocator builderAllocator) {

            checkIsNotAllocated();
        }

        public DiffSchemaMaps build() {

            checkIsAllocated();

            return new DiffSchemaMaps(mapOrEmpty(DDLObjectType.TABLE), mapOrEmpty(DDLObjectType.VIEW), mapOrEmpty(DDLObjectType.INDEX), mapOrEmpty(DDLObjectType.TRIGGER),
                    mapOrEmpty(DDLObjectType.FUNCTION), mapOrEmpty(DDLObjectType.PROCEDURE));
        }
    }

    private static final DiffSchemaMaps EMPTY = new DiffSchemaMaps(SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(), SchemaMap.empty(),
            SchemaMap.empty());

    public DiffSchemaMaps(SchemaMap<Table> tables, SchemaMap<View> views, SchemaMap<Index> indices, SchemaMap<Trigger> triggers, SchemaMap<DBFunction> functions,
            SchemaMap<Procedure> procedures) {
        super(Function.identity(), SchemaMap[]::new, Function.identity(), tables, views, indices, triggers, functions, procedures);
    }
}
