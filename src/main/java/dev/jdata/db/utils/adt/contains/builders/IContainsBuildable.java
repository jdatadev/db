package dev.jdata.db.utils.adt.contains.builders;

import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.builders.IBuildable;

public interface IContainsBuildable<T extends IContains, U extends IContains & IHeapContainsMarker> extends IBuildable<T> {

    T buildOrEmpty();
    T buildOrNull();

    U buildHeapAllocatedOrEmpty();
    U buildHeapAllocatedOrNull();
}
