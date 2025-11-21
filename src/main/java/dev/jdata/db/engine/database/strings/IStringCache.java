package dev.jdata.db.engine.database.strings;

import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;

public interface IStringCache extends IMutable {

    public static IStringCache create(int initialOuterCapacityExponent, int innerCapacityExponent) {

        Checks.isIntInitialOuterCapacityExponent(initialOuterCapacityExponent);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);

        return new BucketsStringCache(AllocationType.HEAP, initialOuterCapacityExponent, innerCapacityExponent);
    }

    boolean contains(CharSequence charSequence);

    String getOrAddString(CharSequence charSequence, int startIndex, int numCharacters);

    default String getOrAddString(CharSequence charSequence) {

        return getOrAddString(charSequence, 0, charSequence.length());
    }

    String getOrAddString(int i);
}
