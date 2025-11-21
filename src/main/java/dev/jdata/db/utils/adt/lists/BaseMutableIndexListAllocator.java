package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.byindex.IByIndexView;
import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IMutableElements;
import dev.jdata.db.utils.adt.elements.IMutableFrom;

abstract class BaseMutableIndexListAllocator<

                INTERFACE_MUTABLE extends IMutableElements & IMutableListType & IByIndexView,
                CLASS_MUTABLE extends BaseADTElements<?, ?, ?> & IMutableListType & IByIndexView,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends BaseMutableListAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM> {

    BaseMutableIndexListAllocator(IntFunction<ELEMENTS> createElements) {
        super(createElements);
    }
}
