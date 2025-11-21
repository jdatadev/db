package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;

abstract class LongToIntEmptyStaticMap extends LongToIntEmptyMap implements ILongToIntStaticMap {

    @Override
    public final int get(long key) {

        throw MapExceptions.noSuchKeyException();
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            ILongToIntForEachMapKeyAndValueWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        return defaultResult;
    }

    @Override
    public final long keysAndValues(ILongAnyOrderAddable keysDst, IIntAnyOrderAddable valuesDst) {

        Objects.requireNonNull(keysDst);
        Objects.requireNonNull(valuesDst);

        return 0L;
    }
}
