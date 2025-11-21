package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;

interface ILongToObjectMapGetters<V> extends IKeyValueMapGettersHelper<ILongAnyOrderAddable, IObjectAnyOrderAddable<V>> {

    <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ILongToObjectForEachMapKeyAndValue2<V, P1, P2, E> forEach) throws E;

    <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            ILongToObjectForEachMapKeyAndValueWithResult<V, P1, P2, R, E> forEach) throws E;

    default <P1, E extends Exception> void forEachKeyAndValue(P1 parameter, ILongToObjectForEachMapKeyAndValue<V, P1, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachKeyAndValue(parameter, forEach, (k, v, p, f) -> {

            f.each(k, v, p);
        });
    }
}
