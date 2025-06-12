package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IBaseIntToLongMapCommon<M extends IBaseIntToLongMapCommon<M>> extends IIntKeyMap, IIntToLongCommonMapGetters, IToLongMapGetters<M> {

    @Override
    default <P> void forEachValue(P parameter, IForEachValue<P> forEach) {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
