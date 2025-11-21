package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.IScatteredElementsView;

public interface IOneDimensionalArrayView extends IArrayView, IByIndexView, IScatteredElementsView, IOneDimensionalArrayGetters {

    @Override
    default boolean isEmpty() {

        return getLimit() == 0L;
    }
}
