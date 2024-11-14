package dev.jdata.db.utils.adt.maps;

import java.util.Map;
import java.util.Objects;

import dev.jdata.db.utils.adt.MutableElements;

public abstract class BaseIntCountMap<M extends Map<Integer, Integer>, C extends BaseCountMap<Integer, M>> implements MutableElements {

    @FunctionalInterface
    public interface ForEachElement<E extends Exception> {

        void each(int value, int count) throws E;
    }

    private final C delegate;

    BaseIntCountMap(C delegate) {

        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public final boolean isEmpty() {

        return delegate.isEmpty();
    }

    @Override
    public final int getNumElements() {

        return delegate.getNumElements();
    }

    @Override
    public final void clear() {

        delegate.clear();
    }

    public final void add(int value) {

        delegate.add(value);
    }

    public final void add(int[] values) {

        for (int value : values) {

            add(value);
        }
    }

    public final int getCount(int value) {

        return delegate.getCount(value);
    }

    public final <E extends Exception> void forEach(ForEachElement<E> forEachElement) throws E {

        Objects.requireNonNull(forEachElement);

        delegate.forEach((v, c) -> forEachElement.each(v, c));
    }
}
