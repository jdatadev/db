package dev.jdata.db.utils.adt.lists;

import java.util.Objects;

@Deprecated
abstract class BaseIntList<D extends BaseLongList> {

    @FunctionalInterface
    public interface IntNodeSetter<T> {

        void setNode(T list, int node);
    }

    static final int NO_NODE = -1;

    final D delegate;

    BaseIntList(D delegate) {

        this.delegate = Objects.requireNonNull(delegate);
    }
}
