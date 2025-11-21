package dev.jdata.db.utils.adt.elements;

public interface IIntUnorderedOnlyElementsView extends IIntUnorderedElementsView, IOnlyElementsView {

    @Override
    default int[] toArray() {

        return IntElementsHelper.toArray(this, IElementsView.intNumElements(getNumElements()));
    }
}
