package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public interface ISingleHeadNodeListView extends INodeListView, ISingleHeadNodeListIterable, IOnlyElementsView, ISingleHeadNodeListGetters {

    @Override
    default long getNumIterableElements() {

        return getNumElements();
    }
}
