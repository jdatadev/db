package dev.jdata.db.utils.adt.byindex;

import dev.jdata.db.utils.checks.Checks;

public interface ICharByIndexMutators extends IByIndexMutatorsMarker {

    char setAndReturnPrevious(long index, char value);

    default void set(long index, char value) {

        Checks.isLongIndex(index);

        setAndReturnPrevious(index, value);
    }
}
