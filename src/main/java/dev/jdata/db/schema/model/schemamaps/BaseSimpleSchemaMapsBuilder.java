package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.ISchemaMap;
import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.SchemaMap;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.utils.allocators.NodeObjectCache.ObjectCacheNode;

public abstract class BaseSimpleSchemaMapsBuilder<T extends SchemaMap<?, ?, ?>, U extends BaseSimpleSchemaMapsBuilder<T, U>> extends ObjectCacheNode {

    protected abstract SchemaMap<?, ?, ?> makeEmptySchema();

    private final T[] schemaMaps;

    protected BaseSimpleSchemaMapsBuilder(IntFunction<T[]> createSchemaMapsArray) {

        Objects.requireNonNull(createSchemaMapsArray);

        final DDLObjectType[] ddlObjectTypes = DDLObjectType.values();

        final int numDDLObjectTypes = ddlObjectTypes.length;

        this.schemaMaps = createSchemaMapsArray.apply(numDDLObjectTypes);
    }

    public final U setTables(SchemaMap<Table, ?, ?> tables) {

        checkSetSchemaMap(DDLObjectType.TABLE, tables);

        return getThis();
    }

    public final U setViews(SchemaMap<View, ?, ?> views) {

        checkSetSchemaMap(DDLObjectType.VIEW, views);

        return getThis();
    }

    public final U setIndices(SchemaMap<Index, ?, ?> indices) {

        checkSetSchemaMap(DDLObjectType.INDEX, indices);

        return getThis();
    }

    public final U setTriggers(SchemaMap<Trigger, ?, ?> triggers) {

        checkSetSchemaMap(DDLObjectType.TRIGGER, triggers);

        return getThis();
    }

    public final U setFunctions(SchemaMap<DBFunction, ?, ?> functions) {

        checkSetSchemaMap(DDLObjectType.FUNCTION, functions);

        return getThis();
    }

    public final U setProcedures(SchemaMap<Procedure, ?, ?> procedures) {

        checkSetSchemaMap(DDLObjectType.PROCEDURE, procedures);

        return getThis();
    }

    public final U setSchemaMap(DDLObjectType ddlObjectType, SchemaMap<?, ?, ?> schemaMap) {

        checkSetSchemaMap(ddlObjectType, schemaMap);

        return getThis();
    }

    public final U setSchemaMaps(BaseSchemaMaps<? extends SchemaMap<?, ?, ?>> schemaMaps) {

        Objects.requireNonNull(schemaMaps);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            setSchemaMap(ddlObjectType, schemaMaps.getSchemaMapForObjectType(ddlObjectType));
        }

        return getThis();
    }

    public final U setSchemaObjects(ISchemaObjects schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final SchemaMap<?, ?, ?> schemaMap = (SchemaMap<?, ?, ?>)schemaObjects.getSchemaMap(ddlObjectType);

            setSchemaMap(ddlObjectType, schemaMap);
        }

        return getThis();
    }

    @SuppressWarnings("unchecked")
    protected final <R extends SchemaMap<?, ?, ?>> R mapOrEmpty(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        final R map = (R)schemaMaps[ddlObjectType.ordinal()];

        return map != null ? map : (R)makeEmptySchema();
    }

    private <R extends ISchemaMap<?>> void checkSetSchemaMap(DDLObjectType ddlObjectType, R value) {

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
