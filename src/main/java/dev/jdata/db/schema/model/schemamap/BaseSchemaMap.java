package dev.jdata.db.schema.model.schemamap;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DBNamedIdentifiableObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseSchemaMap<T extends ISchemaObjects<?>> extends ObjectCacheNode implements ISchemaMap, IResettable {

    private final Function<ISchemaObjects<?>, T> createObjectState;
    private final Function<T, ISchemaObjects<?>> getSchemaObjects;

    private final T[] schemaObjectStates;

    protected BaseSchemaMap(AllocationType allocationType, Function<ISchemaObjects<?>, T> createObjectState, IntFunction<T[]> createArray,
            Function<T, ISchemaObjects<?>> getSchemaObjects, ISchemaObjects<Table> tables, ISchemaObjects<View> views, ISchemaObjects<Index> indices,
            ISchemaObjects<Trigger> triggers, ISchemaObjects<DBFunction> functions, ISchemaObjects<Procedure> procedures) {
        this(allocationType, createObjectState, createArray, getSchemaObjects);

        initialize(createObjectState, tables, views, indices, triggers, functions, procedures);
    }

    private BaseSchemaMap(AllocationType allocationType, Function<ISchemaObjects<?>, T> createObjectState, IntFunction<T[]> createArray,
            Function<T, ISchemaObjects<?>> getSchemaObjects) {
        super(allocationType);

        Objects.requireNonNull(createObjectState);
        Objects.requireNonNull(createArray);
        Objects.requireNonNull(getSchemaObjects);

        this.createObjectState = createObjectState;
        this.getSchemaObjects = getSchemaObjects;

        this.schemaObjectStates = createArray.apply(DDLObjectType.getNumObjectTypes());
    }

    protected final void initialize(ISchemaObjects<Table> tables, ISchemaObjects<View> views, ISchemaObjects<Index> indices, ISchemaObjects<Trigger> triggers,
            ISchemaObjects<DBFunction> functions, ISchemaObjects<Procedure> procedures) {

        initialize(createObjectState, tables, views, indices, triggers, functions, procedures);
    }

    private void initialize(Function<ISchemaObjects<?>, T> createObjectState, ISchemaObjects<Table> tables, ISchemaObjects<View> views, ISchemaObjects<Index> indices,
            ISchemaObjects<Trigger> triggers, ISchemaObjects<DBFunction> functions, ISchemaObjects<Procedure> procedures) {

        setSchemaObjectsOrNull(DDLObjectType.TABLE, tables);
        setSchemaObjectsOrNull(DDLObjectType.VIEW, views);
        setSchemaObjectsOrNull(DDLObjectType.INDEX, indices);
        setSchemaObjectsOrNull(DDLObjectType.TRIGGER, triggers);
        setSchemaObjectsOrNull(DDLObjectType.FUNCTION, functions);
        setSchemaObjectsOrNull(DDLObjectType.PROCEDURE, procedures);
    }

    private void setSchemaObjectsOrNull(DDLObjectType ddlObjectType, ISchemaObjects<?> schemaObjects) {

        Objects.requireNonNull(ddlObjectType);

        if (schemaObjects != null) {

            setSchemaObjects(ddlObjectType, schemaObjects);
        }
        else {
            final int index = ddlObjectType.ordinal();

            schemaObjectStates[index] = Initializable.checkNotYetInitializedToNull(schemaObjectStates[index]);
        }
    }

    private void setSchemaObjects(DDLObjectType ddlObjectType, ISchemaObjects<?> schemaObjects) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObjects);

        final int index = ddlObjectType.ordinal();

        schemaObjectStates[index] = Initializable.checkNotYetInitialized(schemaObjectStates[index], createObjectState.apply(schemaObjects));
    }

    @Override
    public final void reset() {

        Arrays.fill(schemaObjectStates, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <U extends SchemaObject> IIndexList<U> getSchemaObjectsList(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (IIndexList<U>)getSchemaObjectsForObjectType(ddlObjectType).getSchemaObjectsList();
    }

    @Override
    public final boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName) {

        Objects.requireNonNull(ddlObjectType);
        StringRef.checkIsString(hashObjectName);

        return getSchemaObjects(ddlObjectType).containsSchemaObjectName(hashObjectName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R extends SchemaObject> R getSchemaObject(DDLObjectType ddlObjectType, int objectId) {

        Objects.requireNonNull(ddlObjectType);
        Checks.isSchemaObjectId(objectId);

        return (R)getSchemaObjects(ddlObjectType).getSchemaObjectById(objectId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R extends SchemaObject> ISchemaObjects<R> getSchemaObjects(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (ISchemaObjects<R>)getSchemaObjectsForObjectType(ddlObjectType);
    }

    @Override
    public final int computeMaxId(DDLObjectType ddlObjectType, int defaultValue) {

        Objects.requireNonNull(ddlObjectType);

        return getSchemaObjects(ddlObjectType).maxInt(defaultValue, DBNamedIdentifiableObject::getId);
    }

    @SuppressWarnings("unchecked") T getSchemaObjectsForObjectType(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (T)getSchemaObjects.apply(getSchemaObjectState(ddlObjectType));
    }

    private T getSchemaObjectState(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaObjectStates[ddlObjectType.ordinal()];
    }

    public final boolean isEqualTo(StringResolver thisStringResolver, BaseSchemaMap<T> other, StringResolver otherStringResolver) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        if (this == other) {

            result = true;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {

            result = isEqualTo(schemaObjectStates, thisStringResolver, other.schemaObjectStates, otherStringResolver);
        }

        return result;
    }

    private static <T extends ISchemaObjects<?>> boolean isEqualTo(T[] thisSchemaObjectStates, StringResolver thisStringResolver, T[] otherSchemaObjectStates,
            StringResolver otherStringResolver) {

        boolean result;

        final int num = thisSchemaObjectStates.length;

        if (num != otherSchemaObjectStates.length) {

            result = false;
        }
        else {
            result = true;

            for (int i = 0; i < num; ++ i) {

                @SuppressWarnings("unchecked")
                final ISchemaObjects<SchemaObject> thisSchemaObjects = (ISchemaObjects<SchemaObject>)thisSchemaObjectStates[i];
                @SuppressWarnings("unchecked")
                final ISchemaObjects<SchemaObject> otherSchemaObjects = (ISchemaObjects<SchemaObject>)otherSchemaObjectStates[i];

                if (!thisSchemaObjects.isEqualTo(thisStringResolver, otherSchemaObjects, otherStringResolver)) {

                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public final boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final BaseSchemaMap<?> other = (BaseSchemaMap<?>)object;

            result = Arrays.equals(schemaObjectStates, other.schemaObjectStates);
        }

        return result;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [createObjectState=" + createObjectState + ", getSchemaObjects=" + getSchemaObjects
                + ", schemaObjectStates=" + Arrays.toString(schemaObjectStates) + "]";
    }
}
