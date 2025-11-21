package dev.jdata.db.utils.adt.elements;

public interface ILongUnorderedOnlyElementsView extends ILongUnorderedElementsView, IOnlyElementsView {

    @Override
    default long[] toArray() {

        return LongElementsHelper.toArray(this, IElementsView.intNumElements(getNumElements()));
    }
}
