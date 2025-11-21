package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IMutableIndexList;
import dev.jdata.db.utils.adt.maps.IHeapMutableLongToObjectDynamicMapAllocator;
import dev.jdata.db.utils.builders.BaseBuilder;
import dev.jdata.db.utils.checks.Checks;

abstract class SchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                MUTABLE extends IMutableIndexList<SCHEMA_OBJECT>,
                HEAP_INDEX_LIST extends IIndexList<SCHEMA_OBJECT> & IHeapContainsMarker,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, HEAP_INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, MUTABLE, INDEX_LIST_BUILDER>,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                HEAP_SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT> & IHeapContainsMarker>

        extends BaseBuilder<SCHEMA_MAP, HEAP_SCHEMA_MAP>
        implements ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP> {

    abstract SCHEMA_MAP create(INDEX_LIST schemaObjects, IntFunction<SCHEMA_OBJECT[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator);

    abstract HEAP_SCHEMA_MAP heapCreate(INDEX_LIST schemaObjects, IntFunction<SCHEMA_OBJECT[]> createValuesArray,
            IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator);

    private IntFunction<SCHEMA_OBJECT[]> createValuesArray;
    private INDEX_LIST_ALLOCATOR indexListAllocator;
    private IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator;

    private INDEX_LIST_BUILDER schemaObjectsBuilder;

    SchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECT[]> createValuesArray, INDEX_LIST_ALLOCATOR indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator) {

        super(allocationType);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);
        this.indexListAllocator = Objects.requireNonNull(indexListAllocator);
        this.longToObjectMapAllocator = Objects.requireNonNull(longToObjectMapAllocator);
    }

    SchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECT[]> createValuesArray, INDEX_LIST_ALLOCATOR indexListAllocator,
            IHeapMutableLongToObjectDynamicMapAllocator<SCHEMA_OBJECT> longToObjectMapAllocator, int initialCapacity) {
        this(allocationType, createValuesArray, indexListAllocator, longToObjectMapAllocator);

        initialize(initialCapacity);
    }

    public final void initialize(int initialCapacity) {

        this.schemaObjectsBuilder = indexListAllocator.createBuilder(initialCapacity);
    }

    public final void free() {

        indexListAllocator.freeBuilder(schemaObjectsBuilder);

        this.createValuesArray = null;

        this.schemaObjectsBuilder = null;
    }

    @Override
    public final boolean isEmpty() {

        return schemaObjectsBuilder.isEmpty();
    }

    @Override
    public final void addUnordered(SCHEMA_OBJECT schemaObject) {

        Objects.requireNonNull(schemaObject);

        schemaObjectsBuilder.addTail(schemaObject);
    }

    @Override
    public final void addUnordered(@SuppressWarnings("unchecked") SCHEMA_OBJECT... instances) {

        Checks.isNotEmpty(instances);

        schemaObjectsBuilder.addTail(instances);
    }

    @Override
    public final void addUnordered(IObjectIterableElementsView<SCHEMA_OBJECT> schemaObjects) {

        Checks.isNotEmpty(schemaObjects);

        schemaObjectsBuilder.addTail(schemaObjects);
    }

    @Override
    protected final SCHEMA_MAP build() {

        return create(schemaObjectsBuilder.buildNotEmpty(), createValuesArray, longToObjectMapAllocator);
    }

    @Override
    protected final HEAP_SCHEMA_MAP heapBuild() {

        return heapCreate(schemaObjectsBuilder.buildNotEmpty(), createValuesArray, longToObjectMapAllocator);
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [createValuesArray=" + createValuesArray + ", indexListAllocator=" + indexListAllocator
                + ", longToObjectMapAllocator=" + longToObjectMapAllocator + ", schemaObjectsBuilder=" + schemaObjectsBuilder + "]";
    }
}
