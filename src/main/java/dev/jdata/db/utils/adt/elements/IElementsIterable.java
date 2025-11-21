package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.marker.IIterable;

public interface IElementsIterable extends IIterable {

    public static int intNumIterableElements(IElementsIterable elementsIterable) {

        Objects.requireNonNull(elementsIterable);

        return IOnlyElementsView.intNumElementsRenamed(elementsIterable.getNumIterableElements());
    }

    long getNumIterableElements();
}
