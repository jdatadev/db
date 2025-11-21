package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.EmptyOnlyElements;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;

abstract class EmptyMap<T extends IAnyOrderAddable> extends EmptyOnlyElements implements IBaseMap<T> {

    @Override
    public final long keys(T addable) {

        return 0L;
    }

    @Override
    public final long getNumKeys() {

        return 0L;
    }
}
