package dev.jdata.db.schema.model.schemamaps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.schema.model.schemamap.ISchemaMapBuilder;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseSchemaMapBuilders<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, ?>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, ?, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, ?>,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
                BUILDER>

        extends BaseSchemaMapsMutableBuilder<SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAP_BUILDER[]>
        implements ISchemaMapBuilders<SCHEMA_OBJECT, BUILDER> {

    private boolean initialized;

    BaseSchemaMapBuilders(AllocationType allocationType, IntFunction<SCHEMA_MAP_BUILDER[]> createSchemaMapBuildersArray) {
        super(allocationType, DDLObjectType.getNumObjectTypes(), createSchemaMapBuildersArray, (a, n, c) -> c.apply(Integers.checkUnsignedLongToUnsignedInt(n)));

        Objects.requireNonNull(createSchemaMapBuildersArray);
    }

    public final void initialize(ISchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocatedRenamed();

        this.initialized = Initializable.checkNotYetInitialized(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator = schemaMapBuilderAllocators.getAllocator(ddlObjectType);

            getMutable()[ddlObjectType.ordinal()] = schemaMapBuilderAllocator.createBuilder(1);
        }
    }

    public final void reset(ISchemaMapBuilderAllocators schemaMapBuilderAllocators) {

        Objects.requireNonNull(schemaMapBuilderAllocators);

        checkIsAllocatedRenamed();

        this.initialized = Initializable.checkResettable(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final int index = ddlObjectType.ordinal();

            final ISchemaMapBuilderAllocator<SCHEMA_OBJECT, SCHEMA_MAP, SCHEMA_MAP_BUILDER> schemaMapBuilderAllocator = schemaMapBuilderAllocators.getAllocator(ddlObjectType);

            final SCHEMA_MAP_BUILDER[] schemaMapBuilders = getMutable();

            final SCHEMA_MAP_BUILDER schemaMapBuilder = schemaMapBuilders[index];

            schemaMapBuilderAllocator.freeBuilder(schemaMapBuilder);

            schemaMapBuilders[index] = null;
        }
    }

    @Override
    public final BUILDER addSchemaObject(SCHEMA_OBJECT schemaObject) {

        Objects.requireNonNull(schemaObject);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER schemaMapBuilder = getMutable()[schemaObject.getDDLObjectType().ordinal()];

        schemaMapBuilder.addUnordered(schemaObject);

        return getThis();
    }

    @Override
    public final BUILDER addSchemaObjects(ISchemaObjects schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        Initializable.checkIsInitialized(initialized);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            addSchemaObjects(ddlObjectType, schemaObjects.getSchemaObjects(ddlObjectType));
        }

        return getThis();
    }

    @Override
    public final boolean isEmpty() {

        return IContainsView.isNullOrEmpty(getMutable());
    }

    protected final <T extends ISchemaMap<?>> T buildSchemaMap(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER schemaMapBuilder = getMutable()[ddlObjectType.ordinal()];

        @SuppressWarnings("unchecked")
        final T result = (T)schemaMapBuilder.buildOrNull();

        return result;
    }

    private void addSchemaObjects(DDLObjectType ddlObjectType, IObjectIterableElementsView<SCHEMA_OBJECT> schemaObjects) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObjects);

        Initializable.checkIsInitialized(initialized);

        final SCHEMA_MAP_BUILDER tableSchemaMapBuilder = getMutable()[ddlObjectType.ordinal()];

        tableSchemaMapBuilder.addUnordered(schemaObjects);
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [schemaMapBuilders=" + Arrays.toString(getMutable()) + ", initialized=" + initialized + "]";
    }

    @SuppressWarnings("unchecked")
    private BUILDER getThis() {

        return (BUILDER)this;
    }
}
