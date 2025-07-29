package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeCharArray;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.LargeLists;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.maps.BaseLargeArrayKeysMap;
import dev.jdata.db.utils.adt.maps.IForEachKeyAndValueWithKeysAndValues;
import dev.jdata.db.utils.adt.maps.Maps;
import dev.jdata.db.utils.adt.maps.Maps.ILongAppendEachValue;
import dev.jdata.db.utils.adt.maps.Maps.ILongForEachAppendCaller;
import dev.jdata.db.utils.adt.strings.CharSequences;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharMapper;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.scalars.Integers;

public final class StringStorer extends BaseLargeArrayKeysMap<LargeLongArray> implements IStringStorer, StringResolver {

    private static final boolean DEBUG = DebugConstants.DEBUG_STRING_STORER;

    private static final long NO_NODE = LargeLists.NO_LONG_NODE;

    private static final ILongNodeSetter<StringStorer> headSetter = (i, h) -> i.scratchHashArray.set(i.scratchHashArrayIndex, h);
    private static final ILongNodeSetter<StringStorer> tailSetter = (i, t) -> { };

    private final LargeCharArray charArray;
    private final LargeCharArrayCharSequence scratchLargeCharArrayCharSequence;

    private LargeLongMultiHeadSinglyLinkedList<StringStorer> buckets;

    private final CharacterBuffers scratchCharacterBuffers;

    private long scratchHashArrayIndex;
    private LargeLongArray scratchHashArray;
    private CharSequence scratchCharSequence;
    private int scratchOffset;
    private int scratchLength;

    public StringStorer(int initialOuterCapacityExponent, int innerCapacityExponent) {
        this(initialOuterCapacityExponent, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    public StringStorer(int initialOuterCapacityExponent, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, loadFactor, (o, i) -> createHashArray(o, i), LargeLongArray::clear);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        this.charArray = new LargeCharArray(initialOuterCapacityExponent, innerCapacityExponent);
        this.scratchLargeCharArrayCharSequence = new LargeCharArrayCharSequence();
        this.scratchCharacterBuffers = new CharacterBuffers();

        this.buckets = createBuckets(initialOuterCapacityExponent, innerCapacityExponent);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        charArray.clear();
        buckets.clear();

        if (DEBUG) {

            exit();
        }
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
            final long hash = CharSequences.longHashCode(charSequence, offset, length, getCapacityExponent());

            final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, getKeyMask());

            final LargeLongArray bucketHeadNodesHashArray = getHashed();

            if (hashArrayIndex >= bucketHeadNodesHashArray.getLimit()) {

                found = false;
            }
            else {
                final long bucketHeadNode = bucketHeadNodesHashArray.get(hashArrayIndex);

                this.scratchCharSequence = charSequence;
                this.scratchOffset = offset;
                this.scratchLength = length;

                found = bucketHeadNode != NO_NODE && buckets.contains(bucketHeadNode, this,
                        (v, i) -> i.charArray.matches(v, i.scratchCharSequence, i.scratchOffset, i.scratchLength, false));
            }
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

        final LargeCharArrayCharSequence charSequence;

        this.scratchCharSequence = charSequence = scratchLargeCharArrayCharSequence;

        final int length = Integers.checkUnsignedLongToUnsignedInt(charArray.getStringLength(stringRef));

        charSequence.initialize(charArray, stringRef, length, (c, p) -> Character.toLowerCase(c));

        final int offset = 0;

        final long hash = CharSequences.longHashCode(charSequence, offset, length, getCapacityExponent());

        final long result = addNotAlreadyAdded(getHashed(), hash, getKeyMask(), buckets, charSequence, offset, length);

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

        final long result = getOrAdd(getHashed(), buckets, charSequence, offset, length);

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

            result = getOrAdd(getHashed(), buckets, characterBuffer, characterBuffer.getOffset(), characterBuffer.getLength());
            break;

        default:

            final CharacterBuffers buffers = scratchCharacterBuffers;

            buffers.initialize(characterBuffers, numCharacterBuffers);

            result = getOrAdd(getHashed(), buckets, buffers, 0, buffers.length());
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

        final long noNode = NO_NODE;

        if (bucketHeadNode == noNode) {

            removed = false;
        }
        else {
            this.scratchHashArrayIndex = hashArrayIndex;

            removed = buckets.removeAtMostOneNodeByValue(this, stringRef, bucketHeadNode, noNode, headSetter, tailSetter) != noNode;
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
            Checks.checkFromIndexSize(charArrayOffset, length, charArray.getLimit());

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

            Checks.checkIndex(index, length);

            final char c = charArray.get(charArrayOffset + index);

            return charMapper != null ? charMapper.map(c, null) : c;
        }

        @Override
        public CharSequence subSequence(int start, int end) {

            throw new UnsupportedOperationException();
        }
    }

    @Override
    protected LargeLongArray rehash(LargeLongArray bucketHeadNodesHashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .add("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacityExponent = computeOuterCapacityExponent(newCapacityExponent, innerCapacityExponent);
        final LargeLongArray newBucketHeadNodesHashArray = createHashArray(newOuterCapacityExponent, innerCapacityExponent);

        final LargeLongMultiHeadSinglyLinkedList<StringStorer> newBuckets = createBuckets(newOuterCapacityExponent, innerCapacityExponent);

        clearHashArray(newBucketHeadNodesHashArray);

        final long hashArrayLength = bucketHeadNodesHashArray.getLimit();

        this.scratchCharSequence = scratchLargeCharArrayCharSequence;

        final LargeLongMultiHeadSinglyLinkedList<StringStorer> existingBuckets = buckets;

        final long noNode = NO_NODE;

        for (long i = 0L; i < hashArrayLength; ++ i) {

            for (long node = bucketHeadNodesHashArray.get(i); node != noNode; node = existingBuckets.getNextNode(node)) {

                final long charArrayIndex = existingBuckets.getValue(node);

                final int length = Integers.checkUnsignedLongToUnsignedInt(charArray.getStringLength(charArrayIndex));

                scratchLargeCharArrayCharSequence.initialize(charArray, charArrayIndex, length, null);

                final CharSequence charSequence = scratchCharSequence;

                final long hash = CharSequences.longHashCode(charSequence, 0, length, newCapacityExponent);

                addNotAlreadyAddedToHash(newBucketHeadNodesHashArray, hash, newKeyMask, newBuckets, charArrayIndex);
            }
        }

        this.buckets = newBuckets;

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("newCapacity", newCapacity)
                    .add("newCapacityExponent", newCapacityExponent).add("newKeyMask", newKeyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    private long getOrAdd(LargeLongArray bucketHeadNodesHashArray, LargeLongMultiHeadSinglyLinkedList<StringStorer> bucketsToAddTo, CharSequence charSequence, int offset,
            int length) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("bucketsToAddTo", bucketsToAddTo).add("charSequence", charSequence)
                    .add("offset", offset).add("length", length));
        }

        final long noValue = -1L;

        final long bucketHeadNode;
        final long charArrayIndex;

        final long hash = CharSequences.longHashCode(charSequence, offset, length, getCapacityExponent());

        final long keyMask = getKeyMask();

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d keyMask=0x%08x", hashArrayIndex, keyMask);
        }

        if (isEmpty()) {

            bucketHeadNode = NO_NODE;
            charArrayIndex = noValue;
        }
        else {
            if (hashArrayIndex >= bucketHeadNodesHashArray.getLimit()) {

                bucketHeadNode = NO_NODE;
                charArrayIndex = noValue;
            }
            else {
                bucketHeadNode = bucketHeadNodesHashArray.get(hashArrayIndex);

                this.scratchCharSequence = charSequence;
                this.scratchOffset = offset;
                this.scratchLength = length;

                charArrayIndex = bucketHeadNode != NO_NODE
                        ? bucketsToAddTo.findValue(noValue, bucketHeadNode, this,
                                (v, i) -> i.charArray.matches(v, i.scratchCharSequence, i.scratchOffset, i.scratchLength, false))
                        : noValue;
            }
        }

        final long result;

        if (charArrayIndex == noValue) {

            final long checkCapacityKeyMask = checkCapacity(1);

            final long newHash = checkCapacityKeyMask == keyMask
                    ? hash
                    : CharSequences.longHashCode(charSequence, offset, length, getCapacityExponent());

            result = addNotAlreadyAdded(getHashed(), newHash, checkCapacityKeyMask, buckets, charSequence, offset, length);

            incrementNumElements();
        }
        else {
            result = charArrayIndex;
        }

        if (DEBUG) {

            exit(result, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("bucketsToAddTo", bucketsToAddTo).add("charSequence", charSequence)
                    .add("offset", offset).add("length", length));
        }

        return result;
    }

    private long addNotAlreadyAdded(LargeLongArray bucketHeadNodesHashArray, long hash, long keyMask, LargeLongMultiHeadSinglyLinkedList<StringStorer> buckets,
            CharSequence charSequence, int offset, int length) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("hash", hash).add("keyMask", keyMask).add("buckets", buckets)
                    .add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        final long result = charArray.getLimit();

        charArray.add(charSequence, offset, length);

        addNotAlreadyAddedToHash(bucketHeadNodesHashArray, hash, keyMask, buckets, result);

        if (DEBUG) {

            exit(result, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("hash", hash).add("keyMask", keyMask).add("buckets", buckets)
                    .add("charSequence", charSequence).add("offset", offset).add("length", length));
        }

        return result;
    }

    private void addNotAlreadyAddedToHash(LargeLongArray bucketHeadNodesHashArray, long hash, long keyMask, LargeLongMultiHeadSinglyLinkedList<StringStorer> buckets,
            long stringRef) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("hash", hash).add("keyMask", keyMask).add("buckets", buckets)
                    .add("stringRef", stringRef));
        }

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d keyMask=0x%08x", hashArrayIndex, keyMask);
        }

        final long bucketHeadNode = hashArrayIndex < bucketHeadNodesHashArray.getLimit() ? bucketHeadNodesHashArray.get(hashArrayIndex) : NO_NODE;

        this.scratchHashArrayIndex = hashArrayIndex;
        this.scratchHashArray = bucketHeadNodesHashArray;

        buckets.addHead(this, stringRef, bucketHeadNode, NO_NODE, headSetter, tailSetter);

        if (DEBUG) {

            exit(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("hash", hash).add("keyMask", keyMask).add("buckets", buckets)
                    .add("stringRef", stringRef));
        }
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

    private <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, IForEachKeyAndValueWithKeysAndValues<LargeLongArray, LargeCharArray, P1, P2> forEach) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final LargeLongArray bucketHeadNodesHashArray = getHashed();
        final long hashArrayLimit = bucketHeadNodesHashArray.getLimit();

        final LargeCharArray chars = charArray;

        final long noNode = NO_NODE;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            for (long node = bucketHeadNodesHashArray.get(i); node != noNode; node = buckets.getNextNode(node)) {

                final long stringIndex = buckets.getValue(node);

                forEach.each(bucketHeadNodesHashArray, i, chars, stringIndex, parameter1, parameter2);
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

    private static LargeLongArray createHashArray(int outerCapacity, int innerCapacityExponent) {

        return new LargeLongArray(outerCapacity, innerCapacityExponent, NO_NODE);
    }

    private static void clearHashArray(LargeLongArray keys) {

    }
}
