package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IContainsView;

public interface IOnlyElementsView extends IContainsView, IOnlyElementsGetters {

    @Override
    default boolean isEmpty() {

        return getNumElements() == 0L;
    }
}
