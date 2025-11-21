package dev.jdata.db.utils.adt.elements;

public interface IByIndexOrderedOnlyElementsView extends IByIndexOrderedElementsView, IOnlyElementsView {

    @Override
    default long getIndexLimit() {

        return getNumElements();
    }
}
