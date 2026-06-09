package dev.jdata.db.schema.model.schemamap;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.ISchemaObjectsByObjectType;
import dev.jdata.db.schema.model.objects.DDLObjectType;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjects;
import dev.jdata.db.schema.model.schemaobjects.ISchemaObjectsBuilder;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseSimpleSchemaMapBuilder<

                SCHEMA_OBJECT extends SchemaObject,
                SCHEMA_OBJECTS extends ISchemaObjects<SCHEMA_OBJECT>,
                SCHEMA_OBJECTS_BUILDER extends ISchemaObjectsBuilder<SCHEMA_OBJECT, ?, ?>,
                SCHEMA_MAP extends ISchemaMap,
                HEAP_SCHEMA_MAP extends ISchemaMap & IHeapSchemaMapMarker,
                SCHEMA_MAP_BUILDER extends ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER>>

        extends BaseSchemaMapMutableBuilder<SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_OBJECTS_BUILDER[]>
        implements ISchemaMapBuilder<SCHEMA_OBJECT, SCHEMA_MAP, HEAP_SCHEMA_MAP, SCHEMA_MAP_BUILDER> {

    @SuppressWarnings("unchecked")
    protected static <T extends SchemaObject, U extends ISchemaObjects<T>, E extends ISchemaObjects<T>, R extends ISchemaObjects<?>> R mapOrEmpty(U[] schemaObjectsArray,
            DDLObjectType ddlObjectType, E empty) {

        Checks.checkArrayLength(schemaObjectsArray, DDLObjectType.getNumObjectTypes());
        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(empty);

        final R result = (R)mapOrEmpty(schemaObjectsArray, ddlObjectType, empty, m -> m);

        return result;
    }

    protected static <T extends SchemaObject, U extends SchemaObject, V, E extends R, R extends ISchemaObjects<U>> R mapOrEmpty(V[] schemaObjectsArray,
            DDLObjectType ddlObjectType, E empty, Function<V, R> mapper) {

        Checks.checkArrayLength(schemaObjectsArray, DDLObjectType.getNumObjectTypes());
        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(empty);
        Objects.requireNonNull(mapper);

        final V map = schemaObjectsArray[ddlObjectType.ordinal()];

        return map != null ? mapper.apply(map) : empty;
    }

    private final IntFunction<SCHEMA_OBJECTS_BUILDER> createSchemaObjectsBuilder;

    protected BaseSimpleSchemaMapBuilder(AllocationType allocationType, IntFunction<SCHEMA_OBJECTS_BUILDER[]> createSchemaObjectsBuilderArray,
            IntFunction<SCHEMA_OBJECTS_BUILDER> createSchemaObjectsBuilder) {
        super(allocationType, DDLObjectType.getNumObjectTypes(), createSchemaObjectsBuilderArray, (a, n, c) -> c.apply(Integers.checkUnsignedLongToUnsignedInt(n)));

        this.createSchemaObjectsBuilder = Objects.requireNonNull(createSchemaObjectsBuilder);
    }

    @Override
    public final SCHEMA_MAP_BUILDER addSchemaObject(SCHEMA_OBJECT schemaObject) {

        Objects.requireNonNull(schemaObject);

        getOrAddSchemaObjectsBuilder(schemaObject.getDDLObjectType(), 1).addUnordered(schemaObject);

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER addSchemaObjects(ISchemaObjectsByObjectType schemaObjects) {

        Objects.requireNonNull(schemaObjects);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            addSchemaObjects(ddlObjectType, schemaObjects.getSchemaObjects(ddlObjectType));
        }

        return getThis();
    }

    @Override
    public final SCHEMA_MAP_BUILDER addSchemaObjects(DDLObjectType ddlObjectType, ISchemaObjects<SCHEMA_OBJECT> schemaObjects) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObjects);
        Checks.areElements(schemaObjects, ddlObjectType, (e, t) -> e.getDDLObjectType().equals(t));

        checkAddSchemaObjects(ddlObjectType, schemaObjects);

        return getThis();
    }

    private SCHEMA_MAP_BUILDER addSchemaMap(BaseSchemaMap<? extends ISchemaObjects<SCHEMA_OBJECT>> schemaMap) {

        Objects.requireNonNull(schemaMap);

        for (DDLObjectType ddlObjectType : DDLObjectType.values()) {

            addSchemaObjects(ddlObjectType, schemaMap.getSchemaObjectsForObjectType(ddlObjectType));
        }

        return getThis();
    }

    @Override
    public final boolean isEmpty() {

        return IContainsView.isNullOrEmpty(getMutable());
    }

    final void checkAddSchemaObjects(DDLObjectType ddlObjectType, ISchemaObjects<? extends SCHEMA_OBJECT> schemaObjects) {

        Objects.requireNonNull(ddlObjectType);
        Objects.requireNonNull(schemaObjects);
        Checks.areElements(schemaObjects, ddlObjectType, (e, t) -> e.getDDLObjectType().equals(t));

        getOrAddSchemaObjectsBuilder(ddlObjectType, IOnlyElementsView.intNumElements(schemaObjects)).addUnordered(schemaObjects);
    }

    private SCHEMA_OBJECTS_BUILDER getOrAddSchemaObjectsBuilder(DDLObjectType ddlObjectType, int initialCapacity) {

        final int index = ddlObjectType.ordinal();

        final SCHEMA_OBJECTS_BUILDER[] mutable = getMutable();

        SCHEMA_OBJECTS_BUILDER schemaObjectsBuilder = mutable[index];

        if (schemaObjectsBuilder == null) {

            schemaObjectsBuilder = mutable[index] = createSchemaObjectsBuilder.apply(initialCapacity);
        }

        return schemaObjectsBuilder;
    }

    @SuppressWarnings("unchecked")
    private SCHEMA_MAP_BUILDER getThis() {

        return (SCHEMA_MAP_BUILDER)this;
    }
}
