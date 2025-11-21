package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

public interface IIntToObjectMapView<V> extends IIntKeyMapView, IIntToObjectMapGetters<V>, IToObjectMapGetters<V> {

    @Override
    default <P, E extends Exception> void forEachValue(P parameter, IObjectForEachMapValue<V, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
