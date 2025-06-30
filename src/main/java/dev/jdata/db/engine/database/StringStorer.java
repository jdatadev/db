package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeCharArray;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.adt.maps.BaseLargeArrayMap;
import dev.jdata.db.utils.adt.maps.Maps;
import dev.jdata.db.utils.adt.maps.Maps.ILongAppendEachValue;
import dev.jdata.db.utils.adt.maps.Maps.ILongForEachAppendCaller;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharMapper;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.scalars.Integers;

public final class StringStorer extends BaseLargeArrayMap<LargeLongArray, LargeLongArray> implements IStringStorer, StringResolver {

    private static final boolean DEBUG = DebugConstants.DEBUG_STRING_STORER;

    private static final long NO_ELEMENT = -1L;

    private static final LongNodeSetter<StringStorer> headSetter = (i, h) -> i.getHashed().set(i.scratchHashArrayIndex, h);
    private static final LongNodeSetter<StringStorer> tailSetter = (i, t) -> { };

    private final LargeCharArray charArray;
    private final LargeCharArrayCharSequence scratchLargeCharArrayCharSequence;

    private LargeLongMultiHeadSinglyLinkedList<StringStorer> buckets;

    private final CharacterBuffers scratchCharacterBuffers;

    private long scratchHashArrayIndex;
    private CharSequence scratchCharSequence;
    private int scratchOffset;
    private int scratchLength;

    private long keyMask;

    public StringStorer(int initialOuterCapacity, int innerCapacityExponent) {
        this(initialOuterCapacity, innerCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public StringStorer(int initialOuterCapacity, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, (o, i) -> new LargeLongArray(o, i, NO_ELEMENT), LargeLongArray::clear,
                (o, i) -> new LargeLongArray(o, i, null));

        this.charArray = new LargeCharArray(initialOuterCapacity, innerCapacityExponent);
        this.scratchLargeCharArrayCharSequence = new LargeCharArrayCharSequence();
        this.scratchCharacterBuffers = new CharacterBuffers();

        this.buckets = createBuckets(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    public void clear() {

        clearHashed();

        charArray.clear();
        buckets.clear();
    }

    @Override
    public boolean contains(CharSequence charSequence, int offset, int length) {

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        final boolean found;

        if (isEmpty()) {

            found = false;
        }
        else {
            final long hash = getHashCode(charSequence, offset, length);

            final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

            final LargeLongArray keys = getHashed();

            final long bucketHeadNode = keys.get(hashArrayIndex);

            this.scratchCharSequence = charSequence;
            this.scratchOffset = offset;
            this.scratchLength = length;

            found = bucketHeadNode != BaseList.NO_NODE && buckets.contains(bucketHeadNode, this,
                    (v, i) -> i.charArray.matches(v, i.scratchCharSequence, i.scratchOffset, i.scratchLength, false));
        }

        if (DEBUG) {

            exit(found, b -> b.add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        return found;
    }

    @Override
    public boolean containsOnly(long stringRef, CharPredicate predicate) {

        StringRef.checkIsString(stringRef);
        Objects.requireNonNull(predicate);

        if (DEBUG) {

            enter(b -> b.add("stringRef", stringRef).add("predicate", predicate));
        }

        final boolean result = charArray.containsOnly(stringRef, predicate);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public long toLowerCase(long stringRef) {

        if (DEBUG) {

            enter(b -> b.add("stringRef", stringRef));
        }

        this.scratchCharSequence = scratchLargeCharArrayCharSequence;

        final int length = Integers.checkUnsignedLongToUnsignedInt(charArray.getStringLength(stringRef));

        scratchLargeCharArrayCharSequence.initialize(charArray, stringRef, length, (c, p) -> Character.toLowerCase(c));

        final long result = addNotAlreadyAdded(getHashed(), getValues(), buckets, scratchLargeCharArrayCharSequence, 0, length);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public long getOrAddLowerCaseStringRef(long stringRef) {

        if (DEBUG) {

            enter(b -> b.add("stringRef", stringRef));
        }

        this.scratchCharSequence = scratchLargeCharArrayCharSequence;

        final int length = Integers.checkUnsignedLongToUnsignedInt(charArray.getStringLength(stringRef));

        scratchLargeCharArrayCharSequence.initialize(charArray, stringRef, length, (c, p) -> Character.toLowerCase(c));

        final long result = getOrAddStringRef(scratchLargeCharArrayCharSequence);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public long getOrAddStringRef(CharSequence charSequence, int offset, int length) {

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        final long result = getOrAdd(getHashed(), getValues(), buckets, charSequence, offset, length);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public long getOrAddStringRef(CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        Checks.isNotEmpty(characterBuffers);
        Checks.isLengthAboveZero(numCharacterBuffers);

        if (DEBUG) {

            enter(b -> b.add("characterBuffers", characterBuffers).add("numCharacterBuffers", numCharacterBuffers));
        }

        final long result;

        switch (numCharacterBuffers) {

        case 0:
            throw new IllegalArgumentException();

        case 1:

            final CharacterBuffer characterBuffer = characterBuffers[0];

            result = getOrAdd(getHashed(), getValues(), buckets, characterBuffer, characterBuffer.getOffset(), characterBuffer.getLength());
            break;

        default:

            final CharacterBuffers buffers = scratchCharacterBuffers;

            buffers.initialize(characterBuffers, numCharacterBuffers);

            result = getOrAdd(getHashed(), getValues(), buckets, buffers, 0, buffers.length());
            break;
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public boolean remove(long stringRef) {

        Checks.isNotNegative(stringRef);

        if (DEBUG) {

            enter(b -> b.add("element", stringRef));
        }

        if (Boolean.TRUE) {

            throw new UnsupportedOperationException();
        }

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(stringRef, getKeyMask());

        final long bucketHeadNode = getHashed().get(hashArrayIndex);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d stringRef=%d keyMask=0x%08x bucketHeadNode=0x%016x", hashArrayIndex, stringRef, getKeyMask(), bucketHeadNode);
        }

        final boolean removed;

        if (bucketHeadNode == BaseList.NO_NODE) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;

            removed = buckets.removeAtMostOneNodeByValue(this, stringRef, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter) != BaseList.NO_NODE;
        }

        if (removed) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    @Override
    public String asString(long stringRef) {

        StringRef.checkIsString(stringRef);

        if (DEBUG) {

            enter(b -> b.add("stringRef", stringRef));
        }

        final String result = charArray.asString(stringRef);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public void asString(long stringRef, StringBuilder sb) {

        StringRef.checkIsString(stringRef);
        Objects.requireNonNull(sb);

        if (DEBUG) {

            enter(b -> b.add("stringRef", stringRef).add("sb", sb));
        }

        charArray.asString(stringRef, sb);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public boolean equals(long stringRef, StringResolver otherStringResolver, long otherStringRef, boolean caseSensitive) {

        Checks.areNotSame(this, otherStringResolver);

        if (!(otherStringResolver instanceof StringStorer)) {

            throw new IllegalArgumentException();
        }

        final StringStorer other = (StringStorer)otherStringResolver;

        return charArray.equals(stringRef, other.charArray, otherStringRef, caseSensitive);
    }

    @Override
    public <P, R, E extends Exception> R makeString(long stringRef, P parameter, ICharactersBufferAllocator charactersBufferAllocator,
            CharactersToString<P, R, E> charactersToString) throws E {

        StringRef.checkIsString(stringRef);
        Objects.requireNonNull(charactersBufferAllocator);
        Objects.requireNonNull(charactersToString);

        if (DEBUG) {

            enter(b -> b.add("stringRef", stringRef).add("parameter", parameter).add("charactersBufferAllocator", charactersBufferAllocator)
                    .add("charactersToString", charactersToString));
        }

        final R result = charArray.makeString(stringRef, parameter, charactersBufferAllocator, charactersToString);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private static final class LargeCharArrayCharSequence implements CharSequence {

        private LargeCharArray charArray;
        private long charArrayOffset;
        private int length;
        private CharMapper<Void> charMapper;

        void initialize(LargeCharArray charArray, long charArrayOffset, int length, CharMapper<Void> charMapper) {

            Objects.requireNonNull(charArray);
            Objects.checkFromIndexSize(charArrayOffset, length, charArray.getLimit());

            this.charArray = charArray;
            this.charArrayOffset = charArrayOffset;
            this.length = length;
            this.charMapper = charMapper;
        }

        @Override
        public int length() {

            return length;
        }

        @Override
        public char charAt(int index) {

            Objects.checkIndex(index, length);

            final char c = charArray.get(charArrayOffset + index);

            return charMapper != null ? charMapper.map(c, null) : c;
        }

        @Override
        public CharSequence subSequence(int start, int end) {

            throw new UnsupportedOperationException();
        }
    }

    @Override
    protected LargeLongArray rehash(LargeLongArray keys, long newCapacity, long keyMask) {

        if (DEBUG) {

            enter(b -> b.add("keys", keys).add("newCapacity", newCapacity).add("keyMask", keyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacity = computeOuterCapacity(newCapacity, innerCapacityExponent);

        final LargeLongArray newKeys = new LargeLongArray(newOuterCapacity, innerCapacityExponent, NO_ELEMENT);
        final LargeLongMultiHeadSinglyLinkedList<StringStorer> newBuckets = createBuckets(newOuterCapacity, innerCapacityExponent);

        clearSet(newKeys);

        final LargeLongArray existingValues = getValues();
        final LargeLongArray newValues = reallocateValues(newOuterCapacity);

        final long hashArrayLength = keys.getCapacity();

        this.scratchCharSequence = scratchLargeCharArrayCharSequence;

        final LargeLongMultiHeadSinglyLinkedList<StringStorer> existingBuckets = buckets;

        for (long i = 0L; i < hashArrayLength; ++ i) {

            final long element = keys.get(i);

            if (element != NO_ELEMENT) {

                final long bucketHeadNode = existingValues.get(i);

                for (long node = bucketHeadNode; node != BaseList.NO_NODE; node = existingBuckets.getNextNode(node)) {

                    final long charArrayIndex = existingBuckets.getValue(node);

                    final int length = Integers.checkUnsignedLongToUnsignedInt(charArray.getStringLength(charArrayIndex));

                    scratchLargeCharArrayCharSequence.initialize(charArray, charArrayIndex, length, null);

                    addNotAlreadyAdded(newKeys, newValues, newBuckets, scratchCharSequence, 0, length);
                }
            }
        }

        this.buckets = newBuckets;

        if (DEBUG) {

            exit(newKeys);
        }

        return newKeys;
    }

    private long addNotAlreadyAdded(LargeLongArray keys, LargeLongArray values, LargeLongMultiHeadSinglyLinkedList<StringStorer> buckets, CharSequence charSequence, int offset,
            int length) {

        if (DEBUG) {

            enter(b -> b.add("keys", keys).add("values", values).add("buckets", buckets).add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        final long hash = getHashCode(charSequence, offset, length);

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d keyMask=0x%08x", hashArrayIndex, keyMask);
        }

        final long bucketHeadNode = values.get(hashArrayIndex);

        final long result = charArray.getLimit();

        charArray.add(charSequence, offset, length);

        this.scratchHashArrayIndex = hashArrayIndex;

        buckets.addHead(this, result, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);

        if (DEBUG) {

            exit(result, b -> b.add("keys", keys).add("values", values).add("buckets", buckets).add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        return result;
    }

    private long getOrAdd(LargeLongArray keys, LargeLongArray values, LargeLongMultiHeadSinglyLinkedList<StringStorer> buckets, CharSequence charSequence, int offset,
            int length) {

        if (DEBUG) {

            enter(b -> b.add("keys", keys).add("values", values).add("buckets", buckets).add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        final long hash = getHashCode(charSequence, offset, length);

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d keyMask=0x%08x", hashArrayIndex, keyMask);
        }

        final long noValue = -1L;

        final long bucketHeadNode;
        final long charArrayIndex;

        if (isEmpty()) {

            bucketHeadNode = BaseList.NO_NODE;
            charArrayIndex = noValue;
        }
        else {
            bucketHeadNode = values.get(hashArrayIndex);

            this.scratchCharSequence = charSequence;
            this.scratchOffset = offset;
            this.scratchLength = length;

            charArrayIndex = bucketHeadNode != BaseList.NO_NODE
                    ? buckets.findValue(noValue, bucketHeadNode, this,
                            (v, i) -> i.charArray.matches(v, i.scratchCharSequence, i.scratchOffset, i.scratchLength, false))
                    : noValue;
        }

        final long result;

        if (charArrayIndex == noValue) {

            result = charArray.getLimit();

            charArray.add(charSequence, offset, length);

            this.scratchHashArrayIndex = hashArrayIndex;

            final long node = buckets.addHead(this, result, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter);

            values.set(hashArrayIndex, node);

            incrementNumElements();
        }
        else {
            result = charArrayIndex;
        }

        if (DEBUG) {

            exit(result, b -> b.add("keys", keys).add("values", values).add("buckets", buckets).add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        return result;
    }

    @FunctionalInterface
    public interface IForEachKeyAndValue<P> {

        void each(long stringRef, LargeCharArray largeCharArray, P parameter);
    }

    private <P> void forEachKeyAndValue(P parameter, IForEachKeyAndValue<P> forEach) {

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        forEachKeyAndValue(forEach, parameter, (k, ki, v, vi, p1, p2) -> p1.each(vi, v, p2));

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    private <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<LargeLongArray, LargeCharArray, P1, P2> forEach) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final LargeLongArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final LargeCharArray chars = charArray;

        final LargeLongArray bucketHeadNodes = getValues();

        final long noKey = NonBucket.NO_ELEMENT;

        for (long i = 0; i < hashArrayLimit; ++ i) {

            final long mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                for (long node = bucketHeadNodes.get(i); node != BaseList.NO_NODE; node = buckets.getNextNode(node)) {

                    final long stringIndex = buckets.getValue(node);

                    forEach.each(hashArray, i, chars, stringIndex, parameter1, parameter2);
                }
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final String toString() {

        final ILongForEachAppendCaller<LargeCharArray, StringStorer> forEachAppendCaller
                = (b, i, f) -> i.forEachKeyAndValue(f, (stringRef, charArray, forEach) -> forEach.each(stringRef, charArray, b, i));

        final ILongAppendEachValue<LargeCharArray, StringStorer> appendEachValue = (r, a, b, p) -> a.asString(r, b);

        return Maps.longAppendToString(getClass().getSimpleName(), getNumElements(), this, forEachAppendCaller, appendEachValue);
    }

    private static void clearSet(LargeLongArray set) {

    }

    private static long getHashCode(CharSequence charSequence, int offset, int length) {

        long hash = 0L;

        for (int i = 0; i < length; ++ i) {

            hash ^= charSequence.charAt(offset + i);
            hash <<= 1;
        }

        return hash;
    }
}
