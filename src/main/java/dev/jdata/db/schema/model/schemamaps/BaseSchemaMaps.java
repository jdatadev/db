package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.jutils.io.strings.StringRef;

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
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseSchemaMaps<T> {

    public static abstract class BaseBuilder<T extends BaseBuilder<T>> extends ObjectCacheNode {

        private final SchemaMap<?>[] schemaMaps;

        protected BaseBuilder() {

            final DDLObjectType[] ddlObjectTypes = DDLObjectType.values();

            final int numDDLObjectTypes = ddlObjectTypes.length;

            this.schemaMaps = new SchemaMap[numDDLObjectTypes];
        }

        public final T setTables(SchemaMap<Table> tables) {

            checkSchemaMap(DDLObjectType.TABLE, tables);

            return getThis();
        }

        public final T setViews(SchemaMap<View> views) {

            checkSchemaMap(DDLObjectType.VIEW, views);

            return getThis();
        }

        public final T setIndices(SchemaMap<Index> indices) {

            checkSchemaMap(DDLObjectType.INDEX, indices);

            return getThis();
        }

        public final T setTriggers(SchemaMap<Trigger> triggers) {

            checkSchemaMap(DDLObjectType.TRIGGER, triggers);

            return getThis();
        }

        public final T setFunctions(SchemaMap<DBFunction> functions) {

            checkSchemaMap(DDLObjectType.FUNCTION, functions);

            return getThis();
        }

        public final T setProcedures(SchemaMap<Procedure> procedures) {

            checkSchemaMap(DDLObjectType.PROCEDURE, procedures);

            return getThis();
        }

        public final T setSchemaMap(DDLObjectType ddlObjectType, SchemaMap<?> schemaMap) {

            checkSchemaMap(ddlObjectType, schemaMap);

            return getThis();
        }

        @SuppressWarnings("unchecked")
        protected final <R extends SchemaObject> SchemaMap<R> mapOrEmpty(DDLObjectType ddlObjectType) {

            return (SchemaMap<R>)mapOrEmpty(schemaMaps[ddlObjectType.ordinal()]);
        }

        @SuppressWarnings("unchecked")
        private static <T extends SchemaObject> SchemaMap<T> mapOrEmpty(SchemaMap<T> map) {

            return map != null ? map : (SchemaMap<T>)SchemaMap.empty();
        }

        private <R extends ISchemaMap<?>> void checkSchemaMap(DDLObjectType ddlObjectType, R value) {

            Objects.requireNonNull(ddlObjectType);
            Objects.requireNonNull(value);

            final int index = ddlObjectType.ordinal();

            this.schemaMaps[index] = (SchemaMap<?>)checkNoExistingSchemaMap(value, schemaMaps[index]);
        }

        private static <T extends ISchemaMap<?>> T checkNoExistingSchemaMap(T value, T existing) {

            Objects.requireNonNull(value);

            if (existing != null) {

                throw new IllegalArgumentException();
            }

            return value;
        }

        @SuppressWarnings("unchecked")
        private T getThis() {

            return (T)this;
        }
    }

    private final Function<SchemaMap<?>, T> createObjectState;
    private final Function<T, SchemaMap<?>> getSchemaMap;

    private final T[] schemaObjectStates;

    protected BaseSchemaMaps(Function<SchemaMap<?>, T> createObjectState, IntFunction<T[]> createArray, Function<T, SchemaMap<?>> getSchemaMap, SchemaMap<Table> tables,
            SchemaMap<View> views, SchemaMap<Index> indices, SchemaMap<Trigger> triggers, SchemaMap<DBFunction> functions, SchemaMap<Procedure> procedures) {

        Objects.requireNonNull(createObjectState);
        Objects.requireNonNull(createArray);
        Objects.requireNonNull(getSchemaMap);

        this.createObjectState = createObjectState;
        this.getSchemaMap = getSchemaMap;

        this.schemaObjectStates = createArray.apply(DDLObjectType.getNumObjectTypes());

        schemaObjectStates[DDLObjectType.TABLE.ordinal()] = createObjectState.apply(tables);
        schemaObjectStates[DDLObjectType.VIEW.ordinal()] = createObjectState.apply(views);
        schemaObjectStates[DDLObjectType.INDEX.ordinal()] = createObjectState.apply(indices);
        schemaObjectStates[DDLObjectType.TRIGGER.ordinal()] = createObjectState.apply(triggers);
        schemaObjectStates[DDLObjectType.FUNCTION.ordinal()] = createObjectState.apply(functions);
        schemaObjectStates[DDLObjectType.PROCEDURE.ordinal()] = createObjectState.apply(procedures);
    }

    BaseSchemaMaps(Function<SchemaMap<?>, T> createObjectState, IntFunction<T[]> createArray, Function<T, SchemaMap<?>> getSchemaMap) {

        Objects.requireNonNull(createObjectState);
        Objects.requireNonNull(createArray);
        Objects.requireNonNull(getSchemaMap);

        this.createObjectState = createObjectState;
        this.getSchemaMap = getSchemaMap;

        this.schemaObjectStates = createArray.apply(DDLObjectType.getNumObjectTypes());
    }

    final void setSchemaMap(DDLObjectType objectType, SchemaMap<?> schemaMap) {

        Objects.requireNonNull(objectType);
        Objects.requireNonNull(schemaMap);

        final int index = objectType.ordinal();

        if (schemaObjectStates[index] == null) {

            throw new IllegalStateException();
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
}
