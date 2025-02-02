package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.Elements;

public interface IObjectArray<T> extends Elements {

    T get(int index);
}
