package dev.jdata.db.engine.database;

import dev.jdata.db.utils.adt.marker.IGetters;
import dev.jdata.db.utils.function.CharPredicate;

interface IStringStorerGetters extends IGetters {

    boolean contains(CharSequence charSequence, int offset, int length);

    boolean containsOnly(long stringRef, CharPredicate predicate);

    default boolean contains(CharSequence charSequence) {

        return contains(charSequence, 0, charSequence.length());
    }
}
