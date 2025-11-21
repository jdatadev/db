package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.function.IntComparator;

public interface IIntOrderedOnlyElementsAllocator<

                T extends IElements & IOnlyElementsView & IIntOrderedElementsView,
                U extends IMutableElements & IOnlyElementsView & IIntOrderedElementsView,
                V extends IIntOrderedOnlyElementsBuilder<T, ?>>

        extends IIntElementsAllocator<T, U, V>, ISortableAllocator<IIntIterableOnlyElementsView, IntComparator, T> {

}
