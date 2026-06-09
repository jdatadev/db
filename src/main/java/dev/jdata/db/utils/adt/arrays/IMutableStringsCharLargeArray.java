package dev.jdata.db.utils.adt.arrays;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

public interface IMutableStringsCharLargeArray extends IMutableOneDimensionalArray, IStringsCharLargeArrayGetters {

    void add(CharacterBuffer[] characterBuffers, int numCharacterBuffers);
    void add(CharSequence charSequence, int offset, int length);
}
