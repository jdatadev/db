package dev.jdata.db.utils.adt.elements;

public interface IIntUnorderedOnlyElementsView extends IIntUnorderedElementsView, IIntIterableOnlyElementsView {

    @Override
    default int[] toArray() {

        return IntElementsHelper.toArrayIterable(this, IOnlyElementsView.intNumElements(this));
    }
}
