package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

public interface IObjectAnyOrderAddable<T> extends IAnyOrderAddable {

    void addInAnyOrder(T instance);
}
