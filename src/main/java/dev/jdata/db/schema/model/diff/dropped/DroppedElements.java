package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectStaticMap;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public final class DroppedElements<T extends IMutableIntSet> extends ObjectCacheNode implements IDroppedElements {

    private static final int DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT = 0;

    private T droppedObjects;
    private MutableIntToObjectWithRemoveNonBucketMap<T> droppedColumns;

    private IMutableIntSetAllocator<T> scratchIntSetAllocator;
    private int sratchColumnsObjectId;

    @Override
    public boolean isDroppedObject(SchemaObject schemaObject) {

        Objects.requireNonNull(schemaObject);

        return droppedObjects != null ? droppedObjects.contains(schemaObject.getId()) : false;
    }

    @Override
    public boolean isDroppedColumn(SchemaObject schemaObject, Column column) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(column);

        final T droppedColumnsSet = droppedColumns.get(schemaObject.getId());

        return droppedColumnsSet != null ? droppedColumnsSet.contains(column.getId()) : false;
    }

    void add(DroppedElements<T> other, IIntToObjectMapAllocator<T> intToObjectMapAllocator,
            IMutableIntSetAllocator<T> intSetAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(intToObjectMapAllocator);
        Objects.requireNonNull(intSetAllocator);

        final T otherDroppedObjects = other.droppedObjects;

        if (otherDroppedObjects != null) {

            if (droppedObjects == null) {

                this.droppedObjects = intSetAllocator.allocateMutableIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
            }

            this.droppedObjects.addAll(otherDroppedObjects);
        }

        final MutableIntToObjectWithRemoveNonBucketMap<T> otherDroppedColumns = other.droppedColumns;

        if (otherDroppedColumns != null) {

            if (droppedColumns == null) {

                this.droppedColumns = intToObjectMapAllocator.allocateIntToObjectMap(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
            }

            addDroppedColumns(otherDroppedColumns, intToObjectMapAllocator, intSetAllocator);
        }
    }

    void initialize(DroppedElements<T> other, IIntToObjectMapAllocator<T> intToObjectMapAllocator, IMutableIntSetAllocator<T> intSetAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(intToObjectMapAllocator);
        Objects.requireNonNull(intSetAllocator);

        initializeDroppedSchemaObjects(other.droppedObjects, intSetAllocator);

        if (other.droppedColumns != null) {

            initializeDroppedColumns(droppedColumns, intToObjectMapAllocator, intSetAllocator);
        }
        else {
            if (droppedColumns != null) {

                droppedColumns.forEachValue(null, (s, p) -> s.clear());
            }
        }
    }

    void addDroppedSchemaObject(SchemaObject schemaObject, IMutableIntSetAllocator<T> intSetAllocator) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(intSetAllocator);

        addDroppedSchemaObject(schemaObject.getId(), intSetAllocator);
    }

    private void addDroppedSchemaObject(int schemaObjectId, IMutableIntSetAllocator<T> intSetAllocator) {

        T dropped = droppedObjects;

        if (dropped == null) {

            dropped = this.droppedObjects = intSetAllocator.allocateMutableIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
        }

        dropped.add(schemaObjectId);
    }

    private void initializeDroppedSchemaObjects(T schemaObjects, IMutableIntSetAllocator<T> intSetAllocator) {

        if (droppedObjects != null) {

            droppedObjects.clear();
        }
        else {
            this.droppedObjects = intSetAllocator.allocateMutableIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
        }

        droppedObjects.addAll(schemaObjects);
    }

    private void initializeDroppedColumns(MutableIntToObjectWithRemoveNonBucketMap<T> droppedColumnsToAdd, IIntToObjectMapAllocator<T> intToObjectMapAllocator,
            IMutableIntSetAllocator<T> intSetAllocator) {

        if (droppedColumns != null) {

            droppedColumns.forEachValue(null, (s, p) -> s.clear());
        }
        else {
            this.droppedColumns = intToObjectMapAllocator.allocateIntToObjectMap(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
        }

        addDroppedColumns(droppedColumnsToAdd, intToObjectMapAllocator, intSetAllocator);
    }

    private void addDroppedColumns(IMutableIntToObjectStaticMap<T> droppedColumnsToAdd, IIntToObjectMapAllocator<T> intToObjectMapAllocator,
            IMutableIntSetAllocator<T> intSetAllocator) {

        this.scratchIntSetAllocator = intSetAllocator;

        droppedColumnsToAdd.forEachKeyAndValue(this, (coId, set, instance) -> {

            this.sratchColumnsObjectId = coId;

            set.forEach(instance, (c, i) -> i.addDroppedColumn(i.sratchColumnsObjectId, c, i.scratchIntSetAllocator));
        });

        this.scratchIntSetAllocator = null;
    }

    void addDroppedColumn(int columnsObjectId, int columnId, IMutableIntSetAllocator<T> intSetAllocator) {

        Checks.isSchemaObjectId(columnsObjectId);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(intSetAllocator);

        T droppedColumnsSet = droppedColumns.get(columnsObjectId);

        if (droppedColumnsSet == null) {

            droppedColumnsSet = intSetAllocator.allocateMutableIntSet(DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT);

            droppedColumns.put(columnsObjectId, droppedColumnsSet);
        }
        else {
            if (droppedColumnsSet.contains(columnId)) {

                throw new IllegalStateException();
            }
        }

        droppedColumnsSet.add(columnId);
    }

    void free(IIntToObjectMapAllocator<T> intToObjectMapAllocator, IMutableIntSetAllocator<T> intSetAllocator) {

        Objects.requireNonNull(intToObjectMapAllocator);
        Objects.requireNonNull(intSetAllocator);

        if (droppedObjects != null) {

            intSetAllocator.freeMutableIntSet(droppedObjects);

            this.droppedObjects = null;
        }

        if (droppedColumns != null) {

            droppedColumns.forEachValue(intSetAllocator, (s, a) -> {

                s.clear();

                a.freeMutableIntSet(s);
            });

            droppedColumns.clear();

            intToObjectMapAllocator.freeIntToObjectMap(droppedColumns);
        }
    }
}
