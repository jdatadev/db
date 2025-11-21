package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

abstract class ObjectEmptyElements<T> extends EmptyElements implements IObjectElementsView<T> {

    @Override
    public final <P> void toString(StringBuilder sb, P parameter, ElementsToStringAdder<T, P> consumer) {

        Objects.requireNonNull(sb);
        Objects.requireNonNull(consumer);

        sb.append(ELEMENTS_TO_STRING_PREFIX);
        sb.append(ELEMENTS_TO_STRING_SUFFIX);
    }
}
