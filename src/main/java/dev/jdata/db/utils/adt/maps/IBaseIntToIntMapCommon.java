package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IBaseIntToIntMapCommon<M extends IBaseIntToIntMapCommon<M>> extends IIntKeyMap, IIntToIntCommonMapGetters, IToIntMapGetters<M> {

    @Override
    default <P> void forEachValue(P parameter, IForEachValue<P> forEach) {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
