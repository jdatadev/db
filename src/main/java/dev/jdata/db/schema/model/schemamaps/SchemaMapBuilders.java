package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public abstract class SchemaMapBuilders<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IndexList.IndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                INDEX_LIST_ALLOCATOR extends IndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, ?>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>,
                COMPLETE_SCHEMA_MAPS extends CompleteSchemaMaps<? extends SchemaMap<?, ?, ?, ?, ?>>>

        extends ObjectCacheNode {

    public abstract COMPLETE_SCHEMA_MAPS build();

    private final SCHEMA_MAP_BUILDER[] schemaMapBuilders;

    private boolean initialized;

    public SchemaMapBuilders(IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {

        this.schemaMapBuilders = createSchemaMapBuildersArray.apply(DDLObjectType.getNumObjectTypes());
    }

    public final void initialize(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocated();

        this.initialized = Initializable.checkNotYetInitialized(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator
                    = schemaMapBuilderAllocators.getAllocator(ddlObjectType);

            schemaMapBuilders[ddlObjectType.ordinal()] = SchemaMap.createBuilder(1, schemaMapBuilderAllocator);
        }
    }

    public final void reset(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocated();

        this.initialized = Initializable.checkResettable(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final int index = ddlObjectType.ordinal();

            final SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator
                    = schemaMapBuilderAllocators.getAllocator(ddlObjectType);

            final SCHEMA_MAP_BUILDER schemaMapBuilder = schemaMapBuilders[index];

            schemaMapBuilderAllocator.freeSchemaMapBuilder(schemaMapBuilder);

            schemaMapBuilders[index] = null;
        }
    }

    public final void addSchemaObject(DDLObjectType ddlObjectType, SCHEMA_OBJECT schemaObject) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObject);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER tableSchemaMapBuilder = schemaMapBuilders[ddlObjectType.ordinal()];

        tableSchemaMapBuilder.add(schemaObject);
    }

    final <T extends SchemaMap<?, ?, ?, ?, ?>> T build(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER schemaMapBuilder = schemaMapBuilders[ddlObjectType.ordinal()];

        @SuppressWarnings("unchecked")
        final T result = (T)schemaMapBuilder.build();

        return result;
    }
}
