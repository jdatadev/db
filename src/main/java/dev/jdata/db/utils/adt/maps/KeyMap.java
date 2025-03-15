package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.elements.IElements;

public interface KeyMap<T> extends IElements, IClearable {

    T keys();
}
