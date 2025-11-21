package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.IView;
import dev.jdata.db.utils.scalars.Integers;

public interface IByIndexOrderedElementsView extends IView, IByIndexOrderedElementsGetters {

    public static int intIndexLimit(long indexLimit) {

        return Integers.checkUnsignedLongToUnsignedInt(indexLimit);
    }
}
