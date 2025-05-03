package dev.jdata.db.engine.database;

import java.util.Objects;

import org.jutils.io.strings.StringRef;
import org.jutils.io.strings.StringResolver.CharacterBuffer;
import org.jutils.io.strings.StringResolver.CharacterBuffers;

import dev.jdata.db.utils.adt.arrays.LargeCharArray;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.lists.BaseList;
import dev.jdata.db.utils.adt.lists.LargeLongMultiHeadSinglyLinkedList;
import dev.jdata.db.utils.adt.lists.LongNodeSetter;
import dev.jdata.db.utils.adt.maps.BaseLargeArrayMap;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.CharMapper;
import dev.jdata.db.utils.function.CharPredicate;
import dev.jdata.db.utils.scalars.Integers;

public final class StringStorer extends BaseLargeArrayMap<LargeLongArray, LargeLongArray> implements IStringStorer {

    private static final boolean DEBUG = Boolean.FALSE;

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
        super(initialOuterCapacity, innerCapacityExponent, loadFactor, (o, i) -> new LargeLongArray(o, i, NO_ELEMENT), LargeLongArray::reset,
                (o, i) -> new LargeLongArray(o, i, null));

        this.charArray = new LargeCharArray(initialOuterCapacity, innerCapacityExponent);
        this.scratchLargeCharArrayCharSequence = new LargeCharArrayCharSequence();
        this.scratchCharacterBuffers = new CharacterBuffers();

        this.buckets = createBuckets(initialOuterCapacity, innerCapacityExponent);
    }

    @Override
    public void clear() {

        clearHashed();

        charArray.reset();
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

        return charArray.containsOnly(stringRef, predicate);
    }

    @Override
    public long toLowerCase(long stringRef) {

        this.scratchCharSequence = scratchLargeCharArrayCharSequence;

        final int length = Integers.checkUnsignedLongToUnsignedInt(charArray.getStringLength(stringRef));

        scratchLargeCharArrayCharSequence.initialize(charArray, stringRef, length, (c, p) -> Character.toLowerCase(c));

        return addNotAlreadyAdded(getHashed(), getValues(), buckets, scratchLargeCharArrayCharSequence, 0, length);
    }

    @Override
    public long getOrAddStringRef(CharSequence charSequence, int offset, int length) {

        return getOrAdd(getHashed(), getValues(), buckets, charSequence, offset, length);
    }

    @Override
    public long getOrAddStringRef(CharacterBuffer[] characterBuffers, int numCharacterBuffers) {

        Checks.isNotEmpty(characterBuffers);
        Checks.isLengthAboveZero(numCharacterBuffers);

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

            removed = buckets.removeNodeByValue(this, stringRef, bucketHeadNode, BaseList.NO_NODE, headSetter, tailSetter) != BaseList.NO_NODE;
        }

        if (removed) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
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

        final LargeLongArray exitingValues = getValues();
        final LargeLongArray newValues = reallocateValues(newOuterCapacity);

        final long setLength = keys.getCapacity();

        this.scratchCharSequence = scratchLargeCharArrayCharSequence;

        final LargeLongMultiHeadSinglyLinkedList<StringStorer> existingBuckets = buckets;

        for (long i = 0; i < setLength; ++ i) {

            final long element = keys.get(i);

            if (element != NO_ELEMENT) {

                final long bucketHeadNode = exitingValues.get(i);

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
