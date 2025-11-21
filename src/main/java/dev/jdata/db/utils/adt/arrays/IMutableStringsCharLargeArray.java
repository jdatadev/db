package dev.jdata.db.utils.adt.arrays;

import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;
import org.jutils.io.strings.StringResolver.ICharactersToString;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.function.CharPredicate;

public interface IMutableStringsCharLargeArray extends IMutable, IClearable {

    long getLimit();

    char get(long index);

    String asString(long index);
    void asString(long index, StringBuilder sb);

    <P, R, E extends Exception> R makeString(long index, P parameter, ICharactersBufferAllocator charactersBufferAllocator, ICharactersToString<P, R, E> charactersToString)
            throws E;

    long getStringLength(long index);

    boolean containsOnlyCharacters(long index, CharPredicate predicate);

    boolean equals(long index, IMutableStringsCharLargeArray otherCharArray, long otherIndex);
    boolean equals(long index, IMutableStringsCharLargeArray otherCharArray, long otherIndex, boolean caseSensitive);

    boolean matches(long index, CharSequence charSequence, int offset, int length, boolean caseSensitive);
    boolean matches(long index, CharacterBuffer[] characterBuffers, int numCharacterBuffers, boolean caseSensitive);

    void add(CharacterBuffer[] characterBuffers, int numCharacterBuffers);
    void add(CharSequence charSequence, int offset, int length);
}
