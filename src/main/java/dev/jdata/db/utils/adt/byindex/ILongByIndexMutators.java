package dev.jdata.db.utils.adt.byindex;

import dev.jdata.db.utils.checks.Checks;

public interface ILongByIndexMutators extends IByIndexMutatorsMarker {

    long setAndReturnPrevious(long index, long value);

    default void set(long index, long value) {

        Checks.isLongIndex(index);

        setAndReturnPrevious(index, value);
    }
}
