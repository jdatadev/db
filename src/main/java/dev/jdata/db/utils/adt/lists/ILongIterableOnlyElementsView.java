package dev.jdata.db.utils.adt.lists;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

public interface ILongIterableOnlyElementsView extends ILongIterableElementsView, IOnlyElementsView {

    @Override
    default boolean isEmpty() {

        return forEachWithResult(Boolean.TRUE, null, null, (e, p1, p2) -> Boolean.FALSE);
    }
}
