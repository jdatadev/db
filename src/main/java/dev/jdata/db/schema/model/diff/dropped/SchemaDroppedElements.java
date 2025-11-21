package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public abstract class SchemaDroppedElements<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends ObjectCacheNode
        implements ISchemaDroppedElements {

    private final DroppedElements<T, U>[] droppedElementsArray;

    SchemaDroppedElements(AllocationType allocationType) {
        super(allocationType);

        @SuppressWarnings("unchecked")
        final DroppedElements<T, U>[] droppedElementsArray = new DroppedElements[DDLObjectType.getNumObjectTypes()];

        this.droppedElementsArray = droppedElementsArray;
    }

    public final void initialize(SchemaDroppedElements<T, U> other, SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(schemaDroppedElementsAllocators);

        final IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator = schemaDroppedElementsAllocators.getMutableIntToObjectMapAllocator();
        final IMutableIntSetAllocator<T> mutableIntSetAllocator = schemaDroppedElementsAllocators.getMutableIntSetAllocator();

        final int numDDLObjectTypes = DDLObjectType.getNumObjectTypes();

        final DroppedElements<T, U>[] otherDroppedElementsArray = other.droppedElementsArray;

        for (int i = 0; i < numDDLObjectTypes; ++ i) {

            final DroppedElements<T, U> otherDroppedElements = otherDroppedElementsArray[i];

            if (otherDroppedElements != null) {

                DroppedElements<T, U> thisDroppedElements = droppedElementsArray[i];

                if (thisDroppedElements == null) {

                    thisDroppedElements = this.droppedElementsArray[i] = schemaDroppedElementsAllocators.allocateDroppedElements();
                }

                thisDroppedElements.initialize(otherDroppedElements, mutableIntToObjectMapAllocator, mutableIntSetAllocator);
            }
            else {
                final DroppedElements<T, U> thisDroppedElements = droppedElementsArray[i];

                if (thisDroppedElements != null) {

                    thisDroppedElements.free(mutableIntToObjectMapAllocator, mutableIntSetAllocator);

                    this.droppedElementsArray[i] = null;
                }
            }
        }
    }

    public final void add(ISchemaDroppedElements other, SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(schemaDroppedElementsAllocators);

        final IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> intToObjectMapAllocator = schemaDroppedElementsAllocators.getMutableIntToObjectMapAllocator();
        final IMutableIntSetAllocator<T> mutableIntSetAllocator = schemaDroppedElementsAllocators.getMutableIntSetAllocator();

        final SchemaDroppedElements<?, ?> otherDroppedSchemaObjects = (SchemaDroppedElements<?, ?>)other;

        final int numObjectTypes = DDLObjectType.getNumObjectTypes();

        final DroppedElements<T, U>[] thisDroppedElementsArray = droppedElementsArray;
        final DroppedElements<?, ?>[] otherDroppedElementsArray = otherDroppedSchemaObjects.droppedElementsArray;

        for (int i = 0; i < numObjectTypes; ++ i) {

            final DroppedElements<?, ?> otherDroppedElements = otherDroppedElementsArray[i];

            if (otherDroppedElements != null) {

                DroppedElements<T, U> thisDroppedElements = thisDroppedElementsArray[i];

                if (thisDroppedElements == null) {

                    thisDroppedElements = droppedElementsArray[i] = schemaDroppedElementsAllocators.allocateDroppedElements();
                }

                thisDroppedElements.add(otherDroppedElements, intToObjectMapAllocator, mutableIntSetAllocator);
            }
        }
    }

    public final void addDroppedSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject, SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators) {

        checkParameters(ddlObjectType, schemaObject);
        Objects.requireNonNull(schemaDroppedElementsAllocators);

        final IMutableIntSetAllocator<T> mutableIntSetAllocator = schemaDroppedElementsAllocators.getMutableIntSetAllocator();

        addDroppedSchemaObject(ddlObjectType, schemaObject, schemaDroppedElementsAllocators, mutableIntSetAllocator);
    }

    private void addDroppedSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject, SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators,
            IMutableIntSetAllocator<T> mutableIntSetAllocator) {

        checkParameters(ddlObjectType, schemaObject);
        Objects.requireNonNull(schemaDroppedElementsAllocators);
        Objects.requireNonNull(mutableIntSetAllocator);

        final int index = ddlObjectType.ordinal();

        DroppedElements<T, U> droppedElements = droppedElementsArray[index];

        if (droppedElements == null) {

            droppedElements = this.droppedElementsArray[index] = schemaDroppedElementsAllocators.allocateDroppedElements();
        }

        droppedElements.addDroppedSchemaObject(schemaObject, mutableIntSetAllocator);
    }

    @Override
    public final boolean isDroppedObject(DDLObjectType ddlObjectType, SchemaObject schemaObject) {

        checkParameters(ddlObjectType, schemaObject);

        final DroppedElements<T, U> droppedElements = droppedElementsArray[ddlObjectType.ordinal()];

        return droppedElements != null ? droppedElements.isDroppedObject(schemaObject) : false;
    }

    public final void addDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column,
            SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators, IMutableIntSetAllocator<T> intSetAllocator) {

        checkParameters(ddlObjectType, columnsObject);
        Objects.requireNonNull(column);
        Objects.requireNonNull(schemaDroppedElementsAllocators);
        Objects.requireNonNull(intSetAllocator);

        final int index = ddlObjectType.ordinal();

        DroppedElements<T, U> droppedElements = droppedElementsArray[index];

        if (droppedElements == null) {

            droppedElements = this.droppedElementsArray[index] = schemaDroppedElementsAllocators.allocateDroppedElements();
        }

        droppedElements.addDroppedColumn(index, index, intSetAllocator);
    }

    @Override
    public final boolean isDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column) {

        checkParameters(ddlObjectType, columnsObject);
        Objects.requireNonNull(column);

        final DroppedElements<T, U> droppedElements = droppedElementsArray[ddlObjectType.ordinal()];

        return droppedElements != null ? droppedElements.isDroppedColumn(columnsObject, column) : false;
    }

    private static void checkParameters(DDLObjectType ddlObjectType, SchemaObject schemaObject) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObject);

        if (!ddlObjectType.getSchemaObjectType().equals(schemaObject.getClass())) {

            throw new IllegalArgumentException();
        }
    }
}
