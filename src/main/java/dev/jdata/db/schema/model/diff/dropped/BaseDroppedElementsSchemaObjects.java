package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.allocators.model.diff.dropped.BaseDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.diff.dropped.IDroppedElementsAllocator;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;
import dev.jdata.db.utils.allocators.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

abstract class BaseDroppedElementsSchemaObjects<T extends IMutableIntSet, U extends BaseDroppedElements<T>> extends ObjectCacheNode implements IDroppedElementsSchemaObjects {

    private final U[] droppedElementsArray;

    BaseDroppedElementsSchemaObjects(IntFunction<U[]> createDroppedElementsArray) {

        Objects.requireNonNull(createDroppedElementsArray);

        final U[] untypedDroppedElements = createDroppedElementsArray.apply(DDLObjectType.getNumObjectTypes());

        final U[] typedDroppedElements = untypedDroppedElements;

        this.droppedElementsArray = typedDroppedElements;
    }

    public final void initialize(BaseDroppedElementsSchemaObjects<T, U> other, BaseDroppedSchemaObjectsAllocator<T, U> droppedSchemaObjectsAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(droppedSchemaObjectsAllocator);

        final IDroppedElementsAllocator<T, U> droppedElementsAllocator = droppedSchemaObjectsAllocator;
        final IIntToObjectMapAllocator<T> intToObjectMapAllocator = droppedSchemaObjectsAllocator.getIntToObjectMapAllocator();
        final IMutableIntSetAllocator<T> intSetAllocator = droppedSchemaObjectsAllocator.getIntSetAllocator();

        final int numDDLObjectTypes = DDLObjectType.getNumObjectTypes();

        final U[] otherDroppedElementsArray = other.droppedElementsArray;

        for (int i = 0; i < numDDLObjectTypes; ++ i) {

            final BaseDroppedElements<T> otherDroppedElements = otherDroppedElementsArray[i];

            if (otherDroppedElements != null) {

                BaseDroppedElements<T> thisDroppedElements = droppedElementsArray[i];

                if (thisDroppedElements == null) {

                    thisDroppedElements = this.droppedElementsArray[i] = droppedElementsAllocator.allocateDroppedElements();
                }

                thisDroppedElements.initialize(otherDroppedElements, intToObjectMapAllocator, intSetAllocator);
            }
            else {
                final BaseDroppedElements<T> thisDroppedElements = droppedElementsArray[i];

                if (thisDroppedElements != null) {

                    thisDroppedElements.free(intToObjectMapAllocator, intSetAllocator);

                    this.droppedElementsArray[i] = null;
                }
            }
        }
    }

    public final void add(IDroppedElementsSchemaObjects other, BaseDroppedSchemaObjectsAllocator<T, U> droppedSchemaObjectsAllocator) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(droppedSchemaObjectsAllocator);

        final IIntToObjectMapAllocator<T> intToObjectMapAllocator = droppedSchemaObjectsAllocator.getIntToObjectMapAllocator();
        final IMutableIntSetAllocator<T> intSetAllocator = droppedSchemaObjectsAllocator.getIntSetAllocator();

        @SuppressWarnings("unchecked")
        final BaseDroppedElementsSchemaObjects<T, U> otherDroppedSchemaObjects = (BaseDroppedElementsSchemaObjects<T, U>)other;

        final int numObjectTypes = DDLObjectType.getNumObjectTypes();

        final BaseDroppedElements<T>[] thisDroppedElementsArray = droppedElementsArray;
        final BaseDroppedElements<T>[] otherDroppedElementsArray = otherDroppedSchemaObjects.droppedElementsArray;

        for (int i = 0; i < numObjectTypes; ++ i) {

            final BaseDroppedElements<T> otherDroppedElements = otherDroppedElementsArray[i];

            if (otherDroppedElements != null) {

                BaseDroppedElements<T> thisDroppedElements = thisDroppedElementsArray[i];

                if (thisDroppedElements == null) {

                    thisDroppedElements = droppedElementsArray[i] = droppedSchemaObjectsAllocator.allocateDroppedElements();
                }

                thisDroppedElements.add(otherDroppedElements, intToObjectMapAllocator, intSetAllocator);
            }
        }
    }

    public final void addDroppedSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject, BaseDroppedSchemaObjectsAllocator<T, U> droppedSchemaObjectsAllocator) {

        checkParameters(ddlObjectType, schemaObject);
        Objects.requireNonNull(droppedSchemaObjectsAllocator);

        final IDroppedElementsAllocator<T, U> droppedElementsAllocator = droppedSchemaObjectsAllocator;
        final IMutableIntSetAllocator<T> intSetAllocator = droppedSchemaObjectsAllocator.getIntSetAllocator();

        addDroppedSchemaObject(ddlObjectType, schemaObject, droppedElementsAllocator, intSetAllocator);
    }

    private void addDroppedSchemaObject(DDLObjectType ddlObjectType, SchemaObject schemaObject, IDroppedElementsAllocator<T, U> droppedElementsAllocator,
            IMutableIntSetAllocator<T> intSetAllocator) {

        checkParameters(ddlObjectType, schemaObject);
        Objects.requireNonNull(droppedElementsAllocator);
        Objects.requireNonNull(intSetAllocator);

        final int index = ddlObjectType.ordinal();

        BaseDroppedElements<T> droppedElements = droppedElementsArray[index];

        if (droppedElements == null) {

            droppedElements = this.droppedElementsArray[index] = droppedElementsAllocator.allocateDroppedElements();
        }

        droppedElements.addDroppedSchemaObject(schemaObject, intSetAllocator);
    }

    @Override
    public final boolean isDroppedObject(DDLObjectType ddlObjectType, SchemaObject schemaObject) {

        checkParameters(ddlObjectType, schemaObject);

        final BaseDroppedElements<?> droppedElements = droppedElementsArray[ddlObjectType.ordinal()];

        return droppedElements != null ? droppedElements.isDroppedObject(schemaObject) : false;
    }

    public final void addDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column, IDroppedElementsAllocator<T, U> droppedElementsAllocator,
            IMutableIntSetAllocator<T> intSetAllocator) {

        checkParameters(ddlObjectType, columnsObject);
        Objects.requireNonNull(column);
        Objects.requireNonNull(droppedElementsAllocator);
        Objects.requireNonNull(intSetAllocator);

        final int index = ddlObjectType.ordinal();

        BaseDroppedElements<T> droppedElements = droppedElementsArray[index];

        if (droppedElements == null) {

            droppedElements = this.droppedElementsArray[index] = droppedElementsAllocator.allocateDroppedElements();
        }

        droppedElements.addDroppedColumn(index, index, intSetAllocator);
    }

    @Override
    public final boolean isDroppedColumn(DDLObjectType ddlObjectType, ColumnsObject columnsObject, Column column) {

        checkParameters(ddlObjectType, columnsObject);
        Objects.requireNonNull(column);

        final BaseDroppedElements<?> droppedElements = droppedElementsArray[ddlObjectType.ordinal()];

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
