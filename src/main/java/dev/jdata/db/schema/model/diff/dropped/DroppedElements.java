package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.maps.IIntToObjectMapView;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IIntSetView;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

final class DroppedElements<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends ObjectCacheNode
        implements IDroppedElements {

    private static final int DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT = 0;

    private static final int DROPPED_OBJECTS_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
    private static final int DROPPED_COLUMNS_MAP_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
    private static final int DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY = CapacityExponents.computeIntCapacityFromExponent(DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT);

    private T droppedObjectIds;
    private U droppedColumnsByColumnObjectId;

    private IMutableIntSetAllocator<T> scratchMutableIntSetAllocator;
    private int sratchColumnsObjectId;

    DroppedElements(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final boolean isDroppedObject(SchemaObject schemaObject) {

        Objects.requireNonNull(schemaObject);

        return droppedObjectIds != null ? droppedObjectIds.contains(schemaObject.getId()) : false;
    }

    @Override
    public final boolean isDroppedColumn(SchemaObject schemaObject, Column column) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(column);

        final IIntSetView droppedColumnsSet = droppedColumnsByColumnObjectId.get(schemaObject.getId());

        return droppedColumnsSet != null ? droppedColumnsSet.contains(column.getId()) : false;
    }

    final void add(DroppedElements<?, ?> other, IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator,
            IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(mutableIntToObjectMapAllocator);
        Objects.requireNonNull(mutableIntSetAllocator);

        final IMutableIntSet otherDroppedObjects = other.droppedObjectIds;

        if (otherDroppedObjects != null) {

            if (droppedObjectIds == null) {

                this.droppedObjectIds = mutableIntSetAllocator.createMutable(DROPPED_OBJECTS_INITIAL_CAPACITY);
            }

            this.droppedObjectIds.addUnordered(otherDroppedObjects);
        }

        final IMutableIntToObjectWithRemoveStaticMap<? extends IIntSetView> otherDroppedColumns = other.droppedColumnsByColumnObjectId;

        if (otherDroppedColumns != null) {

            if (droppedColumnsByColumnObjectId == null) {

                this.droppedColumnsByColumnObjectId = mutableIntToObjectMapAllocator.createMutable(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY);
            }

            addDroppedColumns(otherDroppedColumns, mutableIntSetAllocator);
        }
    }

    final void initialize(DroppedElements<?, ?> other, IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator,
            IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(mutableIntToObjectMapAllocator);
        Objects.requireNonNull(mutableIntSetAllocator);

        initializeDroppedSchemaObjects(other.droppedObjectIds, mutableIntSetAllocator);

        if (other.droppedColumnsByColumnObjectId != null) {

            initializeDroppedColumns(droppedColumnsByColumnObjectId, mutableIntToObjectMapAllocator, mutableIntSetAllocator);
        }
        else {
            if (droppedColumnsByColumnObjectId != null) {

                droppedColumnsByColumnObjectId.forEachValue(null, (s, p) -> s.clear());
            }
        }
    }

    private void initializeDroppedSchemaObjects(IIntSetView schemaObjects, IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        if (droppedObjectIds != null) {

            droppedObjectIds.clear();
        }
        else {
            this.droppedObjectIds = mutableIntSetAllocator.createMutable(DROPPED_OBJECTS_INITIAL_CAPACITY);
        }

        droppedObjectIds.addUnordered(schemaObjects);
    }

    private void initializeDroppedColumns(IIntToObjectMapView<T> droppedColumnsToAdd, IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator,
            IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        if (droppedColumnsByColumnObjectId != null) {

            droppedColumnsByColumnObjectId.forEachValue(null, (s, p) -> s.clear());
        }
        else {
            this.droppedColumnsByColumnObjectId = mutableIntToObjectMapAllocator.createMutable(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY);
        }

        addDroppedColumns(droppedColumnsToAdd, mutableIntSetAllocator);
    }

    private void addDroppedColumns(IIntToObjectMapView<? extends IIntSetView> droppedColumnsToAdd, IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        this.scratchMutableIntSetAllocator = mutableIntSetAllocator;

        droppedColumnsToAdd.forEachKeyAndValue(this, (coId, set, instance) -> {

            this.sratchColumnsObjectId = coId;

            set.forEach(instance, (c, i) -> i.addDroppedColumn(i.sratchColumnsObjectId, c, i.scratchMutableIntSetAllocator));
        });

        this.scratchMutableIntSetAllocator = null;
    }

    final void addDroppedColumn(int columnsObjectId, int columnId, IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        Checks.isSchemaObjectId(columnsObjectId);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(mutableIntSetAllocator);

        T droppedColumnsSet = droppedColumnsByColumnObjectId.get(columnsObjectId);

        if (droppedColumnsSet == null) {

            droppedColumnsSet = mutableIntSetAllocator.createMutable(DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY);

            droppedColumnsByColumnObjectId.put(columnsObjectId, droppedColumnsSet);
        }
        else {
            if (droppedColumnsSet.contains(columnId)) {

                throw new IllegalStateException();
            }
        }

        droppedColumnsSet.addUnordered(columnId);
    }

    final void addDroppedSchemaObject(SchemaObject schemaObject, IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(mutableIntSetAllocator);

        addDroppedSchemaObject(schemaObject.getId(), mutableIntSetAllocator);
    }

    private void addDroppedSchemaObject(int schemaObjectId, IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        T dropped = droppedObjectIds;

        if (dropped == null) {

            dropped = this.droppedObjectIds = mutableIntSetAllocator.createMutable(DROPPED_OBJECTS_INITIAL_CAPACITY);
        }

        dropped.addUnordered(schemaObjectId);
    }

    final void free(IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator, IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        Objects.requireNonNull(mutableIntToObjectMapAllocator);
        Objects.requireNonNull(mutableIntSetAllocator);

        if (droppedObjectIds != null) {

            mutableIntSetAllocator.freeMutable(droppedObjectIds);

            this.droppedObjectIds = null;
        }

        if (droppedColumnsByColumnObjectId != null) {

            droppedColumnsByColumnObjectId.forEachValue(mutableIntSetAllocator, (s, a) -> {

                s.clear();

                a.freeMutable(s);
            });

            droppedColumnsByColumnObjectId.clear();

            mutableIntToObjectMapAllocator.freeMutableInstance(droppedColumnsByColumnObjectId);
        }
    }
}
