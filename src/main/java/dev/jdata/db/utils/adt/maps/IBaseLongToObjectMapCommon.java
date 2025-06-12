package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IBaseLongToObjectMapCommon<T, M extends IBaseLongToObjectMapCommon<T, M>> extends ILongKeyMap, ILongToObjectCommonMapGetters<T>, IToObjectMapGetters<T, M> {

    @Override
    default <P> void forEachValue(P parameter, IForEachValue<T, P> forEach) {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
