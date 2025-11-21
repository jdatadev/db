package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

public abstract class BaseElements<T, U, V> extends BaseADTElements<T, U, V> {

    protected static int intNumElements(IOnlyElementsView elements) {

        Objects.requireNonNull(elements);

        return IOnlyElementsView.intNumElements(elements);
    }

    protected BaseElements(AllocationType allocationType) {
        super(allocationType);
    }
}
