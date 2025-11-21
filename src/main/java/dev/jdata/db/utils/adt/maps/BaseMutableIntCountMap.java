package dev.jdata.db.utils.adt.maps;

import java.util.Map;
import java.util.Objects;

import dev.jdata.db.utils.adt.maps.BaseMutableIntCountMap.ForEachElement;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseMutableIntCountMap<M extends Map<Integer, Integer>, C extends BaseMutableCountMap<Integer, M>> implements IMutableIntCountMap {

    private final C delegate;

    BaseMutableIntCountMap(C delegate) {

        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public final boolean isEmpty() {

        return delegate.isEmpty();
    }

    @Override
    public final void clear() {

        delegate.clear();
    }

    @Override
    public final long getCapacity() {

        return delegate.getCapacity();
    }

    @Override
    public final long getNumElements() {

        return delegate.getNumElements();
    }

    @Override
    public final void add(int value) {

        delegate.add(value);
    }

    @Override
    public final void add(int[] values) {

        for (int value : values) {

            add(value);
        }
    }

    @Override
    public final int getCount(int value) {

        return Integers.checkUnsignedLongToUnsignedInt(delegate.getCount(Integer.valueOf(value)));
    }

    @Override
    public final <E extends Exception> void forEach(IForEachElement<E> forEachElement) throws E {

        Objects.requireNonNull(forEachElement);

        delegate.forEach((v, c) -> forEachElement.each(v, c));
    }
}
