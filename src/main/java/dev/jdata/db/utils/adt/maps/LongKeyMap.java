package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.Clearable;
import dev.jdata.db.utils.adt.elements.Elements;

public interface LongKeyMap extends Elements, Clearable {

    boolean containsKey(long key);

    long[] keys();

    boolean remove(long key);
}
