package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.IElements;

public interface IObjectArray<T> extends IElements {

    T get(int index);
}
