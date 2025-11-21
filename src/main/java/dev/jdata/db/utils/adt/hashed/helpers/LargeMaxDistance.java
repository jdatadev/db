package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableByteLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableByteLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableIntLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableObjectLargeArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public class LargeMaxDistance extends BaseMaxDistance {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_MAX_DISTANCE;

    private static final Class<?> debugClass = LargeMaxDistance.class;

    public interface LargeMaxDistanceHashedOperations<T, U> extends BaseMaxDistanceHashedOperations<T, U, IMutableByteLargeArray> {

        long getKeyMask(T hashed);
    }

    public interface LargeMaxDistanceIntSetOperations<T> extends LargeMaxDistanceHashedOperations<T, IMutableIntLargeArray> {

        long add(T set, IMutableIntLargeArray hashArray, int value, long hashArrayIndex);
    }

    public interface LargeMaxDistanceLongSetOperations<T> extends LargeMaxDistanceHashedOperations<T, IMutableLongLargeArray> {

        long add(T set, IMutableLongLargeArray hashArray, long value, long hashArrayIndex);
    }

    public interface LargeMaxDistanceMapOperations<T, U, V> extends LargeMaxDistanceHashedOperations<T, U> {

        V getValues(T map);
    }

    public interface LargeMaxDistanceIntMapOperations<T, V> extends LargeMaxDistanceMapOperations<T, IMutableIntLargeArray, V> {

        long put(T map, IMutableIntLargeArray hashArray, int key, long hashArrayIndex);
    }

    public interface LargeMaxDistanceLongMapOperations<T, V> extends LargeMaxDistanceMapOperations<T, IMutableLongLargeArray, V> {

        long put(T map, IMutableLongLargeArray hashArray, long key, long hashArrayIndex);
    }

    public interface LargeMaxDistanceObjectMapOperations<T, K, V> extends LargeMaxDistanceMapOperations<T, IMutableObjectLargeArray<K>, V> {

        long put(T map, IMutableObjectLargeArray<K> hashArray, K key, long hashArrayIndex);
    }

    public static <T> boolean addValue(T set, int value, LargeMaxDistanceIntSetOperations<T> operations) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        boolean result;

        for (;;) {

            final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, operations.getKeyMask(set));

            final long putResult = operations.add(set, operations.getHashArray(set), value, hashArrayIndex);
            final long putIndex = LongCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

            final long capacity = operations.getCapacity(set);

            final long distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(set).set(putIndex, Integers.checkUnsignedLongToUnsignedByteAsByte(distance));

                result = newAdded;
                break;
            }

            operations.increaseCapacityAndRehash(set);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        return result;
    }

    public static <T> int putMaxDistance(T hashed, int key, int value, int defaultPreviousValue, LargeMaxDistanceIntMapOperations<T, IMutableIntLargeArray> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final IMutableIntLargeArray values = operations.getValues(hashed);
        final long putIndex = LongCapacityPutResult.getPutIndex(putResult);

        final int result = LongCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values.get(putIndex);

        values.set(putIndex, value);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    public static <T, U> U putMaxDistance(T hashed, int key, U value, U defaultPreviousValue, LargeMaxDistanceIntMapOperations<T, IMutableObjectLargeArray<U>> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final IMutableObjectLargeArray<U> values = operations.getValues(hashed);
        final long putIndex = LongCapacityPutResult.getPutIndex(putResult);

        final U result = LongCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values.get(putIndex);

        values.set(putIndex, value);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    private static <T> long putMaxDistance(T hashed, int key, LargeMaxDistanceIntMapOperations<T, ?> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("operations", operations));
        }

        long result = LongCapacityPutResult.makePutResult(false, LargeHashArray.NO_INDEX);

        for (;;) {

            final long hashArrayIndex = HashFunctions.longHashArrayIndex(key, operations.getKeyMask(hashed));

            final long putResult = operations.put(hashed, operations.getHashArray(hashed), key, hashArrayIndex);
            final long putIndex = LongCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

            final long capacity = operations.getCapacity(hashed);

            final long distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(hashed).set(putIndex, Integers.checkUnsignedLongToUnsignedByteAsByte(distance));

                result = LongCapacityPutResult.makePutResult(newAdded, putIndex);

                if (newAdded) {

                    operations.incrementNumElements(hashed);
                }
                break;
            }

            operations.increaseCapacityAndRehash(hashed);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("operations", operations));
        }

        return result;
    }

    public static <T> boolean addValue(T set, long value, LargeMaxDistanceLongSetOperations<T> operations) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        boolean result;

        for (;;) {

            final long hashArrayIndex = HashFunctions.longHashArrayIndex(value, operations.getKeyMask(set));

            final long putResult = operations.add(set, operations.getHashArray(set), value, hashArrayIndex);
            final long putIndex = LongCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

            final long capacity = operations.getCapacity(set);

            final long distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(set).set(putIndex, Integers.checkUnsignedLongToUnsignedByteAsByte(distance));

                result = newAdded;
                break;
            }

            operations.increaseCapacityAndRehash(set);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        return result;
    }

    private static <T> long putMaxDistance(T hashed, long key, int value, int defaultPreviousValue, LargeMaxDistanceLongMapOperations<T, IMutableLongLargeArray> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final IMutableLongLargeArray values = operations.getValues(hashed);
        final long putIndex = LongCapacityPutResult.getPutIndex(putResult);

        final long result = LongCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values.get(putIndex);

        values.set(putIndex, value);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    public static <T, U> U putMaxDistance(T hashed, long key, U value, U defaultPreviousValue, LargeMaxDistanceLongMapOperations<T, IMutableObjectLargeArray<U>> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final IMutableObjectLargeArray<U> values = operations.getValues(hashed);
        final long putIndex = LongCapacityPutResult.getPutIndex(putResult);

        final U result = LongCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values.get(putIndex);

        values.set(putIndex, value);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    private static <T> long putMaxDistance(T hashed, long key, LargeMaxDistanceLongMapOperations<T, ?> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("operations", operations));
        }

        long result = LongCapacityPutResult.makePutResult(false, LargeHashArray.NO_INDEX);

        for (;;) {

            final long hashArrayIndex = HashFunctions.longHashArrayIndex(key, operations.getKeyMask(hashed));

            final long putResult = operations.put(hashed, operations.getHashArray(hashed), key, hashArrayIndex);
            final long putIndex = LongCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

            final long capacity = operations.getCapacity(hashed);

            final long distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(hashed).set(putIndex, Integers.checkUnsignedLongToUnsignedByteAsByte(distance));

                result = LongCapacityPutResult.makePutResult(newAdded, putIndex);

                if (newAdded) {

                    operations.incrementNumElements(hashed);
                }
                break;
            }

            operations.increaseCapacityAndRehash(hashed);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("operations", operations));
        }

        return result;
    }

    public static <T, K, V> V putMaxDistance(T hashed, K key, V value, V defaultPreviousValue,
            LargeMaxDistanceObjectMapOperations<T, K, IMutableObjectLargeArray<V>> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(key);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final IMutableObjectLargeArray<V> values = operations.getValues(hashed);
        final long putIndex = IntCapacityPutResult.getPutIndex(putResult);

        final V result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values.get(putIndex);

        values.set(putIndex, value);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    private static <T, K> long putMaxDistance(T hashed, K key, LargeMaxDistanceObjectMapOperations<T, K, ?> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(key);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("operations", operations));
        }

        long result = LongCapacityPutResult.makePutResult(false, LargeHashArray.NO_INDEX);

        for (;;) {

            final long hashArrayIndex = HashFunctions.longObjectHashArrayIndex(key, operations.getKeyMask(hashed));

            final long putResult = operations.put(hashed, operations.getHashArray(hashed), key, hashArrayIndex);

            final long putIndex = LongCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

            final long capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(hashed));

            final long distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(hashed).set(putIndex, Integers.checkUnsignedLongToUnsignedByteAsByte(distance));

                result = LongCapacityPutResult.makePutResult(newAdded, putIndex);

                if (newAdded) {

                    operations.incrementNumElements(hashed);
                }
                break;
            }

            operations.increaseCapacityAndRehash(hashed);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("operations", operations));
        }

        return result;
    }

    public static long computeDistance(long putIndex, long hashArrayIndex, long capacity) {

        Checks.isLongIndex(putIndex);
        Checks.isLongIndex(hashArrayIndex);
        Checks.isLongCapacityAboveZero(capacity);
        Checks.checkLongIndex(putIndex, capacity);
        Checks.checkLongIndex(hashArrayIndex, capacity);

        return putIndex >= hashArrayIndex ? putIndex - hashArrayIndex : capacity - hashArrayIndex + putIndex;
    }

    public static IHeapMutableByteLargeArray copyMaxDistances(IMutableByteLargeArray maxDistances) {

        Objects.requireNonNull(maxDistances);

        return IHeapMutableByteLargeArray.copyOf(maxDistances);
    }
}
