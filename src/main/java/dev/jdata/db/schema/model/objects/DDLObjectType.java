package dev.jdata.db.schema.model.objects;

import java.util.Objects;
import java.util.function.IntFunction;

public enum DDLObjectType {

    TABLE(true, Table.class, Table[]::new),
    VIEW(true, View.class, View[]::new),
    INDEX(true, Index.class, Index[]::new),
    TRIGGER(false, Trigger.class, Trigger[]::new),
    FUNCTION(false, DBFunction.class, DBFunction[]::new),
    PROCEDURE(false, Procedure.class, Procedure[]::new);

    private static int numObjectTypes = -1;

    private final boolean hasColumns;
    private final Class<? extends SchemaObject> schemaObjectType;
    private final IntFunction<Object> createArray;

    private DDLObjectType(boolean hasColumns, Class<? extends SchemaObject> schemaObjectType, IntFunction<Object> createArray) {

        this.hasColumns = hasColumns;
        this.schemaObjectType = Objects.requireNonNull(schemaObjectType);
        this.createArray = Objects.requireNonNull(createArray);
    }

    public boolean hasColumns() {
        return hasColumns;
    }

    public Class<? extends SchemaObject> getSchemaObjectType() {
        return schemaObjectType;
    }

    @SuppressWarnings("unchecked")
    public <T> IntFunction<T> getCreateArray() {

        return (IntFunction<T>)createArray;
    }

    public static int getNumObjectTypes() {

        final int num = numObjectTypes;

        if (num == -1) {

            numObjectTypes = DDLObjectType.values().length;
        }

        return num;
    }

    @SuppressWarnings("unchecked")
    private <T extends SchemaObject> T[] createArray(int length) {

        return (T[])createArray.apply(length);
    }
}
