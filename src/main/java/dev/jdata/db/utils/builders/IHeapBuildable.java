package dev.jdata.db.utils.builders;

import dev.jdata.db.utils.adt.marker.IHeapTypeMarker;

public interface IHeapBuildable<T, U extends IHeapTypeMarker> extends IBuildable<T> {

    U buildHeapAllocatedOrEmpty();
    U buildHeapAllocatedOrNull();
    U buildHeapAllocatedNotEmpty();
}
