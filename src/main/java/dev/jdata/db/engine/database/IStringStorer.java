package dev.jdata.db.engine.database;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.function.CharPredicate;

public interface IStringStorer extends IClearable {

    boolean containsOnly(long stringRef, CharPredicate predicate);

    long toLowerCase(long stringRef);
    long getOrAddLowerCaseStringRef(long stringRef);

    long getOrAddStringRef(CharSequence charSequence, int offset, int length);

    long getOrAddStringRef(CharacterBuffer[] characterBuffers, int numCharacterBuffers);

    boolean contains(CharSequence charSequence, int offset, int length);

    @Deprecated
    boolean remove(long stringRef);

    default long getOrAddStringRef(CharSequence charSequence) {

        return getOrAddStringRef(charSequence, 0, charSequence.length());
    }

    default boolean contains(CharSequence charSequence) {

        return contains(charSequence, 0, charSequence.length());
    }
}
