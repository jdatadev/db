package dev.jdata.db.utils.adt.elements;

public interface ILongIterableOnlyElementsView extends ILongIterableElementsView, IOnlyElementsView {

    @Override
    default long[] toArray() {

        return LongElementsHelper.toArrayIterable(this, IOnlyElementsView.intNumElements(this));
    }
}
