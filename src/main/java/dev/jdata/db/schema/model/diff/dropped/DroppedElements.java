package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectBaseStaticMap;
import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;
import dev.jdata.db.utils.adt.sets.IIntSetView;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public final class DroppedElements extends ObjectCacheNode implements IDroppedElements {

work object

    private static final int DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT = 0;
    private static final int DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT = 0;

    private IMutableIntSet droppedObjects;
    private MutableIntToObjectWithRemoveNonBucketMap<IMutableIntSet> droppedColumns;

    private IMutableIntSetAllocator scratchIntSetAllocator;
    private int sratchColumnsObjectId;

    @Override
    public final boolean isDroppedObject(SchemaObject schemaObject) {

        Objects.requireNonNull(schemaObject);

        return droppedObjects != null ? droppedObjects.contains(schemaObject.getId()) : false;
    }

    @Override
    public final boolean isDroppedColumn(SchemaObject schemaObject, Column column) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(column);

        final IIntSetView droppedColumnsSet = droppedColumns.get(schemaObject.getId());

        return droppedColumnsSet != null ? droppedColumnsSet.contains(column.getId()) : false;
    }

    final void add(DroppedElements other, IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator, IMutableIntSetAllocator intSetAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(intToObjectMapAllocator);
        Objects.requireNonNull(intSetAllocator);

        final IMutableIntSet otherDroppedObjects = other.droppedObjects;

        if (otherDroppedObjects != null) {

            if (droppedObjects == null) {

                this.droppedObjects = intSetAllocator.allocateMutableIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
            }

            this.droppedObjects.addUnordered(otherDroppedObjects);
        }

        final MutableIntToObjectWithRemoveNonBucketMap<IMutableIntSet> otherDroppedColumns = other.droppedColumns;

        if (otherDroppedColumns != null) {

            if (droppedColumns == null) {

                this.droppedColumns = intToObjectMapAllocator.allocateIntToObjectMap(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
            }

            addDroppedColumns(otherDroppedColumns, intToObjectMapAllocator, intSetAllocator);
        }
    }

    final void initialize(DroppedElements other, IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator, IMutableIntSetAllocator intSetAllocator) {

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

    final void addDroppedSchemaObject(SchemaObject schemaObject, IMutableIntSetAllocator intSetAllocator) {

        Objects.requireNonNull(schemaObject);
        Objects.requireNonNull(intSetAllocator);

        addDroppedSchemaObject(schemaObject.getId(), intSetAllocator);
    }

    private void addDroppedSchemaObject(int schemaObjectId, IMutableIntSetAllocator intSetAllocator) {

        IMutableIntSet dropped = droppedObjects;

        if (dropped == null) {

            dropped = this.droppedObjects = intSetAllocator.allocateMutableIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
        }

        dropped.addUnordered(schemaObjectId);
    }

    private void initializeDroppedSchemaObjects(IIntSetView schemaObjects, IMutableIntSetAllocator intSetAllocator) {

        if (droppedObjects != null) {

            droppedObjects.clear();
        }
        else {
            this.droppedObjects = intSetAllocator.allocateMutableIntSet(DROPPED_OBJECTS_INITIAL_CAPACITY_EXPONENT);
        }

        droppedObjects.addUnordered(schemaObjects);
    }

    private void initializeDroppedColumns(MutableIntToObjectWithRemoveNonBucketMap<IMutableIntSet> droppedColumnsToAdd,
            IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator, IMutableIntSetAllocator intSetAllocator) {

        if (droppedColumns != null) {

            droppedColumns.forEachValue(null, (s, p) -> s.clear());
        }
        else {
            this.droppedColumns = intToObjectMapAllocator.allocateIntToObjectMap(DROPPED_COLUMNS_MAP_INITIAL_CAPACITY_EXPONENT);
        }

        addDroppedColumns(droppedColumnsToAdd, intToObjectMapAllocator, intSetAllocator);
    }

    private void addDroppedColumns(IMutableIntToObjectBaseStaticMap<IMutableIntSet> droppedColumnsToAdd, IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator,
            IMutableIntSetAllocator intSetAllocator) {

        this.scratchIntSetAllocator = intSetAllocator;

        droppedColumnsToAdd.forEachKeyAndValue(this, (coId, set, instance) -> {

            this.sratchColumnsObjectId = coId;

            set.forEach(instance, (c, i) -> i.addDroppedColumn(i.sratchColumnsObjectId, c, i.scratchIntSetAllocator));
        });

        this.scratchIntSetAllocator = null;
    }

    final void addDroppedColumn(int columnsObjectId, int columnId, IMutableIntSetAllocator intSetAllocator) {

        Checks.isSchemaObjectId(columnsObjectId);
        Checks.isColumnId(columnId);
        Objects.requireNonNull(intSetAllocator);

        IMutableIntSet droppedColumnsSet = droppedColumns.get(columnsObjectId);

        if (droppedColumnsSet == null) {

            droppedColumnsSet = intSetAllocator.allocateMutableIntSet(DROPPED_COLUMNS_INTSET_INITIAL_CAPACITY_EXPONENT);

            droppedColumns.put(columnsObjectId, droppedColumnsSet);
        }
        else {
            if (droppedColumnsSet.contains(columnId)) {

                throw new IllegalStateException();
            }
        }

        droppedColumnsSet.addUnordered(columnId);
    }

    final void free(IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator, IMutableIntSetAllocator intSetAllocator) {

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
