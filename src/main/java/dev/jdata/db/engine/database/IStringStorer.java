package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IStringStorer extends IMutable, IStringStorerView, StringResolver, IClearable {

    public static IStringStorer create(int initialOuterCapacityExponent, int innerCapacityExponent) {

        Checks.isIntInitialOuterCapacityExponent(initialOuterCapacityExponent);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new StringStorer(AllocationType.HEAP, initialOuterCapacityExponent, innerCapacityExponent);
    }

    @Deprecated
    boolean remove(long stringRef);
}
