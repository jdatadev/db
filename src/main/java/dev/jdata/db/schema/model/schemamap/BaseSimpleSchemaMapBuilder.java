package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.ISchemaObjectsByObjectType;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseSimpleSchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends BaseSchemaMapMutableBuilder<SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_OBJECTS[]>
        implements ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER> {

    @SuppressWarnings("unchecked")
    protected static <T extends ISchemaObjects<?>, E extends ISchemaObjects<?>, R extends ISchemaObjects<?>> R mapOrEmpty(T[] schemaObjectsArray, DDLObjectType ddlObjectType,
            E empty) {

        Checks.checkArrayLength(schemaObjectsArray, DDLObjectType.getNumObjectTypes());
        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(empty);

        final R result = (R)mapOrEmpty(schemaObjectsArray, ddlObjectType, empty, m -> m);

        return result;
    }

    protected static <T extends ISchemaObjects<?>, E extends R, R extends ISchemaObjects<?>> R mapOrEmpty(T[] schemaObjectsArray, DDLObjectType ddlObjectType, E empty,
            Function<T, R> mapper) {

        Checks.checkArrayLength(schemaObjectsArray, DDLObjectType.getNumObjectTypes());
        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(empty);
        Objects.requireNonNull(mapper);

        final T map = schemaObjectsArray[ddlObjectType.ordinal()];

        return map != null ? mapper.apply(map) : empty;
    }

    protected BaseSimpleSchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECTS[]> createSchemaObjectsArray) {
        super(allocationType, DDLObjectType.getNumObjectTypes(), createSchemaObjectsArray, (a, n, c) -> c.apply(Integers.checkUnsignedLongToUnsignedInt(n)));
    }

    @Override
    public final SCHEMA_MAP_BUILDER setTables(ISchemaObjects<Table> tables) {

        checkSetSchemaObjects(DDLObjectType.TABLE, tables);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER setViews(ISchemaObjects<View> views) {

        checkSetSchemaObjects(DDLObjectType.VIEW, views);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER setIndices(ISchemaObjects<Index> indices) {

        checkSetSchemaObjects(DDLObjectType.INDEX, indices);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER setTriggers(ISchemaObjects<Trigger> triggers) {

        checkSetSchemaObjects(DDLObjectType.TRIGGER, triggers);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER setFunctions(ISchemaObjects<DBFunction> functions) {

        checkSetSchemaObjects(DDLObjectType.FUNCTION, functions);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER setProcedures(ISchemaObjects<Procedure> procedures) {

        checkSetSchemaObjects(DDLObjectType.PROCEDURE, procedures);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER addSchemaObject(SCHEMA_OBJECT schemaObject) {

        Objects.requireNonNull(schemaObject);

        throw new UnsupportedOperationException();
    }

    @Override
    public final SCHEMA_MAP_BUILDER addSchemaObjects(ISchemaObjectsByObjectType schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        throw new UnsupportedOperationException();
    }

    @Override
    public final SCHEMA_MAP_BUILDER setSchemaObjects(DDLObjectType ddlObjectType, ISchemaObjects<?> schemaObjects) {

        checkSetSchemaObjects(ddlObjectType, schemaObjects);

        return getThis();
    }

    private SCHEMA_MAP_BUILDER setSchemaMap(BaseSchemaMap<? extends ISchemaObjects<?>> schemaMap) {

        Objects.requireNonNull(schemaMap);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            setSchemaObjects(ddlObjectType, schemaMap.getSchemaObjectsForObjectType(ddlObjectType));
        }

        return getThis();
    }

    public final SCHEMA_MAP_BUILDER setSchemaObjects(ISchemaObjectsByObjectType schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final ISchemaObjects<?> schemaMap = schemaObjects.getSchemaObjects(ddlObjectType);

            setSchemaObjects(ddlObjectType, schemaMap);
        }

        return getThis();
    }

    @Override
    public final boolean isEmpty() {

        return IContainsView.isNullOrEmpty(getMutable());
    }

    private <R extends ISchemaObjects<?>> void checkSetSchemaObjects(DDLObjectType ddlObjectType, R value) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(value);

        final int index = ddlObjectType.ordinal();

        final SCHEMA_OBJECTS[] mutable = getMutable();

        @SuppressWarnings("unchecked")
        final SCHEMA_OBJECTS schemaObjects = (SCHEMA_OBJECTS)checkNoExistingSchemaObjects(value, mutable[index]);

        mutable[index] = schemaObjects;
    }

    private static <T extends ISchemaObjects<?>> T checkNoExistingSchemaObjects(T value, T existing) {

        Objects.requireNonNull(value);

        if (existing != null) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    private SCHEMA_MAP_BUILDER getThis() {

        return (SCHEMA_MAP_BUILDER)this;
    }
}
