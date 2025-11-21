package dev.jdata.db.schema.model.schemamaps;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.ISchemaObjects;
import dev.jdata.db.schema.model.objects.DBFunction;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.Index;
import dev.jdata.db.schema.model.objects.Procedure;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.model.objects.Trigger;
import dev.jdata.db.schema.model.objects.View;
import dev.jdata.db.schema.model.schemamap.ISchemaMap;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseSimpleSchemaMapsBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_MAP extends ISchemaMap<SCHEMA_OBJECT>,
                SCHEMA_MAPS extends ISchemaMaps,
                HEAP_SCHEMA_MAPS extends ISchemaMaps & IHeapSchemaMapsMarker,
                SCHEMA_MAPS_BUILDER extends ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER>>

        extends BaseSchemaMapsMutableBuilder<SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAP[]>
        implements ISchemaMapsBuilder<SCHEMA_OBJECT, SCHEMA_MAPS, HEAP_SCHEMA_MAPS, SCHEMA_MAPS_BUILDER> {

    @SuppressWarnings("unchecked")
    protected static <T extends ISchemaMap<?>, E extends ISchemaMap<?>, R extends ISchemaMap<?>> R mapOrEmpty(T[] schemaMaps, DDLObjectType ddlObjectType, E empty) {

        Checks.checkArrayLength(schemaMaps, DDLObjectType.getNumObjectTypes());
        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(empty);

        final R result = (R)mapOrEmpty(schemaMaps, ddlObjectType, empty, m -> m);

        return result;
    }

    protected static <T extends ISchemaMap<?>, E extends R, R extends ISchemaMap<?>> R mapOrEmpty(T[] schemaMaps, DDLObjectType ddlObjectType, E empty, Function<T, R> mapper) {

        Checks.checkArrayLength(schemaMaps, DDLObjectType.getNumObjectTypes());
        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(empty);
        Objects.requireNonNull(mapper);

        final T map = schemaMaps[ddlObjectType.ordinal()];

        return map != null ? mapper.apply(map) : empty;
    }

    protected BaseSimpleSchemaMapsBuilder(AllocationType allocationType, IntFunction<SCHEMA_MAP[]> createSchemaMapsArray) {
        super(allocationType, DDLObjectType.getNumObjectTypes(), createSchemaMapsArray, (a, n, c) -> c.apply(Integers.checkUnsignedLongToUnsignedInt(n)));
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setTables(ISchemaMap<Table> tables) {

        checkSetSchemaMap(DDLObjectType.TABLE, tables);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setViews(ISchemaMap<View> views) {

        checkSetSchemaMap(DDLObjectType.VIEW, views);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setIndices(ISchemaMap<Index> indices) {

        checkSetSchemaMap(DDLObjectType.INDEX, indices);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setTriggers(ISchemaMap<Trigger> triggers) {

        checkSetSchemaMap(DDLObjectType.TRIGGER, triggers);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setFunctions(ISchemaMap<DBFunction> functions) {

        checkSetSchemaMap(DDLObjectType.FUNCTION, functions);

        return getThis();
    }

    @Override
    public final SCHEMA_MAPS_BUILDER setProcedures(ISchemaMap<Procedure> procedures) {

        checkSetSchemaMap(DDLObjectType.PROCEDURE, procedures);

        return getThis();
    }

    public final SCHEMA_MAPS_BUILDER setSchemaMap(DDLObjectType ddlObjectType, ISchemaMap<?> schemaMap) {

        checkSetSchemaMap(ddlObjectType, schemaMap);

        return getThis();
    }

    public final SCHEMA_MAPS_BUILDER setSchemaMaps(BaseSchemaMaps<? extends ISchemaMap<?>> schemaMaps) {

        Objects.requireNonNull(schemaMaps);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            setSchemaMap(ddlObjectType, schemaMaps.getSchemaMapForObjectType(ddlObjectType));
        }

        return getThis();
    }

    public final SCHEMA_MAPS_BUILDER setSchemaObjects(ISchemaObjects schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            final ISchemaMap<?> schemaMap = schemaObjects.getSchemaMap(ddlObjectType);

            setSchemaMap(ddlObjectType, schemaMap);
        }

        return getThis();
    }

    @Override
    public final boolean isEmpty() {

        return IContainsView.isNullOrEmpty(getMutable());
    }

    private <R extends ISchemaMap<?>> void checkSetSchemaMap(DDLObjectType ddlObjectType, R value) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(value);

        final int index = ddlObjectType.ordinal();

        final SCHEMA_MAP[] schemaMaps = getMutable();

        @SuppressWarnings("unchecked")
        final SCHEMA_MAP schemaMap = (SCHEMA_MAP)checkNoExistingSchemaMap(value, schemaMaps[index]);

        schemaMaps[index] = schemaMap;
    }

    private static <T extends ISchemaMap<?>> T checkNoExistingSchemaMap(T value, T existing) {

        Objects.requireNonNull(value);

        if (existing != null) {

            throw new IllegalArgumentException();
        }

        return value;
    }

    @SuppressWarnings("unchecked")
    private SCHEMA_MAPS_BUILDER getThis() {

        return (SCHEMA_MAPS_BUILDER)this;
    }
}
