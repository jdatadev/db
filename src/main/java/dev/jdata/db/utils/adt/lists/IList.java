package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IIterableElements;

public interface IList<T> extends IIterableElements<T> {

    T getHead();
}
