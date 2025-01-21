package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.Clearable;
import dev.jdata.db.utils.adt.elements.Elements;

public interface KeyMap<T> extends Elements, Clearable {

    T keys();
}
