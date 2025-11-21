package dev.jdata.db.utils.adt.elements;

public interface IByIndexOrderedScatteredElementsView extends IByIndexOrderedElementsView, IScatteredElementsView {

    @Override
    default long getIndexLimit() {

        return getLimit();
    }
}
