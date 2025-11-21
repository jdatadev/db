package dev.jdata.db.engine.database.strings;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.LongBuckets;
import dev.jdata.db.utils.adt.lists.ILongNodeSetter;
import dev.jdata.db.utils.adt.lists.ISinglyLinkedMultiHeadNodeListView;
import dev.jdata.db.utils.adt.lists.InheritableMutableObjectLargeSinglyLinkedMultiHeadNodeList;
import dev.jdata.db.utils.adt.lists.InheritableObjectInnerOuterNodeListValues;
import dev.jdata.db.utils.adt.lists.LargeNodeLists;
import dev.jdata.db.utils.adt.maps.InheritableLargeArrayKeysMap;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.jdk.adt.strings.CharSequences;
import dev.jdata.db.utils.jdk.adt.strings.Strings;

final class BucketsStringCache extends InheritableLargeArrayKeysMap<IMutableLongLargeArray> implements IStringCache, IClearable {

    private static final boolean DEBUG = DebugConstants.DEBUG_BUCKETS_STRING_CACHE;

    private static final long NO_NODE = LargeNodeLists.NO_LONG_NODE;

    static final class StringLargeSinglyLinkedMultiHeadList<INSTANCE>

            extends InheritableMutableObjectLargeSinglyLinkedMultiHeadNodeList<INSTANCE, String, LongToStringValues<INSTANCE>> {

        StringLargeSinglyLinkedMultiHeadList(AllocationType allocationType, int initialOuterCapacity, int innerCapacity) {
            super(allocationType, initialOuterCapacity, innerCapacity, LongToStringValues::new);
        }

        @Override
        public ISinglyLinkedMultiHeadNodeListView createEmptyWithSameCapacity() {

            return instantiateOuterCapacityInnerCapacity((i, o) -> new StringLargeSinglyLinkedMultiHeadList<>(AllocationType.HEAP, i, o));
        }

        @Override
        public ISinglyLinkedMultiHeadNodeListView createEmptyWithCapacityExponentIncrease(int capacityExponentIncrease) {

            Checks.isLongCapacityExponentIncrease(capacityExponentIncrease);

            throw new UnsupportedOperationException();
        }
    }

    static final class LongToStringValues<INSTANCE> extends InheritableObjectInnerOuterNodeListValues<String> {

        LongToStringValues(int initialOuterCapacity) {
            super(initialOuterCapacity, String[][]::new, String[]::new);
        }
    }

    private static final ILongNodeSetter<BucketsStringCache> headSetter = (i, h) -> i.scratchHashArray.set(i.scratchHashArrayIndex, h);
    private static final ILongNodeSetter<BucketsStringCache> tailSetter = (i, t) -> { };

    private StringLargeSinglyLinkedMultiHeadList<BucketsStringCache> buckets;

    private final StringBuilder scratchStringBuilder;

    private long scratchHashArrayIndex;
    private IMutableLongLargeArray scratchHashArray;

    BucketsStringCache(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent) {
        this(allocationType, initialOuterCapacityExponent, innerCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    BucketsStringCache(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, float loadFactor) {
        super(allocationType, initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, loadFactor, (o, i) -> createHashArray(o, i),
                BucketsStringCache::clearHashArray);

        Checks.isIntInitialOuterCapacityExponent(initialOuterCapacityExponent);
        Checks.isIntInnerCapacityExponent(innerCapacityExponent);
        Checks.isLoadFactor(loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        this.buckets = createStringBuckets(initialOuterCapacityExponent, innerCapacityExponent);

        this.scratchStringBuilder = new StringBuilder();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void clear() {

        clearHashed();

        buckets.clear();
    }

    @Override
    public boolean contains(CharSequence charSequence) {

        Objects.requireNonNull(charSequence);

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence));
        }

        final long hash = CharSequences.longHashCode(charSequence, getCapacityExponent());

        final boolean result = getString(charSequence, hash) != null;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public String getOrAddString(CharSequence charSequence) {

        Objects.requireNonNull(charSequence);

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence));
        }

        final long hash = CharSequences.longHashCode(charSequence, getCapacityExponent());

        String result = getString(charSequence, hash);

        if (result == null) {

            if (charSequence instanceof String) {

                result = (String)charSequence;
            }
            else {
                result = Strings.of(charSequence);
            }

            final long keyMask = checkCapacity(1);

            addNotAlreadyAdded(getHashed(), hash, keyMask, buckets, result);

            incrementNumElements();
        }

        return result;
    }

    @Override
    public String getOrAddString(CharSequence charSequence, int startIndex, int numCharacters) {

        Objects.requireNonNull(charSequence);
        Checks.checkFromIndexSize(startIndex, numCharacters, charSequence.length());

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence));
        }

        final long hash = CharSequences.longHashCode(charSequence, startIndex, numCharacters, getCapacityExponent());

        String result = getString(charSequence, startIndex, numCharacters, hash);

        if (result == null) {

            if (startIndex == 0 && numCharacters == charSequence.length() && charSequence instanceof String) {

                result = (String)charSequence;
            }
            else {
                result = Strings.of(charSequence, startIndex, numCharacters);
            }

            final long keyMask = checkCapacity(1);

            addNotAlreadyAdded(getHashed(), hash, keyMask, buckets, result);

            incrementNumElements();
        }

        return result;
    }

    @Override
    public String getOrAddString(int i) {

        scratchStringBuilder.setLength(0);
        scratchStringBuilder.append(i);

        return getOrAddString(scratchStringBuilder);
    }

    @Override
    protected IMutableLongLargeArray rehash(IMutableLongLargeArray bucketHeadNodesHashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent)
                    .add("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();

        final int newOuterCapacityExponent = computeOuterCapacityExponent(newCapacityExponent, innerCapacityExponent);

        final IMutableLongLargeArray newBucketHeadNodesHashArray = createHashArray(newOuterCapacityExponent, innerCapacityExponent);
        final StringLargeSinglyLinkedMultiHeadList<BucketsStringCache> newBuckets = createStringBuckets(newOuterCapacityExponent, innerCapacityExponent);

        clearHashArray(newBucketHeadNodesHashArray);

        final long hashArrayLength = bucketHeadNodesHashArray.getLimit();

        final StringLargeSinglyLinkedMultiHeadList<BucketsStringCache> existingBuckets = buckets;

        final long noNode = NO_NODE;

        final int capacityExponent = getCapacityExponent();

        for (long i = 0L; i < hashArrayLength; ++ i) {

            for (long node = bucketHeadNodesHashArray.get(i); node != noNode; node = existingBuckets.getNextNode(node)) {

                final String stringValue = existingBuckets.getValue(node);

                final long hash = CharSequences.longHashCode(stringValue, capacityExponent);

                addNotAlreadyAdded(newBucketHeadNodesHashArray, hash, newKeyMask, newBuckets, stringValue);
            }
        }

        this.buckets = newBuckets;

        if (DEBUG) {

            exit(newBucketHeadNodesHashArray, b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray).add("newCapacity", newCapacity)
                    .add("newCapacityExponent", newCapacityExponent).add("newKeyMask", newKeyMask));
        }

        return newBucketHeadNodesHashArray;
    }

    private String getString(CharSequence charSequence, long hash) {

        Objects.requireNonNull(charSequence);

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence).add("hash", hash));
        }

        String result = null;

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, getKeyMask());

        final IMutableLongLargeArray bucketHeadNodesArray = getHashed();
        final StringLargeSinglyLinkedMultiHeadList<BucketsStringCache> b = buckets;

        if (hashArrayIndex < bucketHeadNodesArray.getLimit()) {

            final long noNode = NO_NODE;

            for (long node = bucketHeadNodesArray.get(hashArrayIndex); node != noNode; node = b.getNextNode(node)) {

                final String stringValue = b.getValue(node);

                if (stringValue.contentEquals(charSequence)) {

                    result = stringValue;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private String getString(CharSequence charSequence, int startIndex, int numCharacters, long hash) {

        Objects.requireNonNull(charSequence);
        Checks.checkFromIndexSize(startIndex, numCharacters, charSequence.length());

        if (DEBUG) {

            enter(b -> b.add("charSequence", charSequence).add("startIndex", startIndex).add("numCharacters", numCharacters).add("hash", hash));
        }

        String result = null;

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(hash, getKeyMask());

        final IMutableLongLargeArray bucketHeadNodesArray = getHashed();
        final StringLargeSinglyLinkedMultiHeadList<BucketsStringCache> b = buckets;

        if (hashArrayIndex < bucketHeadNodesArray.getLimit()) {

            final long noNode = NO_NODE;

            for (long node = bucketHeadNodesArray.get(hashArrayIndex); node != noNode; node = b.getNextNode(node)) {

                final String stringValue = b.getValue(node);

                if (stringValue.length() == numCharacters) {

                    boolean equals = true;

                    for (int i = 0; i < numCharacters; ++ i) {

                        if (stringValue.charAt(i) != charSequence.charAt(startIndex + i)) {

                            equals = false;
                            break;
                        }
                    }

                    if (equals) {

                        result = stringValue;
                        break;
                    }
                }
            }
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private void addNotAlreadyAdded(IMutableLongLargeArray bucketHeadNodesHashArray, long hash, long keyMask, StringLargeSinglyLinkedMultiHeadList<BucketsStringCache> buckets,
            String string) {

        if (DEBUG) {

            enter(b -> b.add("bucketHeadNodesHashArray", bucketHeadNodesHashArray.toHexString()).add("hash", hash).hex("keyMask", keyMask).add("buckets", buckets)
                    .add("string", string));
        }

        final long putHashArrayIndex = HashFunctions.longHashArrayIndex(hash, keyMask);

        final long node = putHashArrayIndex < bucketHeadNodesHashArray.getLimit() ? bucketHeadNodesHashArray.get(putHashArrayIndex) : NO_NODE;

        this.scratchHashArrayIndex = putHashArrayIndex;
        this.scratchHashArray = bucketHeadNodesHashArray;

        buckets.addHead(this, string, node, NO_NODE, headSetter, tailSetter);

        if (DEBUG) {

            exit();
        }
    }

    private static <T> StringLargeSinglyLinkedMultiHeadList<T> createStringBuckets(int initialOuterCapacityExponent, int innerCapacityExponent) {

        final int initialOuterCapacity = CapacityExponents.computeIntCapacityFromExponent(initialOuterCapacityExponent);
        final int innerCapacity = CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent);

        return new StringLargeSinglyLinkedMultiHeadList<>(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }

    private static IMutableLongLargeArray createHashArray(int outerCapacityExponent, int innerCapacityExponent) {

        final int outerCapacity = CapacityExponents.computeIntCapacityFromExponent(outerCapacityExponent);

        return IHeapMutableLongLargeArray.create(outerCapacity, innerCapacityExponent, NO_NODE);
    }

    private static void clearHashArray(IMutableLongLargeArray bucketHeadNodesHashArray) {

        LongBuckets.clearHashArray(bucketHeadNodesHashArray);
    }
}
