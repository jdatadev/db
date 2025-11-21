package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IBaseMutableLongToObjectDynamicMapAllocator<V, M extends IBaseMutableLongToObjectDynamicMap<V>> extends IMutableMapAllocator<M> {

    default M copyLongToObjectMap(M toCopy) {

        Objects.requireNonNull(toCopy);

        final M copy = createMutable(toCopy.getCapacity());

        copy.putAll(toCopy);

        return copy;
    }
}
