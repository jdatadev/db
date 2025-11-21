package dev.jdata.db.utils.adt.elements;

import java.util.Comparator;

public interface IObjectOrderedOnlyElementsAllocator<

                T,
                IMMUTABLE extends IElements & IOnlyElementsView & IObjectOrderedElementsView<T>,
                MUTABLE extends IMutableElements & IOnlyElementsView & IObjectOrderedElementsView<T>,
                BUILDER extends IObjectOrderedOnlyElementsBuilder<T, IMMUTABLE, ?>>

        extends IObjectElementsAllocator<T, IMMUTABLE, MUTABLE, BUILDER>, ISortableAllocator<IObjectIterableOnlyElementsView<T>, Comparator<? super T>, IMMUTABLE> {

}
