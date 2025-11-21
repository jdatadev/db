package dev.jdata.db.utils.adt.elements;

public interface IIterableOnlyElementsView extends IOnlyElementsView, IElementsIterable {

    @Override
    default long getNumIterableElements() {

        return getNumElements();
    }
}
