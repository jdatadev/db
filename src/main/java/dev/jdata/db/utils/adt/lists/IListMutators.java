package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

public interface IListMutators<T> {

    boolean removeAtMostOneInstance(T instance);

    default void removeExactlyOneInstance(T instance) {

        if (!removeAtMostOneInstance(instance)) {

            throw new NoSuchElementException();
        }
    }
}
