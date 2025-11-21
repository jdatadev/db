package dev.jdata.db.utils.adt.elements;

import java.util.function.IntFunction;

public interface IObjectIterableOnlyElementsView<T> extends IObjectIterableElementsView<T>, IIterableOnlyElementsView {

    @Override
    default T[] toArray(IntFunction<T[]> createArray) {

        return ObjectElementsHelper.toArrayIterable(this, IOnlyElementsView.intNumElements(this), createArray);
    }
}
