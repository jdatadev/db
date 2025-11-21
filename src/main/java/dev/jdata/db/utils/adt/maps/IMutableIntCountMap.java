package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsMutable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public interface IMutableIntCountMap extends IOnlyElementsView, IOnlyElementsMutable, IClearable {

    void add(int value);
    void add(int[] values);

    int getCount(int value);

    @FunctionalInterface
    public interface IForEachElement<E extends Exception> {

        void each(int value, int count) throws E;
    }

    <E extends Exception> void forEach(IForEachElement<E> forEachElement) throws E;
}
