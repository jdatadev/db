package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;

abstract class LongToObjectEmptyDynamicMap<V> extends LongToObjectEmptyMap<V> implements ILongToObjectDynamicMap<V> {

    @Override
    public final boolean containsKey(long key) {

        return false;
    }

    @Override
    public final V get(long key, V defaultValue) {

        return defaultValue;
    }

    @Override
    public final <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ILongToObjectForEachMapKeyAndValue2<V, P1, P2, E> forEach) throws E {

    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2,
            ILongToObjectForEachMapKeyAndValueWithResult<V, P1, P2, R, E> forEach) throws E {

        return defaultResult;
    }

    @Override
    public final long keysAndValues(ILongAnyOrderAddable keysDst, IObjectAnyOrderAddable<V> valuesDst) {

        return 0L;
    }
}
