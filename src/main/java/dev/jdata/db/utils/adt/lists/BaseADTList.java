package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.BaseADTElements;
import dev.jdata.db.utils.adt.elements.IByIndexOrderedElementsView;

abstract class BaseADTList extends BaseADTElements implements IByIndexOrderedElementsView {

    BaseADTList(AllocationType allocationType) {
        super(allocationType);
    }

    @Override
    public final long getIndexLimit() {

        return getNumElements();
    }
}
