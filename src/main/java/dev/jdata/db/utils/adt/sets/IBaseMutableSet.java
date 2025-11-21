package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IMutableObjectUnorderedOnlyElements;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IObjectElementsRandomAccessRemovalMutable;

interface IBaseMutableSet<T>

        extends IMutableObjectUnorderedOnlyElements<T>,
                IMutableSetType,
                IBaseObjectSetCommon<T>,
                IObjectElementsRandomAccessRemovalMutable<T>,
                IObjectSetMutators<T>,
                IObjectAnyOrderAddable<T> {

    @Override
    default void addUnordered(T value) {

        Objects.requireNonNull(value);

        addToSet(value);
    }

    @Override
    default void addInAnyOrder(T value) {

        Objects.requireNonNull(value);

        addToSet(value);
    }
}
