package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;

abstract class BaseMutableIndexListAllocator<

                T extends IMutableElements & IMutableListType & IByIndexView,
                U extends BaseADTElements<?, ?, ?> & IMutableListType & IByIndexView,
                V>

        extends BaseMutableListAllocator<T, U, V> {

    BaseMutableIndexListAllocator(IntFunction<V> createElements) {
        super(createElements);
    }
}
