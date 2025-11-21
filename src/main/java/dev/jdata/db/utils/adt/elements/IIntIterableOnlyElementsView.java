package dev.jdata.db.utils.adt.elements;

public interface IIntIterableOnlyElementsView extends IIntIterableElementsView, IOnlyElementsView {

    @Override
    default int[] toArray() {

        return IntElementsHelper.toArrayIterable(this, IOnlyElementsView.intNumElements(this));
    }
}
