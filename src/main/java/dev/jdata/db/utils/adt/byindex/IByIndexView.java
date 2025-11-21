package dev.jdata.db.utils.adt.byindex;

import dev.jdata.db.utils.scalars.Integers;

public interface IByIndexView extends IByIndexToStringView {

    public static int intIndex(long index) {

        return Integers.checkUnsignedLongToUnsignedInt(index);
    }
}
