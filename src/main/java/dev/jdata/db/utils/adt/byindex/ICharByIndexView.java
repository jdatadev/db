package dev.jdata.db.utils.adt.byindex;

import dev.jdata.db.utils.scalars.Integers;

public interface ICharByIndexView extends IByIndexView, ICharByIndexGetters {

    @Override
    default void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    @Override
    default void toHexString(long index, StringBuilder sb) {

        Integers.toHexUnsigned(get(index), sb);
    }
}
