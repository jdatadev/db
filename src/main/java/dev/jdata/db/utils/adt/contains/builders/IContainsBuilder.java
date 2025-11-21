package dev.jdata.db.utils.adt.contains.builders;

import dev.jdata.db.utils.adt.contains.IContains;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.builders.IBuilder;

public interface IContainsBuilder<T extends IContains, U extends IContains & IHeapContainsMarker> extends IBuilder, IContainsBuildable<T, U>, IContainsView {

}
