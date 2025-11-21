package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.ICharactersBufferAllocator;
import org.jutils.io.strings.StringResolver.ICharactersToString;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.maps.Maps;
import dev.jdata.db.utils.adt.maps.Maps.ILongAppendEachValue;
import dev.jdata.db.utils.adt.maps.Maps.ILongForEachAppendCaller;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.jdk.adt.strings.Characters;
import dev.jdata.db.utils.scalars.Integers;

abstract class MutableStringsCharLargeArray extends BaseMutableCharLargeArray implements IMutableStringsCharLargeArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_STRINGS_CHAR_LARGE_ARRAY;

    private static final Class<?> debugClass = MutableStringsCharLargeArray.class;

    private static final char TERMINATOR_CHAR = '\0';

    @FunctionalInterface
    private interface IForEachStringIndex<P> {

        void each(long index, MutableStringsCharLargeArray largeCharArray, P parameter);
    }

    MutableStringsCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    MutableStringsCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, char clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    @Override
    public final String asString(long index) {

        Checks.checkLongIndex(index, getLimit());

        final long length = getStringLength(index);

        final int sbCapacity = Integers.checkUnsignedLongToUnsignedInt(length);

        final StringBuilder sb = new StringBuilder(sbCapacity);

        asString(index, sb);

        return sb.toString();
    }

    @Override
    public final void asString(long index, StringBuilder sb) {

        Checks.checkLongIndex(index, getLimit());
        Objects.requireNonNull(sb);

        final long length = getStringLength(index);

        final int sbCapacity = sb.length() + Integers.checkUnsignedLongToUnsignedInt(length);

        sb.ensureCapacity(sbCapacity);

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        final char[][] buffers = getOuterArray();

        char[] buffer = buffers[bufferNo];

        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerNumAllocateElements()) - 1;

        for (;;) {

            final char c = buffer[bufferOffset];

            if (c == TERMINATOR_CHAR) {

                break;
            }

            sb.append(c);

            if (bufferOffset == endBufferOffset) {

                ++ bufferNo;
                bufferOffset = 0;
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }
    }

    @Override
    public final <P, R, E extends Exception> R makeString(long index, P parameter, ICharactersBufferAllocator charactersBufferAllocator,
            ICharactersToString<P, R, E> charactersToString) throws E {

        Checks.checkLongIndex(index, getLimit());
        Objects.requireNonNull(charactersBufferAllocator);
        Objects.requireNonNull(charactersToString);

        final long length = getStringLength(index);

        if (length <= 0L) {

            throw new IllegalStateException();
        }

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        final char[][] buffers = getOuterArray();

        char[] buffer = buffers[bufferNo];

        CharacterBuffer[] characterBuffers = charactersBufferAllocator.allocateCharacterBuffers(2);

        int characterBuffersOffset = 0;

        int offset = bufferOffset;

        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerElementCapacity()) - 1;

        long remaining = length;

        do {
            -- remaining;

            if (bufferOffset == endBufferOffset) {

                characterBuffers[characterBuffersOffset].initialize(buffer, offset, bufferOffset - offset + 1);

                ++ bufferNo;
                bufferOffset = 0;
                offset = 0;
                buffer = buffers[bufferNo];

                final int characterBuffersLength = characterBuffers.length;

                if (remaining != 0L) {

                    if (characterBuffersOffset == characterBuffersLength - 1) {

                        characterBuffers = charactersBufferAllocator.reallocateCharacterBuffers(characterBuffers, characterBuffersLength << 1);
                    }

                    ++ characterBuffersOffset;
                }
            }
            else {
                ++ bufferOffset;
            }
        }
        while (remaining != 0L);

        if (bufferOffset > offset) {

            characterBuffers[characterBuffersOffset].initialize(buffer, offset, bufferOffset - offset);
        }

        return charactersToString.makeString(characterBuffers, characterBuffersOffset + 1, parameter);
    }

    @Override
    public final long getStringLength(long index) {

        Checks.checkLongIndex(index, getLimit());

        long length = 0L;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        final char[][] buffers = getOuterArray();

        char[] buffer = buffers[bufferNo];

        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerNumAllocateElements()) - 1;

        for (;;) {

            final char c = buffer[bufferOffset];

            if (c == TERMINATOR_CHAR) {

                break;
            }

            ++ length;

            if (bufferOffset == endBufferOffset) {

                ++ bufferNo;
                bufferOffset = 0;
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        return length;
    }

    @Override
    public final boolean containsOnlyCharacters(long index, CharPredicate predicate) {

        Checks.checkLongIndex(index, getLimit());

        boolean containsOnly = true;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        final char[][] buffers = getOuterArray();

        char[] buffer = buffers[bufferNo];

        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerNumAllocateElements()) - 1;

        for (;;) {

            final char c = buffer[bufferOffset];

            if (c == TERMINATOR_CHAR) {

                break;
            }
            else if (!predicate.test(c)) {

                containsOnly = false;
                break;
            }

            if (bufferOffset == endBufferOffset) {

                ++ bufferNo;
                bufferOffset = 0;
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        return containsOnly;
    }

    @Override
    public final boolean equals(long index, IMutableStringsCharLargeArray otherCharArray, long otherIndex) {

        return equals(index, otherCharArray, otherIndex, true);
    }

    @Override
    public final boolean equals(long index, IMutableStringsCharLargeArray otherStringsCharLargeArray, long otherIndex, boolean caseSensitive) {

        Checks.checkLongIndex(index, getLimit());

        final MutableStringsCharLargeArray otherCharArray = (MutableStringsCharLargeArray)otherStringsCharLargeArray;

        Checks.checkLongIndex(otherIndex, otherCharArray.getLimit());

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        int otherBufferNo = getOuterIndex(otherIndex);
        int otherBufferOffset = getInnerElementIndex(otherIndex);

        final char[][] buffers = getOuterArray();
        char[] buffer = buffers[bufferNo];
        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerNumAllocateElements()) - 1;

        final char[][] otherBuffers = otherCharArray.getOuterArray();
        char[] otherBuffer = otherBuffers[otherBufferNo];
        final int otherEndBufferOffset = Integers.checkUnsignedLongToUnsignedInt(otherCharArray.getInnerNumAllocateElements()) - 1;

        boolean equals = true;

        for (;;) {

            final char c = buffer[bufferOffset];
            final char otherC = otherBuffer[otherBufferOffset];

            if (c == TERMINATOR_CHAR) {

                equals = otherC == TERMINATOR_CHAR;
                break;
            }
            else if (otherC == TERMINATOR_CHAR) {

                equals = c == TERMINATOR_CHAR;
                break;
            }
            else if (c == otherC || (!caseSensitive && Characters.areEqualCaseInsensitive(c, otherC))) {

                if (bufferOffset == endBufferOffset) {

                    ++ bufferNo;
                    bufferOffset = 0;
                    buffer = buffers[bufferNo];
                }
                else {
                    ++ bufferOffset;
                }

                if (otherBufferOffset == otherEndBufferOffset) {

                    ++ otherBufferNo;
                    otherBufferOffset = 0;
                    otherBuffer = otherBuffers[otherBufferNo];
                }
                else {
                    ++ otherBufferOffset;
                }
            }
            else {
                equals = false;
                break;
            }
        }

        return equals;
    }

    @Override
    public final boolean matches(long index, CharSequence charSequence, int offset, int length, boolean caseSensitive) {

        Checks.checkLongIndex(index, getLimit());
        Checks.checkFromIndexSize(offset, length, charSequence.length());

        boolean equals = true;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        final char[][] buffers = getOuterArray();

        char[] buffer = buffers[bufferNo];

        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerNumAllocateElements()) - 1;

        for (int i = 0; i < length; ++ i) {

            final char c = buffer[bufferOffset];

            final char sequenceChar = charSequence.charAt(offset + i);

            if (caseSensitive) {

                if (c != sequenceChar) {

                    equals = false;
                    break;
                }
            }
            else {
                if (Character.toUpperCase(c) != Character.toUpperCase(sequenceChar)) {

                    equals = false;
                    break;
                }
            }

            if (bufferOffset == endBufferOffset) {

                ++ bufferNo;
                bufferOffset = 0;
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        return equals;
    }

    @Override
    public final boolean matches(long index, CharacterBuffer[] characterBuffers, int numCharacterBuffers, boolean caseSensitive) {

        Checks.checkLongIndex(index, getLimit());
        Checks.isNotEmpty(characterBuffers);
        Checks.isIntLengthAboveZero(numCharacterBuffers);

        boolean equals = true;

        long currentIndex = index;

        for (int i = 0; i < numCharacterBuffers; ++ i) {

            final CharacterBuffer characterBuffer = characterBuffers[i];

            final int characterBufferLength = characterBuffer.getLength();

            if (!matches(currentIndex, characterBuffer, characterBuffer.getOffset(), characterBufferLength, caseSensitive)) {

                equals = false;
                break;
            }

            currentIndex += characterBufferLength;
        }

        return equals;
    }

    @Override
    public final void add(CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        Checks.isNotEmpty(characterBuffers);
        Checks.isIntLengthAboveZero(numCharacterBuffers);

        if (Array.sum(characterBuffers, CharacterBuffer::length) < 1) {

            throw new IllegalArgumentException();
        }

        for (int i = 0; i < numCharacterBuffers; ++ i) {

            final CharacterBuffer characterBuffer = characterBuffers[i];

            final int length = characterBuffer.getLength();

            if (length != 0) {

                add(characterBuffer, characterBuffer.getOffset(), length, i == numCharacterBuffers - 1);
            }
        }
    }

    @Override
    public final void add(CharSequence charSequence, int offset, int length) {

        add(charSequence, offset, length, true);
    }

    private void add(CharSequence charSequence, int offset, int length, boolean addTerminator) {

        Objects.requireNonNull(charSequence);
        Checks.checkFromIndexSize(offset, length, charSequence.length());
        Checks.isIntLengthAboveZero(length);

        final int numAdditional = length + 1;
        final long limit = getLimit();

        checkCapacityWithNewLimit(limit, limit + numAdditional, shouldClear());

        final long index = limit;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerElementIndex(index);

        final char[][] buffers = getOuterArray();

        char[] buffer = buffers[bufferNo];

        final int endBufferOffset = Integers.checkUnsignedLongToUnsignedInt(getInnerNumAllocateElements()) - 1;

        for (int i = 0; i < length; ++ i) {

            buffer[bufferOffset] = charSequence.charAt(offset + i);

            if (bufferOffset == endBufferOffset) {

                ++ bufferNo;
                bufferOffset = 0;
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        final int limitIncrease;

        if (addTerminator) {

            buffer[bufferOffset] = TERMINATOR_CHAR;

            limitIncrease = length + 1;
        }
        else {
            limitIncrease = length;
        }

        increaseLimit(limitIncrease);
    }

    private <P> void forEachIndexAndString(P parameter, IForEachStringIndex<P> forEachStringIndex) {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachStringIndex", forEachStringIndex));
        }

        final long num = getLimit();

        long stringIndex = 0L;

        for (long i = stringIndex; i < num; ++ i) {

            final char c = get(i);

            if (c == TERMINATOR_CHAR) {

                forEachStringIndex.each(stringIndex, this, parameter);

                stringIndex = i + 1;
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEachStringIndex", forEachStringIndex));
        }
    }

    @Override
    public final String toString() {

        final ILongForEachAppendCaller<MutableStringsCharLargeArray, MutableStringsCharLargeArray> forEachAppendCaller
                = (b, i, f) -> i.forEachIndexAndString(f, (index, charArray, forEach) -> forEach.each(index, charArray, b, null));

        final ILongAppendEachValue<MutableStringsCharLargeArray, MutableStringsCharLargeArray> appendEachValue = (i, a, b, p) -> a.asString(i, b);

        return Maps.longAppendToString(getClass().getSimpleName(), getLimit(), this, forEachAppendCaller, appendEachValue);
    }
}
