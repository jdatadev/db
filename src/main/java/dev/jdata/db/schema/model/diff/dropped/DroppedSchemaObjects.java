package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.DroppedElements.IDroppedElementsAllocator;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public final class DroppedSchemaObjects extends ObjectCacheNode implements IDroppedSchemaObjects {

    public interface IDroppedSchemaObjectsAllocator extends IDroppedElementsAllocator, IIntToObjectMapAllocator<MutableIntBucketSet>, IIntSetAllocator, IAllocators {

    }

    private final DroppedElements[] droppedElementsArray;

    public DroppedSchemaObjects() {

        this.droppedElementsArray = new DroppedElements[DDLObjectType.getNumObjectTypes()];
    }

    public void add(IDroppedSchemaObjects other, IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {

        final IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator = droppedSchemaObjectsAllocator;
        final IIntSetAllocator intSetAllocator = droppedSchemaObjectsAllocator;

        final DroppedSchemaObjects otherDroppedSchemaObjects = (DroppedSchemaObjects)other;

        final int numObjectTypes = DDLObjectType.getNumObjectTypes();

        final DroppedElements[] thisDroppedElementsArray = droppedElementsArray;
        final DroppedElements[] otherDroppedElementsArray = otherDroppedSchemaObjects.droppedElementsArray;

        for (int i = 0; i < numObjectTypes; ++ i) {

            final DroppedElements otherDroppedElements = otherDroppedElementsArray[i];

            if (otherDroppedElements != null) {

                DroppedElements thisDroppedElements = thisDroppedElementsArray[i];

                if (thisDroppedElements == null) {

                    thisDroppedElements = droppedElementsArray[i] = droppedSchemaObjectsAllocator.allocateDroppedElements();
                }

                thisDroppedElements.add(otherDroppedElements, intToObjectMapAllocator, intSetAllocator);
            }
        }
    }

    public void initialize(DroppedSchemaObjects other, IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(droppedSchemaObjectsAllocator);

        final IDroppedElementsAllocator droppedElementsAllocator = droppedSchemaObjectsAllocator;
        final IIntToObjectMapAllocator<MutableIntBucketSet> intToObjectMapAllocator = droppedSchemaObjectsAllocator;
        final IIntSetAllocator intSetAllocator = droppedSchemaObjectsAllocator;

        final int numDDLObjectTypes = DDLObjectType.getNumObjectTypes();

        final DroppedElements[] otherDroppedElementsArray = other.droppedElementsArray;

        for (int i = 0; i < numDDLObjectTypes; ++ i) {

            final DroppedElements otherDroppedElements = otherDroppedElementsArray[i];

            if (otherDroppedElements != null) {

                DroppedElements thisDroppedElements = droppedElementsArray[i];

                if (thisDroppedElements == null) {

                    thisDroppedElements = this.droppedElementsArray[i] = droppedElementsAllocator.allocateDroppedElements();
                }

                thisDroppedElements.initialize(otherDroppedElements, intToObjectMapAllocator, intSetAllocator);
            }
            else {
                final DroppedElements thisDroppedElements = droppedElementsArray[i];

                if (thisDroppedElements != null) {

                    thisDroppedElements.free(intToObjectMapAllocator, intSetAllocator);

                    this.droppedElementsArray[i] = null;
                }
            }
        }
    }

    public void addDroppedSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject, IDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator) {

        checkParameters(ddlObjectType, schemaObject);
        Objects.requireNonNull(droppedSchemaObjectsAllocator);

        final IDroppedElementsAllocator droppedElementsAllocator = droppedSchemaObjectsAllocator;
        final IIntSetAllocator intSetAllocator = droppedSchemaObjectsAllocator;

        addDroppedSchemaObject(ddlObjectType, schemaObject, droppedElementsAllocator, intSetAllocator);
    }

    void addDroppedSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject, IDroppedElementsAllocator droppedElementsAllocator,
            IIntSetAllocator intSetAllocator) {

        checkParameters(ddlObjectType, schemaObject);
        Objects.requireNonNull(droppedElementsAllocator);
        Objects.requireNonNull(intSetAllocator);

        final int index = ddlObjectType.ordinal();

        DroppedElements droppedElements = droppedElementsArray[index];

        if (droppedElements == null) {

            droppedElements = this.droppedElementsArray[index] = droppedElementsAllocator.allocateDroppedElements();
        }

        droppedElements.addDroppedSchemaObject(schemaObject, intSetAllocator);
    }

    @Override
    public boolean isDroppedObject(DDLObjectType ddlObjectType, SchemaObject schemaObject) {

        checkParameters(ddlObjectType, schemaObject);

        final DroppedElements droppedElements = droppedElementsArray[ddlObjectType.ordinal()];

        return droppedElements != null ? droppedElements.isDroppedObject(schemaObject) : false;
    }

    public void addDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column, IDroppedElementsAllocator droppedElementsAllocator,
            IIntSetAllocator intSetAllocator) {

        checkParameters(ddlObjectType, columnsObject);
        Objects.requireNonNull(column);
        Objects.requireNonNull(droppedElementsAllocator);
        Objects.requireNonNull(intSetAllocator);

        final int index = ddlObjectType.ordinal();

        DroppedElements droppedElements = droppedElementsArray[index];

        if (droppedElements == null) {

            droppedElements = this.droppedElementsArray[index] = droppedElementsAllocator.allocateDroppedElements();
        }

        droppedElements.addDroppedColumn(index, index, intSetAllocator);
    }

    @Override
    public boolean isDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column) {

        checkParameters(ddlObjectType, columnsObject);
        Objects.requireNonNull(column);

        final DroppedElements droppedElements = droppedElementsArray[ddlObjectType.ordinal()];

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
