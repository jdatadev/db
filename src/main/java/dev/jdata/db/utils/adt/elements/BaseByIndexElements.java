package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.byindex.IByIndex;

@Deprecated // currently not in use
public abstract class BaseByIndexElements extends BaseNumElements implements IByIndex {

    @Override
    public final String toString() {

        return ByIndex.closureOrConstantLargeToString(this, 0L, getNumElements(), (byIndex, sb) -> sb.append(byIndex.getClass().getSimpleName()),
                (byIndex, index, sb) -> byIndex.toString(index, sb));
    }
}
