package dev.jdata.db.utils.adt.mutability;

import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;
import dev.jdata.db.utils.builders.IBuilder;
import dev.jdata.db.utils.builders.IHeapBuildable;

public interface IImmutableBuilder<T extends IImmutable, U extends IImmutable & IHeapTypeMarker> extends IBuilder, IHeapBuildable<T, U> {

}
