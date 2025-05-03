package dev.jdata.db.schema;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.Builder;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects.IDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps.CompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.allocators.ObjectCache;

public final class DatabasesSchemaManagerAllocator implements IAllocators, CompleteSchemaMapsBuilderAllocator {

    private static abstract class BaseDDLObjectTypeCaches<T, U extends IObjectCache<T>> extends DDLObjectTypeInstances<U> implements IAllocators {

        abstract Class<T> getCachedObjectClass();

        BaseDDLObjectTypeCaches(IntFunction<U[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, U>, U> createElement) {
            super(createArray, createElement);
        }

        @Override
        public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

            Objects.requireNonNull(statisticsGatherer);

            final Class<T> cachedObjectClass = getCachedObjectClass();

            for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

                getObjectCache(ddlObjectType).addCache(statisticsGatherer, ddlObjectType.name(), cachedObjectClass);
            }
        }

        final U getObjectCache(DDLObjectType ddlObjectType) {

            return getInstance(ddlObjectType);
        }
    }

    private static abstract class DDLObjectTypeCaches<T> extends BaseDDLObjectTypeCaches<T, ObjectCache<T>> {

        DDLObjectTypeCaches(IntFunction<T[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, ObjectCache<T>>, T> createInstance) {
            super(ObjectCache[]::new, (t, g) -> new ObjectCache<>(() -> createInstance.apply(t, g), createArray));
        }
    }

    static abstract class NodeDDLObjectTypeCaches<T extends ObjectCacheNode> extends BaseDDLObjectTypeCaches<T, NodeObjectCache<T>> {

        NodeDDLObjectTypeCaches(BiFunction<DDLObjectType, Function<DDLObjectType, NodeObjectCache<T>>, T> createInstance) {
            super(NodeObjectCache[]::new, (t, g) -> new NodeObjectCache<>(() -> createInstance.apply(t, g)));
        }
    }

    static final class SchemaMapBuildersCache extends NodeDDLObjectTypeCaches<SchemaMap.Builder<? extends SchemaObject>> {

        SchemaMapBuildersCache(SchemaObjectIndexListAllocators indexListAllocators) {
            super((t, g) -> createBuilder(t, indexListAllocators, g));
        }

        @Override
        Class<Builder<? extends SchemaObject>> getCachedObjectClass() {

            @SuppressWarnings("unchecked")
            final Class<Builder<? extends SchemaObject>> result = (Class<Builder<? extends SchemaObject>>)(Class<?>)SchemaMap.Builder.class;

            return result;
        }

        private static <T extends SchemaObject> SchemaMap.Builder<T> createBuilder(DDLObjectType ddlObjectType, SchemaObjectIndexListAllocators indexListAllocators,
                Function<DDLObjectType, NodeObjectCache<SchemaMap.Builder<? extends SchemaObject>>> objectCacheGetter) {

            final IntFunction<T[]> createArray = l -> {

                @SuppressWarnings("unchecked")
                final T[] schemaObjectArray = (T[])Array.newInstance(ddlObjectType.getSchemaObjectType(), l);

                return schemaObjectArray;
            };

            final NodeObjectCache<SchemaMap.Builder<? extends SchemaObject>> objectCache = objectCacheGetter.apply(ddlObjectType);

            final SchemaMapBuilderAllocator<T> schemaMapBuilderAllocator = null;

            return SchemaMap.createBuilder(0, null); // indexListAllocators.getIndexListAllocator(ddlObjectType));
        }
    }

    private final IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator;
    private final IDatabaseSchemasAllocator databaseSchemasAllocator;
    private final SchemaMapBuildersCache schemaMapBuildersCache;
    private final NodeObjectCache<CompleteSchemaMaps.Builder> completeSchemaMapsBuilderCache;

    DatabasesSchemaManagerAllocator(IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            SchemaObjectIndexListAllocators schemaObjectIndexListAllocators) {

        this.droppedSchemaObjectsAllocator = Objects.requireNonNull(droppedSchemaObjectsAllocator);
        this.databaseSchemasAllocator = Objects.requireNonNull(databaseSchemasAllocator);

        this.schemaMapBuildersCache = new SchemaMapBuildersCache(schemaObjectIndexListAllocators);

        final CompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator = this;

        final Supplier<CompleteSchemaMaps.Builder> completeSchemMapBuilderSupplier = () -> CompleteSchemaMaps.createBuilder(completeSchemaMapsBuilderAllocator);
        this.completeSchemaMapsBuilderCache = new NodeObjectCache<>(completeSchemMapBuilderSupplier);
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("droppedSchemaObjectsAllocator", RefType.PASSED, droppedSchemaObjectsAllocator);
        statisticsGatherer.addAllocators("databaseSchemasAllocator", RefType.PASSED, databaseSchemasAllocator);
        statisticsGatherer.addAllocators("schemaMapBuildersCache", RefType.INSTANTIATED, schemaMapBuildersCache);
        statisticsGatherer.addNodeObjectCache("completeSchemaMapsBuilderCache", CompleteSchemaMaps.Builder.class, completeSchemaMapsBuilderCache);
    }

    IDroppedSchemaObjectsAllocator getDroppedSchemaObjectsAllocator() {
        return droppedSchemaObjectsAllocator;
    }

    DroppedSchemaObjects allocateDroppedSchemaObjects() {

        return databaseSchemasAllocator.allocateDroppedSchemaObjects();
    }

    void freeDroppedSchemaObjects(DroppedSchemaObjects droppedSchemaObjects) {

        databaseSchemasAllocator.freeDroppedSchemaObjects(droppedSchemaObjects);
    }

    @Override
    public CompleteSchemaMaps.Builder allocateCompleteSchemaMapsBuilder() {

        return completeSchemaMapsBuilderCache.allocate();
    }

    @Override
    public void freeCompleteSchemaMapsBuilder(CompleteSchemaMaps.Builder builder) {

        completeSchemaMapsBuilderCache.free(builder);
    }
}
