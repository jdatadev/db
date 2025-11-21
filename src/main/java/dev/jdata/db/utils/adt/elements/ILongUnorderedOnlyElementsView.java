package dev.jdata.db.utils.adt.elements;

public interface ILongUnorderedOnlyElementsView extends ILongUnorderedElementsView, ILongIterableOnlyElementsView {

    @Override
    default long[] toArray() {

        return LongElementsHelper.toArrayIterable(this, IOnlyElementsView.intNumElements(this));
    }
}
