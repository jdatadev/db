package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.utils.adt.byindex.ByIndex;
import dev.jdata.db.utils.adt.byindex.IObjectByIndexView;
import dev.jdata.db.utils.checks.Checks;

public interface IObjectArrayView<T> extends IOneDimensionalArrayView, IObjectByIndexView<T> {

    @Override
    default boolean containsInstance(T instance, long startIndex, long numElements) {

        Objects.requireNonNull(instance);

        final long limit = getLimit();

        Checks.checkFromIndexNum(startIndex, numElements, limit);

        return ByIndex.containsInstance(this, getLimit(), instance, startIndex, numElements, IObjectByIndexView::get, IndexOutOfBoundsException::new);
    }
}
