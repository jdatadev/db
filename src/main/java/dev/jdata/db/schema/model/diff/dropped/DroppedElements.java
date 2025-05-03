package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.allocators.IIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

final class DroppedElements extends ObjectCacheNode implements IDroppedElements {

    private static final int DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT = 0;

    interface IDroppedElementsAllocator {

        DroppedElements allocateDroppedElements();

        void freeDroppedElements(DroppedElements droppedElements);
    }

    private MutableIntBucketSet droppedObjects;
    private MutableIntToObjectWithRemoveNonBucketMap<MutableIntBucketSet> droppedColumns;

    private IIntSetAllocator scratchIntSetAllocator;
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

        final MutableIntBucketSet droppedColumnsSet = droppedColumns.get(schemaObject.getId());

        return droppedColumnsSet != null ? droppedColumnsSet.contains(column.getId()) : false;
    }

    void add(DroppedElements other, IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator, IIntSetAllocator intSetAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(intToObjectMapAllocator);
        Objects.requireNonNull(intSetAllocator);

        final MutableIntBucketSet otherDroppedObjects = other.droppedObjects;

        if (otherDroppedObjects != null) {

            if (droppedObjects == null) {

                this.droppedObjects = intSetAllocator.allocateIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
            }

            this.droppedObjects.addAll(otherDroppedObjects);
        }

        final MutableIntToObjectWithRemoveNonBucketMap<MutableIntBucketSet> otherDroppedColumns = other.droppedColumns;

        if (otherDroppedColumns != null) {

            if (droppedColumns == null) {

                this.droppedColumns = intToObjectMapAllocator.allocateIntToObjectMap(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
            }

            addDroppedColumns(otherDroppedColumns, intToObjectMapAllocator, intSetAllocator);
        }
    }

    void initialize(DroppedElements other, IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator, IIntSetAllocator intSetAllocator) {

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

    void addDroppedSchemaObject(SchemaObject schemaObject, IIntSetAllocator intSetAllocator) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(intSetAllocator);

        addDroppedSchemaObject(schemaObject.getId(), intSetAllocator);
    }

    private void addDroppedSchemaObject(int schemaObjectId, IIntSetAllocator intSetAllocator) {

        MutableIntBucketSet dropped = droppedObjects;

        if (dropped == null) {

            dropped = this.droppedObjects = intSetAllocator.allocateIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
        }

        dropped.add(schemaObjectId);
    }

    private void initializeDroppedSchemaObjects(MutableIntBucketSet schemaObjects, IIntSetAllocator intSetAllocator) {

        if (droppedObjects != null) {

            droppedObjects.clear();
        }
        else {
            this.droppedObjects = intSetAllocator.allocateIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
        }

        droppedObjects.addAll(schemaObjects);
    }

    private void initializeDroppedColumns(MutableIntToObjectWithRemoveNonBucketMap<MutableIntBucketSet> droppedColumnsToAdd,
            IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator, IIntSetAllocator intSetAllocator) {

        if (droppedColumns != null) {

            droppedColumns.forEachValue(null, (s, p) -> s.clear());
        }
        else {
            this.droppedColumns = intToObjectMapAllocator.allocateIntToObjectMap(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
        }

        addDroppedColumns(droppedColumnsToAdd, intToObjectMapAllocator, intSetAllocator);
    }

    private void addDroppedColumns(IMutableIntToObjectWithRemoveNonBucketMap<MutableIntBucketSet> droppedColumnsToAdd,
            IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator, IIntSetAllocator intSetAllocator) {

        this.scratchIntSetAllocator = intSetAllocator;

        droppedColumnsToAdd.forEachKeyAndValue(this, (coId, set, instance) -> {

            this.sratchColumnsObjectId = coId;

            set.forEach(instance, (c, i) -> i.addDroppedColumn(i.sratchColumnsObjectId, c, i.scratchIntSetAllocator));
        });

        this.scratchIntSetAllocator = null;
    }

    void addDroppedColumn(int columnsObjectId, int columnId, IIntSetAllocator intSetAllocator) {

        Checks.isSchemaObjectId(columnsObjectId);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(intSetAllocator);

        MutableIntBucketSet droppedColumnsSet = droppedColumns.get(columnsObjectId);

        if (droppedColumnsSet == null) {

            droppedColumnsSet = intSetAllocator.allocateIntSet(DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT);

            droppedColumns.put(columnsObjectId, droppedColumnsSet);
        }
        else {
            if (droppedColumnsSet.contains(columnId)) {

                throw new IllegalStateException();
            }
        }

        droppedColumnsSet.add(columnId);
    }

    void free(IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator, IIntSetAllocator intSetAllocator) {

        Objects.requireNonNull(intToObjectMapAllocator);
        Objects.requireNonNull(intSetAllocator);

        if (droppedObjects != null) {

            intSetAllocator.freeIntSet(droppedObjects);

            this.droppedObjects = null;
        }

        if (droppedColumns != null) {

            droppedColumns.forEachValue(intSetAllocator, (s, a) -> {

                s.clear();

                a.freeIntSet(s);
            });

            droppedColumns.clear();

            intToObjectMapAllocator.freeIntToObjectMap(droppedColumns);
        }
    }
}
