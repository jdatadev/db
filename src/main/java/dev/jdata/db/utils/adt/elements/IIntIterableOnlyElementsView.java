package dev.jdata.db.utils.adt.elements;

public interface IIntIterableOnlyElementsView extends IIntIterableElementsView, IOnlyElementsView {

    @Override
    default boolean isEmpty() {

        return forEachWithResult(Boolean.TRUE, null, null, (e, p1, p2) -> Boolean.FALSE);
    }
}
