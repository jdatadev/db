package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.scalars.Integers;

public interface IElementsView extends IContainsView {

    public static int intNumElements(long numElements) {

        return Integers.checkUnsignedLongToUnsignedInt(numElements);
    }
}
