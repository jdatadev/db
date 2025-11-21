package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.IntFunction;

public interface IObjectUnorderedOnlyElementsView<T> extends IObjectUnorderedElementsView<T>, IObjectIterableOnlyElementsView<T> {

    @Override
    default T[] toArray(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        return ObjectElementsHelper.toArrayIterable(this, IOnlyElementsView.intNumElements(this), createArray);
    }
}
