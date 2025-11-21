package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.IByIndexOrderedScatteredElementsView;
import dev.jdata.db.utils.adt.elements.IScatteredElementsView;

public interface IOneDimensionalArrayView extends IAnyDimensionalArrayView, IByIndexOrderedScatteredElementsView, IScatteredElementsView, IOneDimensionalArrayGetters {

    @Override
    default boolean isEmpty() {

        return getLimit() == 0L;
    }

    @Override
    default long getIndexLimit() {

        return getLimit();
    }
}
