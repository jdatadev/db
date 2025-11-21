package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.IObjectOrderedOnlyElementsView;

public interface IListView<T> extends IObjectOrderedOnlyElementsView<T>, IObjectHeadListGetters<T>, IObjectListGetters<T>, IObjectTailListGetters<T> {

}
