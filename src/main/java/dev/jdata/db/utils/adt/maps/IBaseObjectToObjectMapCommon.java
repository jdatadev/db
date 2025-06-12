package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

interface IBaseObjectToObjectMapCommon<K, V, M extends IBaseObjectToObjectMapCommon<K, V, M>>

        extends IObjectKeyMap<K>, IObjectToObjectCommonMapGetters<K, V>, IToObjectMapGetters<V, M> {

    @Override
    default <P> void forEachValue(P parameter, IForEachValue<V, P> forEachValue) {

        Objects.requireNonNull(forEachValue);

        forEachKeyAndValueWithResult(null, parameter, forEachValue, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
