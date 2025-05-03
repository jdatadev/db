package dev.jdata.db.schema;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

import dev.jdata.db.schema.model.objects.BaseDDLObjectTypeMap;
import dev.jdata.db.schema.model.objects.DDLObjectType;

public abstract class DDLObjectTypeInstances<T> extends BaseDDLObjectTypeMap<T> {

    protected DDLObjectTypeInstances(IntFunction<T[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, T>, T> createElement) {
        super(createArray, createElement);
    }

    protected final T getInstance(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return getElement(ddlObjectType);
    }
}
