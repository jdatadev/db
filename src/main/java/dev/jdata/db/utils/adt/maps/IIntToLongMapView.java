package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

public interface IIntToLongMapView extends IIntKeyMapView, IIntToLongMapGetters, IToLongMapGetters {

    @Override
    default <P, E extends Exception> void forEachValue(P parameter, ILongForEachMapValue<P, E> forEach) throws E{

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(v, p);

            return null;
        });
    }
}
