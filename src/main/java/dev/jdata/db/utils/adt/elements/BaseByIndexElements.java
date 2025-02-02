package dev.jdata.db.utils.adt.elements;

public abstract class BaseByIndexElements extends BaseNumElements {

    protected abstract void toString(long index, StringBuilder sb);

    @Override
    public final String toString() {

        return ByIndex.largeToString(this, 0L, getNumElements(), (byIndex, sb) -> sb.append(byIndex.getClass().getSimpleName()),
                (byIndex, index, sb) -> byIndex.toString(index, sb));
    }
}
