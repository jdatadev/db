package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IMutableOnlyElementsAllocator;
import dev.jdata.db.utils.adt.elements.IObjectIterableElementsView;

public interface IMutableDoublyLinkedListAllocator<T, U extends IMutableDoublyLinkedList<T>> extends IMutableOnlyElementsAllocator<U, IObjectIterableElementsView<T>> {

}
