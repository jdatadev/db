package dev.jdata.db.utils.adt.byindex;

import java.util.Objects;

public interface IObjectByIndexView<T> extends IByIndexView, IObjectByIndexGetters<T> {

    @Override
    default void toString(long index, StringBuilder sb) {

        sb.append(Objects.toString(get(index)));
    }

    @Override
    default void toHexString(long index, StringBuilder sb) {

        toString(index, sb);
    }
}
