package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IIterableElements;

public final class MutableIndexList<T> extends BaseIndexList<T> implements IMutableIndexList<T> {

    public MutableIndexList() {

    }

    public MutableIndexList(IntFunction<T[]> createArray, int initialCapacity) {
        super(createArray, initialCapacity);
    }

    MutableIndexList(IntFunction<T[]> createArray, IIndexList<T> toCopy) {
        super(createArray, toCopy);
    }

    @Override
    public final void addTail(IIterableElements<T> elements) {

        if (elements instanceof BaseArrayList<?>) {

            @SuppressWarnings("unchecked")
            final BaseArrayList<T> baseArrayList = (BaseArrayList<T>)elements;

            addTail(baseArrayList);
        }
        else {
            IMutableIndexList.super.addTail(elements);
        }
    }

    IndexList<T> swapToImmutable() {

        return swapToOther(false, IndexList::new);
    }

    IndexList<T> swapToImmutableAndClear() {

        return swapToOther(true, IndexList::new);
    }
}
