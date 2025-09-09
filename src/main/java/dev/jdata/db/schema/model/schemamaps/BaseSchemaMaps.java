package dev.jdata.db.schema.model.schemamaps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.schema.model.ISchemaMap;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DBNamedIdentifiableObject;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.utils.Initializable;
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseSchemaMaps<T extends ISchemaMap<? extends SchemaObject>> extends ObjectCacheNode implements ISchemaMaps, IResettable {

    private final Function<SchemaMap<?, ?, ?>, T> createObjectState;
    private final Function<T, SchemaMap<?, ?, ?>> getSchemaMap;

    private final T[] schemaObjectStates;

    protected BaseSchemaMaps(Function<SchemaMap<?, ?, ?>, T> createObjectState, IntFunction<T[]> createArray, Function<T, SchemaMap<?, ?, ?>> getSchemaMap,
            SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices, SchemaMap<Trigger, ?, ?> triggers,
            SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {
        this(createObjectState, createArray, getSchemaMap);

        initialize(createObjectState, tables, views, indices, triggers, functions, procedures);
    }

    BaseSchemaMaps(Function<SchemaMap<?, ?, ?>, T> createObjectState, IntFunction<T[]> createArray, Function<T, SchemaMap<?, ?, ?>> getSchemaMap) {

        Objects.requireNonNull(createObjectState);
        Objects.requireNonNull(createArray);
        Objects.requireNonNull(getSchemaMap);

        this.createObjectState = createObjectState;
        this.getSchemaMap = getSchemaMap;

        this.schemaObjectStates = createArray.apply(DDLObjectType.getNumObjectTypes());
    }

    protected final void initialize(SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices, SchemaMap<Trigger, ?, ?> triggers,
            SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {

        initialize(createObjectState, tables, views, indices, triggers, functions, procedures);
    }

    private void initialize(Function<SchemaMap<?, ?, ?>, T> createObjectState, SchemaMap<Table, ?, ?> tables, SchemaMap<View, ?, ?> views, SchemaMap<Index, ?, ?> indices,
            SchemaMap<Trigger, ?, ?> triggers, SchemaMap<DBFunction, ?, ?> functions, SchemaMap<Procedure, ?, ?> procedures) {

        setSchemaMap(DDLObjectType.TABLE, tables);
        setSchemaMap(DDLObjectType.VIEW, views);
        setSchemaMap(DDLObjectType.INDEX, indices);
        setSchemaMap(DDLObjectType.TRIGGER, triggers);
        setSchemaMap(DDLObjectType.FUNCTION, functions);
        setSchemaMap(DDLObjectType.PROCEDURE, procedures);
    }

    private void setSchemaMap(DDLObjectType ddlObjectType, SchemaMap<?, ?, ?> schemaMap) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaMap);

        final int index = ddlObjectType.ordinal();

        schemaObjectStates[index] = Initializable.checkNotYetInitialized(schemaObjectStates[index], createObjectState.apply(schemaMap));
    }

    @Override
    public final void reset() {

        Arrays.fill(schemaObjectStates, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <U extends SchemaObject> IIndexList<U> getSchemaObjects(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (IIndexList<U>)getSchemaMapForObjectType(ddlObjectType).getSchemaObjects();
    }

    public final boolean containsSchemaObjectName(DDLObjectType ddlObjectType, long hashObjectName) {

        Objects.requireNonNull(ddlObjectType);
        StringRef.checkIsString(hashObjectName);

        return getSchemaMap(ddlObjectType).containsSchemaObjectName(hashObjectName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R extends SchemaObject> R getSchemaObject(DDLObjectType ddlObjectType, int objectId) {

        Objects.requireNonNull(ddlObjectType);
        Checks.isSchemaObjectId(objectId);

        return (R)getSchemaMap(ddlObjectType).getSchemaObjectById(objectId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <R extends SchemaObject> ISchemaMap<R> getSchemaMap(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (ISchemaMap<R>)getSchemaMapForObjectType(ddlObjectType);
    }

    public final int computeMaxId(DDLObjectType ddlObjectType, int defaultValue) {

        Objects.requireNonNull(ddlObjectType);

        return getSchemaMap(ddlObjectType).maxInt(defaultValue, DBNamedIdentifiableObject::getId);
    }

    @SuppressWarnings("unchecked") T getSchemaMapForObjectType(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return (T)getSchemaMap.apply(getSchemaObjectState(ddlObjectType));
    }

    private T getSchemaObjectState(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return schemaObjectStates[ddlObjectType.ordinal()];
    }

    public final boolean isEqualTo(StringResolver thisStringResolver, BaseSchemaMaps<T> other, StringResolver otherStringResolver) {

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

    private static <T extends ISchemaMap<?>> boolean isEqualTo(T[] thisSchemaObjectStates, StringResolver thisStringResolver, T[] otherSchemaObjectStates,
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
                final ISchemaMap<SchemaObject> thisSchemaMap = (ISchemaMap<SchemaObject>)thisSchemaObjectStates[i];
                @SuppressWarnings("unchecked")
                final ISchemaMap<SchemaObject> otherSchemaMap = (ISchemaMap<SchemaObject>)otherSchemaObjectStates[i];

                if (!thisSchemaMap.isEqualTo(thisStringResolver, otherSchemaMap, otherStringResolver)) {

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
            final BaseSchemaMaps<?> other = (BaseSchemaMaps<?>)object;

            result = Arrays.equals(schemaObjectStates, other.schemaObjectStates);
        }

        return result;
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [createObjectState=" + createObjectState + ", getSchemaMap=" + getSchemaMap
                + ", schemaObjectStates=" + Arrays.toString(schemaObjectStates) + "]";
    }
}
