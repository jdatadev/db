package dev.jdata.db.schema.model.schemamaps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocator;
import dev.jdata.db.schema.allocators.model.SchemaMapBuilderAllocators;
import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.SchemaMap.SchemaMapBuilder;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class BaseSchemaMapBuilders<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IBaseIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IBaseIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends SchemaMap<SCHEMA_OBJECT, INDEX_LIST, SCHEMA_MAP>,
                SCHEMA_MAP_BUILDER extends SchemaMapBuilder<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends ObjectCacheNode
        implements ISchemaMapBuilders<SCHEMA_OBJECT> {

    private final SCHEMA_MAP_BUILDER[] schemaMapBuilders;

    private boolean initialized;

    BaseSchemaMapBuilders(AllocationType allocationType, IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(allocationType);

        Objects.requireNonNull(createSchemaMapBuildersArray);

        this.schemaMapBuilders = createSchemaMapBuildersArray.apply(DDLObjectType.getNumObjectTypes());
    }

    public final void initialize(SchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocated();

        this.initialized = Initializable.checkNotYetInitialized(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final SchemaMapBuilderAllocator<SCHEMA_OBJECT, INDEX_LIST, INDEX_LIST_BUILDER, INDEX_LIST_ALLOCATOR, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator
                    = schemaMapBuilderAllocators.getAllocator(ddlObjectType);

            schemaMapBuilders[ddlObjectType.ordinal()] = schemaMapBuilderAllocator.createBuilder(1);
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

            schemaMapBuilderAllocator.freeBuilder(schemaMapBuilder);

            schemaMapBuilders[index] = null;
        }
    }

    @Override
    public final void addSchemaObject(SCHEMA_OBJECT schemaObject) {

        Objects.requireNonNull(schemaObject);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER schemaMapBuilder = schemaMapBuilders[schemaObject.getDDLObjectType().ordinal()];

        schemaMapBuilder.addUnordered(schemaObject);
    }

    @Override
    public final void addSchemaObjects(ISchemaObjects schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        Initializable.checkIsInitialized(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            addSchemaObjects(ddlObjectType, schemaObjects.getSchemaObjects(ddlObjectType));
        }
    }

    protected final <T extends SchemaMap<?, ?, ?>> T buildSchemaMap(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER schemaMapBuilder = schemaMapBuilders[ddlObjectType.ordinal()];

        @SuppressWarnings("unchecked")
        final T result = (T)schemaMapBuilder.buildOrNull();

        return result;
    }

    private void addSchemaObjects(DDLObjectType ddlObjectType, IObjectIterableElementsView<SCHEMA_OBJECT> schemaObjects) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObjects);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER tableSchemaMapBuilder = schemaMapBuilders[ddlObjectType.ordinal()];

        tableSchemaMapBuilder.addUnordered(schemaObjects);
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [schemaMapBuilders=" + Arrays.toString(schemaMapBuilders) + ", initialized=" + initialized + "]";
    }
}
