package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongIterableElements;

public interface ILongListBuildable<T extends ILongList, U extends ILongListBuildable<T, U>> {

    U addTail(long value);

    U addTail(long ... values);

    default U addTail(ILongIterableElements elements) {

        elements.forEach(this, (e, b) -> b.addTail(e));

        @SuppressWarnings("unchecked")
        final U result = (U)this;

        return result;
    }
}
