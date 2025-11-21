package dev.jdata.db.utils.adt.mutability;

import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;

public interface IInstanceBuilder<T extends IImmutable, U extends IImmutable & IHeapTypeMarker> extends IImmutableBuilder<T, U> {

}
