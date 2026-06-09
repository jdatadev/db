package dev.jdata.db.utils.adt.arrays;

import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;
import org.jutils.io.strings.StringResolver.ICharactersToString;

import dev.jdata.db.utils.adt.elements.ICharForEach2;
import dev.jdata.db.utils.function.CharPredicate;

interface IStringsCharLargeArrayGetters extends IArrayGettersMarker {

    <P1, P2, E extends Exception> void forEach(long index, P1 parameter1, P2 parameter2, ICharForEach2<P1, P2, E> forEach) throws E;

    char charAt(long index);

    String asString(long index);
    void asString(long index, StringBuilder sb);

    <P, R, E extends Exception> R makeString(long index, P parameter, ICharactersBufferAllocator charactersBufferAllocator, ICharactersToString<P, R, E> charactersToString)
            throws E;

    long getStringLength(long index);

    boolean containsOnlyCharacters(long index, CharPredicate predicate);

    boolean equals(long index, IStringsCharLargeArrayGetters otherCharArray, long otherIndex, boolean caseSensitive);

    boolean matches(long index, CharSequence charSequence, int offset, int length, boolean caseSensitive);
    boolean matches(long index, CharacterBuffer[] characterBuffers, int numCharacterBuffers, boolean caseSensitive);

    default boolean equals(long index, IStringsCharLargeArrayGetters otherCharArray, long otherIndex) {

        return equals(index, otherCharArray, otherIndex, true);
    }
}
