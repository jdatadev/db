package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.IIterableElements;

public final class MutableIndexList<T> extends BaseIndexList<T> implements IMutableIndexList<T> {

    public MutableIndexList(AllocationType allocationType) {
        super(allocationType);
    }

    public MutableIndexList(AllocationType allocationType, IntFunction<T[]> createArray, int initialCapacity) {
        super(allocationType, createArray, initialCapacity);
    }

    MutableIndexList(AllocationType allocationType, IntFunction<T[]> createArray, IIndexList<T> toCopy) {
        super(allocationType, createArray, toCopy);
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

        return swapToOther(false, (c, a, n) -> new IndexList<T>(AllocationType.HEAP, c, a, n));
    }

    IndexList<T> swapToImmutableAndClear() {

        return swapToOther(true, (c, a, n) -> new IndexList<T>(AllocationType.HEAP, c, a, n));
    }
}
