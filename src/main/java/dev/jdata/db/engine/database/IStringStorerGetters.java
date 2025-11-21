package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.utils.adt.marker.IGetters;
import dev.jdata.db.utils.function.CharPredicate;

interface IStringStorerGetters extends IGetters {

    boolean containsOnly(long stringRef, CharPredicate predicate);

    long toLowerCase(long stringRef);
    long getOrAddLowerCaseStringRef(long stringRef);

    long getOrAddStringRef(CharSequence charSequence, int offset, int length);

    long getOrAddStringRef(CharacterBuffer[] characterBuffers, int numCharacterBuffers);

    boolean contains(CharSequence charSequence, int offset, int length);

    default long getOrAddStringRef(CharSequence charSequence) {

        return getOrAddStringRef(charSequence, 0, charSequence.length());
    }

    default boolean contains(CharSequence charSequence) {

        return contains(charSequence, 0, charSequence.length());
    }
}
