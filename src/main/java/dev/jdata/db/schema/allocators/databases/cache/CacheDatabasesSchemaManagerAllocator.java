package dev.jdata.db.schema.allocators.databases.cache;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.DDLObjectTypeInstances;
import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.cache.CacheCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.cache.CachedSchemaMap;
import dev.jdata.db.schema.model.cache.CachedSchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.CachedCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.CachedCompleteSchemaMaps.CachedCompleteSchemaBuilder;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.allocators.IObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.allocators.ObjectCache;

final class CacheDatabasesSchemaManagerAllocator

        extends DatabasesSchemaManagerAllocator<CachedSchemaMap<?>, CachedCompleteSchemaMaps, CachedCompleteSchemaBuilder>
        implements IAllocators {

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

    private static abstract class NodeDDLObjectTypeCaches<T extends ObjectCacheNode> extends BaseDDLObjectTypeCaches<T, NodeObjectCache<T>> {

        NodeDDLObjectTypeCaches(BiFunction<DDLObjectType, Function<DDLObjectType, NodeObjectCache<T>>, T> createInstance) {
            super(NodeObjectCache[]::new, (t, g) -> new NodeObjectCache<>(() -> createInstance.apply(t, g)));
        }
    }

    private static final class SchemaMapBuildersCache extends NodeDDLObjectTypeCaches<CachedSchemaMapBuilder<? extends SchemaObject>> {

        private SchemaMapBuildersCache(
                BiFunction<DDLObjectType, Function<DDLObjectType, NodeObjectCache<CachedSchemaMapBuilder<? extends SchemaObject>>>, CachedSchemaMapBuilder<? extends SchemaObject>> createInstance) {
            super(createInstance);

            throw new UnsupportedOperationException();
        }

        @Override
        Class<CachedSchemaMapBuilder<? extends SchemaObject>> getCachedObjectClass() {

            throw new UnsupportedOperationException();
        }

/*
        SchemaMapBuildersCache(CacheSchemaObjectIndexListAllocators indexListAllocators) {
            super((t, g) -> createBuilder(t, indexListAllocators, g));
        }

        @Override
        Class<CachedSchemaMapBuilder<? extends SchemaObject>> getCachedObjectClass() {

            @SuppressWarnings("unchecked")
            final Class<CachedSchemaMapBuilder<? extends SchemaObject>> result
                    = (Class<CachedSchemaMapBuilder<? extends SchemaObject>>)(Class<?>)CachedSchemaMapBuilder.class;

            return result;
        }

        private static CachedSchemaMapBuilder<? extends SchemaObject> createBuilder(DDLObjectType ddlObjectType,
                CacheSchemaObjectIndexListAllocators indexListAllocators,
                Function<DDLObjectType, NodeObjectCache<HeapSchemaMap.HeapSchemaMapBuilder<? extends SchemaObject>>> objectCacheGetter) {

            final IntFunction<T[]> createArray = l -> {

                @SuppressWarnings("unchecked")
                final T[] schemaObjectArray = (T[])Array.newInstance(ddlObjectType.getSchemaObjectType(), l);

                return schemaObjectArray;
            };

            final NodeObjectCache<HeapSchemaMapBuilder<? extends SchemaObject>> objectCache = objectCacheGetter.apply(ddlObjectType);

            final CacheSchemaMapBuilderAllocator<T> schemaMapBuilderAllocator = null;

            return SchemaMap.createBuilder(0, indexListAllocators.getIndexListAllocator(ddlObjectType));
        }
*/
    }

    private final SchemaMapBuildersCache schemaMapBuildersCache;

    CacheDatabasesSchemaManagerAllocator(DroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator,
            CacheCompleteSchemaMapsBuilderAllocator completeSchemaMapsBuilderAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, completeSchemaMapsBuilderAllocator);

        this.schemaMapBuildersCache = null; // new SchemaMapBuildersCache(completeSchemaMapsBuilderAllocator);
/*

        final ICompleteSchemaMapsBuilderAllocator<U, V> completeSchemaMapsBuilderAllocator = this;
*/
    }

    @Override
    public void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addAllocators("schemaMapBuildersCache", RefType.INSTANTIATED, schemaMapBuildersCache);
    }
}
