package dev.jdata.db.utils.adt.contains.builders;

import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.contains.IHeapContainsMarker;
import dev.jdata.db.utils.builders.IBuilder;

public interface IContainsBuilder<T extends IContainsView, U extends IContainsView & IHeapContainsMarker> extends IBuilder, IContainsBuildable<T, U>, IContainsView {

}
