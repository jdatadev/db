package dev.jdata.db.utils.adt.elements;

import java.util.Objects;

import dev.jdata.db.utils.adt.IView;
import dev.jdata.db.utils.scalars.Integers;

public interface IByIndexOrderedElementsView extends IView, IByIndexOrderedElementsGetters {

    public static int intIndexLimit(IByIndexOrderedElementsView byIndexOrderedElementsView) {

        Objects.requireNonNull(byIndexOrderedElementsView);

        return intIndexLimitRenamed(byIndexOrderedElementsView.getIndexLimit());
    }

    public static int intIndexLimitRenamed(long indexLimit) {

        return Integers.checkUnsignedLongToUnsignedInt(indexLimit);
    }
}
