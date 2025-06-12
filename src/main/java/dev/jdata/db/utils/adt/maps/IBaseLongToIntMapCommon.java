package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IBaseLongToIntMapCommon<M extends IBaseLongToIntMapCommon<M>> extends ILongKeyMap, ILongToIntCommonMapGetters, IToIntMapGetters<M> {

    @Override
    default <P> void forEachValue(P parameter, IForEachValue<P> forEach) {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
