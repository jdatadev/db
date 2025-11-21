package dev.jdata.db.utils.adt.byindex;

import dev.jdata.db.utils.checks.Checks;

public interface IObjectByIndexMutators<T> extends IByIndexMutatorsMarker {

    T setAndReturnPrevious(long index, T value);

    default void set(long index, T value) {

        Checks.isLongIndex(index);

        setAndReturnPrevious(index, value);
    }
}
