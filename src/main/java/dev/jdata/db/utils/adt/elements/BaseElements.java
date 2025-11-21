package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.byindex.IByIndexView;

public abstract class BaseElements {

    protected static int intIndex(long index) {

        return IByIndexView.intIndex(index);
    }

    protected static int intNumElements(long numElements) {

        return IElementsView.intNumElements(numElements);
    }
}
