package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.scalars.Integers;

public interface IScatteredElementsView extends IContainsView, IScatteredElementsGetters {

    public static int intLimit(long limit) {

        return Integers.checkUnsignedLongToUnsignedInt(limit);
    }

    default boolean isZeroLimit() {

        return getLimit() == 0L;
    }
}
