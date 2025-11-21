package dev.jdata.db.utils.adt.elements;

public interface IByIndexOrderedOnlyElementsView extends IByIndexOrderedElementsView, IIterableOnlyElementsView {

    @Override
    default long getIndexLimit() {

        return getNumElements();
    }
}
