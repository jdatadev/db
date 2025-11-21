package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;

interface ILongToIntMapGetters extends IKeyValueMapGettersHelper<ILongAnyOrderAddable, IIntAnyOrderAddable> {

    <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            ILongToIntForEachMapKeyAndValueWithResult<P1, P2, R, E> forEach) throws E;

    default <P, E extends Exception> void forEachKeyAndValue(P parameter, ILongToIntForEachMapKeyAndValue<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        forEachKeyAndValueWithResult(null, parameter, forEach, (k, v, p, f) -> {

            f.each(k, v, p);

            return null;
        });
    }
}
