package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.function.LongComparator;

public interface ILongOrderedOnlyElementsAllocator<

                T extends IElements & IOnlyElementsView & ILongOrderedElementsView,
                U extends IMutableElements & IOnlyElementsView & ILongOrderedElementsView,
                V extends ILongOrderedOnlyElementsBuilder<T, ?>>

        extends ILongElementsAllocator<T, U, V>, ISortableAllocator<ILongIterableOnlyElementsView, LongComparator, T> {

}
