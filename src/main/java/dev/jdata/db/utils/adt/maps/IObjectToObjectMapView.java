package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;

public interface IObjectToObjectMapView<K, V> extends IObjectKeyMapView<K>, IObjectToObjectMapGetters<K, V>, IToObjectMapGetters<V> {

    @Override
    default <P, E extends Exception> void forEachValue(P parameter, IObjectForEachMapValue<V, P, E> forEachValue) throws E {

        Objects.requireNonNull(forEachValue);

        forEachKeyAndValueWithResult(null, parameter, forEachValue, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }

    @Override
    default long keysAndValues(IObjectAnyOrderAddable<K> keysDst, IObjectAnyOrderAddable<V> valuesDst) {

        forEachKeyAndValueWithResult(null, keysDst, valuesDst, (key, value, kDst, vDst) -> {

            kDst.addInAnyOrder(key);
            vDst.addInAnyOrder(value);

            return null;
        });

        return getNumElements();
    }
}
