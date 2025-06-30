package dev.jdata.db.schema.model.schemamaps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBNamedObject;
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
import dev.jdata.db.utils.adt.IResettable;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseSchemaMaps<T extends ISchemaMap<? extends DBNamedObject>> extends ObjectCacheNode implements IResettable {

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_SCHEMA_MAP;

    public static abstract class BaseBuilder<T extends SchemaMap<?, ?, ?, ?, ?>, U extends BaseBuilder<T, U>> extends ObjectCacheNode {

        protected abstract SchemaMap<?, ?, ?, ?, ?> makeEmptySchema();

        private final T[] schemaMaps;

        protected BaseBuilder(IntFunction<T[]> createSchemaMapsArray) {

            Objects.requireNonNull(createSchemaMapsArray);

            final DDLObjectType[] ddlObjectTypes = DDLObjectType.values();

            final int numDDLObjectTypes = ddlObjectTypes.length;

            this.schemaMaps = createSchemaMapsArray.apply(numDDLObjectTypes);
        }

        public final U setTables(SchemaMap<Table, ?, ?, ?, ?> tables) {

            checkSchemaMap(DDLObjectType.TABLE, tables);

            return getThis();
        }

        public final U setViews(SchemaMap<View, ?, ?, ?, ?> views) {

            checkSchemaMap(DDLObjectType.VIEW, views);

            return getThis();
        }

        public final U setIndices(SchemaMap<Index, ?, ?, ?, ?> indices) {

            checkSchemaMap(DDLObjectType.INDEX, indices);

            return getThis();
        }

        public final U setTriggers(SchemaMap<Trigger, ?, ?, ?, ?> triggers) {

            checkSchemaMap(DDLObjectType.TRIGGER, triggers);

            return getThis();
        }

        public final U setFunctions(SchemaMap<DBFunction, ?, ?, ?, ?> functions) {

            checkSchemaMap(DDLObjectType.FUNCTION, functions);

            return getThis();
        }

        public final U setProcedures(SchemaMap<Procedure, ?, ?, ?, ?> procedures) {

            checkSchemaMap(DDLObjectType.PROCEDURE, procedures);

            return getThis();
        }

        public final U setSchemaMap(DDLObjectType ddlObjectType, SchemaMap<?, ?, ?, ?, ?> schemaMap) {

            checkSchemaMap(ddlObjectType, schemaMap);

            return getThis();
        }

        @SuppressWarnings("unchecked")
        protected final <R extends SchemaMap<?, ?, ?, ?, ?>> R mapOrEmpty(DDLObjectType ddlObjectType) {

            Objects.requireNonNull(ddlObjectType);

            final R map = (R)schemaMaps[ddlObjectType.ordinal()];

            return map != null ? map : (R)makeEmptySchema();
        }

        private <R extends ISchemaMap<?>> void checkSchemaMap(DDLObjectType ddlObjectType, R value) {

            Objects.requireNonNull(ddlObjectType);
            Objects.requireNonNull(value);

            final int index = ddlObjectType.ordinal();

            @SuppressWarnings("unchecked")
            final T schemaMap = (T)checkNoExistingSchemaMap(value, schemaMaps[index]);

            this.schemaMaps[index] = schemaMap;
        }

        private static <T extends ISchemaMap<?>> T checkNoExistingSchemaMap(T value, T existing) {

            Objects.requireNonNull(value);

            if (existing != null) {

                throw new IllegalArgumentException();
            }

            return value;
        }

        @SuppressWarnings("unchecked")
        private U getThis() {

            return (U)this;
        }
    }

    private final Function<SchemaMap<?, ?, ?, ?, ?>, T> createObjectState;
    private final Function<T, SchemaMap<?, ?, ?, ?, ?>> getSchemaMap;

    private final T[] schemaObjectStates;

    protected BaseSchemaMaps(Function<SchemaMap<?, ?, ?, ?, ?>, T> createObjectState, IntFunction<T[]> createArray, Function<T, SchemaMap<?, ?, ?, ?, ?>> getSchemaMap,
            SchemaMap<Table, ?, ?, ?, ?> tables, SchemaMap<View, ?, ?, ?, ?> views, SchemaMap<Index, ?, ?, ?, ?> indices, SchemaMap<Trigger, ?, ?, ?, ?> triggers,
            SchemaMap<DBFunction, ?, ?, ?, ?> functions, SchemaMap<Procedure, ?, ?, ?, ?> procedures) {
        this(createObjectState, createArray, getSchemaMap);

        initialize(createObjectState, tables, views, indices, triggers, functions, procedures);
    }

    BaseSchemaMaps(Function<SchemaMap<?, ?, ?, ?, ?>, T> createObjectState, IntFunction<T[]> createArray, Function<T, SchemaMap<?, ?, ?, ?, ?>> getSchemaMap) {

        Objects.requireNonNull(createObjectState);
        Objects.requireNonNull(createArray);
        Objects.requireNonNull(getSchemaMap);

        this.createObjectState = createObjectState;
        this.getSchemaMap = getSchemaMap;

        this.schemaObjectStates = createArray.apply(DDLObjectType.getNumObjectTypes());
    }

    final void initialize(SchemaMap<Table, ?, ?, ?, ?> tables, SchemaMap<View, ?, ?, ?, ?> views, SchemaMap<Index, ?, ?, ?, ?> indices, SchemaMap<Trigger, ?, ?, ?, ?> triggers,
            SchemaMap<DBFunction, ?, ?, ?, ?> functions, SchemaMap<Procedure, ?, ?, ?, ?> procedures) {

        initialize(createObjectState, tables, views, indices, triggers, functions, procedures);
    }

    private void initialize(Function<SchemaMap<?, ?, ?, ?, ?>, T> createObjectState, SchemaMap<Table, ?, ?, ?, ?> tables, SchemaMap<View, ?, ?, ?, ?> views,
            SchemaMap<Index, ?, ?, ?, ?> indices, SchemaMap<Trigger, ?, ?, ?, ?> triggers, SchemaMap<DBFunction, ?, ?, ?, ?> functions,
            SchemaMap<Procedure, ?, ?, ?, ?> procedures) {

        schemaObjectStates[DDLObjectType.TABLE.ordinal()] = createObjectState.apply(tables);
        schemaObjectStates[DDLObjectType.VIEW.ordinal()] = createObjectState.apply(views);
        schemaObjectStates[DDLObjectType.INDEX.ordinal()] = createObjectState.apply(indices);
        schemaObjectStates[DDLObjectType.TRIGGER.ordinal()] = createObjectState.apply(triggers);
        schemaObjectStates[DDLObjectType.FUNCTION.ordinal()] = createObjectState.apply(functions);
        schemaObjectStates[DDLObjectType.PROCEDURE.ordinal()] = createObjectState.apply(procedures);
    }

    @Override
    public final void reset() {

        Arrays.fill(schemaObjectStates, null);
    }

    private void setSchemaMap(DDLObjectType objectType, SchemaMap<?, ?, ?, ?, ?> schemaMap) {

        Objects.requireNonNull(objectType);
        Objects.requireNonNull(schemaMap);

        final int index = objectType.ordinal();

        if (ASSERT) {

            Assertions.isNull(schemaObjectStates[index]);
        }

        schemaObjectStates[index] = createObjectState.apply(schemaMap);
    }

    final T getSchemaObjectState(DDLObjectType objectType) {

        Objects.requireNonNull(objectType);

        return schemaObjectStates[objectType.ordinal()];
    }

    public final boolean containsSchemaObjectName(DDLObjectType objectType, long hashObjectName) {

        Objects.requireNonNull(objectType);
        StringRef.checkIsString(hashObjectName);

        return getSchemaMap(objectType).containsSchemaObjectName(hashObjectName);
    }

    @SuppressWarnings("unchecked")
    public final <R extends SchemaObject> R getSchemaObject(DDLObjectType objectType, int objectId) {

        Objects.requireNonNull(objectType);
        Checks.isSchemaObjectId(objectId);

        return (R)getSchemaMap(objectType).getSchemaObjectById(objectId);
    }

    @SuppressWarnings("unchecked")
    public final <R extends SchemaObject> ISchemaMap<R> getSchemaMap(DDLObjectType objectType) {

        Objects.requireNonNull(objectType);

        return (ISchemaMap<R>)getSchemaMap.apply(getSchemaObjectState(objectType));
    }

    public final int computeMaxId(DDLObjectType objectType, int defaultValue) {

        Objects.requireNonNull(objectType);

        return getSchemaMap(objectType).maxInt(defaultValue, DBNamedIdentifiableObject::getId);
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
                final ISchemaMap<DBNamedObject> thisSchemaMap = (ISchemaMap<DBNamedObject>)thisSchemaObjectStates[i];
                @SuppressWarnings("unchecked")
                final ISchemaMap<DBNamedObject> otherSchemaMap = (ISchemaMap<DBNamedObject>)otherSchemaObjectStates[i];

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
