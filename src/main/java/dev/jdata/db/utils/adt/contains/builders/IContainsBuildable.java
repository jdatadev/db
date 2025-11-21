package dev.jdata.db.utils.adt.contains.builders;

import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.builders.IHeapBuildable;

public interface IContainsBuildable<T extends IContainsView, U extends IContainsView & IHeapContainsMarker> extends IHeapBuildable<T, U> {

}
