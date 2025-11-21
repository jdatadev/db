package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.scalars.Integers;

public interface IOnlyElementsView extends IElementsView, IOnlyElementsGetters {

    public static int intNumElementsRenamed(long numElements) {

        return Integers.checkUnsignedLongToUnsignedInt(numElements);
    }

    public static int intNumElements(IOnlyElementsView onlyElementsView) {

        Objects.requireNonNull(onlyElementsView);

        return intNumElementsRenamed(onlyElementsView.getNumElements());
    }

    @Override
    default boolean isEmpty() {

        return getNumElements() == 0L;
    }
}
