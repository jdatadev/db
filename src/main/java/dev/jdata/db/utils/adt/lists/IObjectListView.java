package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectOrderedElementsView;

public interface IObjectListView<T> extends IObjectOrderedElementsView<T>, IObjectHeadListGetters<T>, IObjectListGetters<T>, IObjectTailListGetters<T> {

}
