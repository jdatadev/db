package dev.jdata.db.schema.model.objects;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public abstract class BaseDDLObjectTypeMap<T> {

    private final T[] elements;

    protected BaseDDLObjectTypeMap(IntFunction<T[]> createArray, BiFunction<DDLObjectType, Function<DDLObjectType, T>, T> createElement) {

        Objects.requireNonNull(createArray);
        Objects.requireNonNull(createElement);

        final DDLObjectType[] ddlObjectTypes = DDLObjectType.values();

        final int numDDLObjectTypes = ddlObjectTypes.length;

        final T[] instances = this.elements = createArray.apply(numDDLObjectTypes);

        for (int i = 0; i < numDDLObjectTypes; ++ i) {

            instances[i] = createElement.apply(ddlObjectTypes[i], this::getElement);
        }
    }

    protected final T getElement(DDLObjectType ddlObjectType) {

        Objects.requireNonNull(ddlObjectType);

        return elements[ddlObjectType.ordinal()];
    }
}
