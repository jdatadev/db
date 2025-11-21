package dev.jdata.db.utils.adt.byindex;

import dev.jdata.db.utils.checks.Checks;

public interface IIntByIndexMutators extends IByIndexMutatorsMarker {

    int setAndReturnPrevious(long index, int value);

    default void set(long index, int value) {

        Checks.isLongIndex(index);

        setAndReturnPrevious(index, value);
    }
}
