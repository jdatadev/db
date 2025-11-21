package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IByIndexOrderedElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class BaseADTList<T, U, V> extends BaseADTElements<T, U, V> implements IByIndexOrderedElementsView, IOnlyElementsView {

    BaseADTList(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final long getIndexLimit() {

        return getNumElements();
    }
}
