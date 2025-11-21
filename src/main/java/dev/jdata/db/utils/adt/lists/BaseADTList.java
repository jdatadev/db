package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IByIndexOrderedOnlyElementsView;

abstract class BaseADTList<T, U, V> extends BaseADTElements<T, U, V> implements IByIndexOrderedOnlyElementsView {

    BaseADTList(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final long getIndexLimit() {

        return getNumElements();
    }
}
