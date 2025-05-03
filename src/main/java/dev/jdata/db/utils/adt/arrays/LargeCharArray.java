package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;

import org.jutils.io.strings.StringResolver.CharacterBuffer;

import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharPredicate;

public final class LargeCharArray extends LargeLimitArray<char[][], char[]> {

    private static final char TERMINATOR_CHAR = '\0';

    public LargeCharArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent, 0, char[][]::new);
    }

    public boolean containsOnly(long index, CharPredicate predicate) {

        Objects.checkIndex(index, getLimit());

        boolean containsOnly = false;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerIndex(index);

        final char[][] buffers = getArray();

        char[] buffer = buffers[bufferNo];

        final int innerNumAllocateElements = getInnerNumAllocateElements();

        for (;;) {

            final char c = buffer[bufferOffset];

            if (!predicate.test(c)) {

                containsOnly = false;
                break;
            }
            else if (c == TERMINATOR_CHAR) {

                break;
            }

            if (bufferOffset == innerNumAllocateElements) {

                ++ bufferNo;
                bufferOffset = getInnerArrayLengthNumElements();
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        return containsOnly;
    }

    public long getStringLength(long index) {

        Objects.checkIndex(index, getLimit());

        long length = 0L;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerIndex(index);

        final char[][] buffers = getArray();

        char[] buffer = buffers[bufferNo];

        final int innerNumAllocateElements = getInnerNumAllocateElements();

        for (;;) {

            final char c = buffer[bufferOffset];

            if (c == TERMINATOR_CHAR) {

                break;
            }

            ++ length;

            if (bufferOffset == innerNumAllocateElements) {

                ++ bufferNo;
                bufferOffset = getInnerArrayLengthNumElements();
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        return length;
    }

    public boolean matches(long index, CharSequence charSequence, int offset, int length, boolean caseSensitive) {

        Objects.checkIndex(index, getLimit());
        Objects.checkFromIndexSize(offset, length, charSequence.length());

        boolean equals = true;

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerIndex(index);

        final char[][] buffers = getArray();

        char[] buffer = buffers[bufferNo];

        final int afterArrayOffset= getInnerNumAllocateElements() - 1;

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

            if (bufferOffset == afterArrayOffset) {

                ++ bufferNo;
                bufferOffset = getInnerArrayLengthNumElements();
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        return equals;
    }

    public boolean matches(long index, CharacterBuffer[] characterBuffers, int numCharacterBuffers, boolean caseSensitive) {

        Objects.checkIndex(index, getLimit());
        Checks.isNotEmpty(characterBuffers);
        Checks.isLengthAboveZero(numCharacterBuffers);

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
    public void toString(long index, StringBuilder sb) {

        sb.append(get(index));
    }

    public char get(long index) {

        Objects.checkIndex(index, getLimit());

        return getArray()[getOuterIndex(index)][getInnerIndex(index)];
    }

    public void add(CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        Checks.isNotEmpty(characterBuffers);
        Checks.isLengthAboveZero(numCharacterBuffers);

        for (int i = 0; i < numCharacterBuffers; ++ i) {

            final CharacterBuffer characterBuffer = characterBuffers[i];

            add(characterBuffer, characterBuffer.getOffset(), characterBuffer.getLength());
        }
    }

    public void add(CharSequence charSequence, int offset, int length) {

        Objects.requireNonNull(charSequence);
        Objects.checkFromIndexSize(offset, length, charSequence.length());

        checkCapacity(length + 1);

        final long index = getLimit();

        int bufferNo = getOuterIndex(index);
        int bufferOffset = getInnerIndex(index);

        final char[][] buffers = getArray();

        char[] buffer = buffers[bufferNo];

        final int afterArrayOffset= getInnerNumAllocateElements() - 1;

        for (int i = 0; i < length; ++ i) {

            buffer[bufferOffset] = charSequence.charAt(offset + i);

            if (bufferOffset == afterArrayOffset) {

                ++ bufferNo;
                bufferOffset = getInnerArrayLengthNumElements();
                buffer = buffers[bufferNo];
            }
            else {
                ++ bufferOffset;
            }
        }

        buffer[bufferOffset] = TERMINATOR_CHAR;

        increaseLimit(length + 1);
    }

    public void add(char value) {

        final char[] array = checkCapacity();

        final long index = getAndIncrementLimit();

        array[getInnerIndex(index)] = value;
    }

    public void set(long index, char value) {

        Checks.isIndex(index);

        final int outerIndex = ensureCapacityAndLimit(index);

        getArray()[outerIndex][getInnerIndex(index)] = value;
    }

    @Override
    char[][] copyOuterArray(char[][] outerArray, int capacity) {

        return Arrays.copyOf(outerArray, capacity);
    }

    @Override
    int getOuterArrayLength(char[][] outerArray) {

        return outerArray.length;
    }

    @Override
    char[] getInnerArray(char[][] outerArray, int index) {

        return outerArray[index];
    }

    @Override
    int getInnerArrayLength(char[] innerArray) {

        return innerArray.length;
    }

    @Override
    void setInnerArrayLength(char[] innerArray, int length) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getNumInnerElements(char[] innerArray) {

        throw new UnsupportedOperationException();
    }

    @Override
    char[] setInnerArray(char[][] outerArray, int outerIndex, int innerArrayLength) {

        return outerArray[outerIndex] = new char[innerArrayLength];
    }

    private char[] checkCapacity() {

        return checkCapacity(null, null);
    }

    private void checkCapacity(long numAdditional) {

        checkCapacity(numAdditional, null, null);
    }

    private int ensureCapacityAndLimit(long index) {

        return ensureCapacityAndLimit(index, null, null);
    }
}
