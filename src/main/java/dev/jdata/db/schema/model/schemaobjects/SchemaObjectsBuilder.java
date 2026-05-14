package dev.jdata.db.schema.model.schemaobjects;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IMutableIndexList;
import dev.jdata.db.utils.allocators.IFreeable;
import dev.jdata.db.utils.builders.BaseBuilder;
import dev.jdata.db.utils.checks.Checks;

abstract class SchemaObjectsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                INDEX_LIST extends IIndexList<SCHEMA_OBJECT>,
                MUTABLE_INDEX_LIST extends IMutableIndexList<SCHEMA_OBJECT>,
                HEAP_INDEX_LIST extends IIndexList<SCHEMA_OBJECT> & IHeapContainsMarker,
                INDEX_LIST_BUILDER extends IIndexListBuilder<SCHEMA_OBJECT, INDEX_LIST, HEAP_INDEX_LIST>,
                INDEX_LIST_ALLOCATOR extends IIndexListAllocator<SCHEMA_OBJECT, INDEX_LIST, MUTABLE_INDEX_LIST, INDEX_LIST_BUILDER>,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                HEAP_SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT> & IHeapContainsMarker>

        extends BaseBuilder<SCHEMA_OBJECTS, HEAP_SCHEMA_OBJECTS>
        implements ISchemaObjectsBuilder<SCHEMA_OBJECT, SCHEMA_OBJECTS, HEAP_SCHEMA_OBJECTS>, IFreeable {

    abstract SCHEMA_OBJECTS create(INDEX_LIST schemaObjects, IntFunction<SCHEMA_OBJECT[]> createValuesArray);
    abstract HEAP_SCHEMA_OBJECTS heapCreate(INDEX_LIST schemaObjects, IntFunction<SCHEMA_OBJECT[]> createValuesArray);

    private IntFunction<SCHEMA_OBJECT[]> createValuesArray;
    private INDEX_LIST_ALLOCATOR indexListAllocator;

    private INDEX_LIST_BUILDER schemaObjectsBuilder;

    SchemaObjectsBuilder(AllocationType allocationType, INDEX_LIST_ALLOCATOR indexListAllocator, IntFunction<SCHEMA_OBJECT[]> createValuesArray) {
        super(allocationType);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);
        this.indexListAllocator = Objects.requireNonNull(indexListAllocator);
    }

    SchemaObjectsBuilder(AllocationType allocationType, int minimumCapacity, INDEX_LIST_ALLOCATOR indexListAllocator, IntFunction<SCHEMA_OBJECT[]> createValuesArray) {
        this(allocationType, indexListAllocator, createValuesArray);

        Objects.requireNonNull(createValuesArray);
        Objects.requireNonNull(indexListAllocator);
        checkIsIntMinimumCapacity(minimumCapacity);

        this.createValuesArray = Objects.requireNonNull(createValuesArray);
        this.indexListAllocator = Objects.requireNonNull(indexListAllocator);

        initialize(minimumCapacity);
    }

    public final void initialize(int minimumCapacity) {

        checkIsIntMinimumCapacity(minimumCapacity);

        this.schemaObjectsBuilder = indexListAllocator.createBuilder(minimumCapacity);
    }

    @Override
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
    public final void setSchemaMap(ISchemaObjects<SCHEMA_OBJECT> schemaMap) {

        Objects.requireNonNull(schemaMap);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final SCHEMA_OBJECTS build() {

        return create(schemaObjectsBuilder.buildNotEmpty(), createValuesArray);
    }

    @Override
    protected final HEAP_SCHEMA_OBJECTS heapBuild() {

        return heapCreate(schemaObjectsBuilder.buildNotEmpty(), createValuesArray);
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [createValuesArray=" + createValuesArray + ", indexListAllocator=" + indexListAllocator
                + ", schemaObjectsBuilder=" + schemaObjectsBuilder + "]";
    }
}
