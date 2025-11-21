package dev.jdata.db.utils.adt.elements;

public interface IObjectOrderedOnlyElementsView<T> extends IObjectOrderedElementsView<T>, IOnlyElementsView {

    @Override
    default boolean isEmpty() {

        return forEachWithResult(Boolean.TRUE, null, null, (e, p1, p2) -> Boolean.FALSE);
    }
}
