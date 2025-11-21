package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public class MaxDistance extends BaseMaxDistance {

    private static final boolean DEBUG = DebugConstants.DEBUG_MAX_DISTANCE;

    private static final Class<?> debugClass = MaxDistance.class;

    public interface MaxDistanceHashedOperations<T, U> extends BaseMaxDistanceHashedOperations<T, U, byte[]> {

        int getKeyMask(T hashed);
    }

    public interface MaxDistanceIntSetOperations<T> extends MaxDistanceHashedOperations<T, int[]> {

        long add(T set, int[] hashArray, int value, int hashArrayIndex);
    }

    public interface MaxDistanceLongSetOperations<T> extends MaxDistanceHashedOperations<T, long[]> {

        long add(T set, long[] hashArray, long value, int hashArrayIndex);
    }

    public interface MaxDistanceObjectSetOperations<T, U> extends MaxDistanceHashedOperations<U, T[]> {

        long add(U set, T[] hashArray, T value, int hashArrayIndex);
    }

    public interface MaxDistanceMapOperations<T, U, V> extends MaxDistanceHashedOperations<T, U> {

        V getValues(T map);
    }

    public interface MaxDistanceIntMapOperations<T, V> extends MaxDistanceMapOperations<T, int[], V> {

        long put(T map, int[] hashArray, int key, int hashArrayIndex);
    }

    public interface MaxDistanceLongMapOperations<T, V> extends MaxDistanceMapOperations<T, long[], V> {

        long put(T map, long[] hashArray, long key, int hashArrayIndex);
    }

    public interface MaxDistanceObjectMapOperations<T, K, V> extends MaxDistanceMapOperations<T, K[], V> {

        long put(T map, K[] hashArray, K key, int hashArrayIndex);
    }

    public static <T> boolean addValue(T set, int value, MaxDistanceIntSetOperations<T> operations) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        boolean result;

        for (;;) {

            final int hashArrayIndex = HashFunctions.hashArrayIndex(value, operations.getKeyMask(set));

            final long putResult = operations.add(set, operations.getHashArray(set), value, hashArrayIndex);
            final int putIndex = IntCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

            final int capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(set));

            final int distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(set)[putIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(distance);

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

    public static <T> int putMaxDistance(T hashed, int key, int value, int defaultPreviousValue, MaxDistanceIntMapOperations<T, int[]> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final int[] values = operations.getValues(hashed);
        final int putIndex = IntCapacityPutResult.getPutIndex(putResult);

        final int result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[putIndex];

        values[putIndex] = value;

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    public static <T, U> U putMaxDistance(T hashed, int key, U value, U defaultPreviousValue, MaxDistanceIntMapOperations<T, U[]> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final U[] values = operations.getValues(hashed);
        final int putIndex = IntCapacityPutResult.getPutIndex(putResult);

        final U result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[putIndex];

        values[putIndex] = value;

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    private static <T> long putMaxDistance(T hashed, int key, MaxDistanceIntMapOperations<T, ?> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("operations", operations));
        }

        long result = IntCapacityPutResult.makePutResult(false, HashArray.NO_INDEX);

        for (;;) {

            final int hashArrayIndex = HashFunctions.hashArrayIndex(key, operations.getKeyMask(hashed));

            final long putResult = operations.put(hashed, operations.getHashArray(hashed), key, hashArrayIndex);
            final int putIndex = IntCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

            final int capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(hashed));

            final int distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(hashed)[putIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(distance);

                result = IntCapacityPutResult.makePutResult(newAdded, putIndex);

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

    public static <T> boolean addValue(T set, long value, MaxDistanceLongSetOperations<T> operations) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        boolean result;

        for (;;) {

            final int hashArrayIndex = HashFunctions.hashArrayIndex(value, operations.getKeyMask(set));

            final long putResult = operations.add(set, operations.getHashArray(set), value, hashArrayIndex);
            final int putIndex = IntCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

            final int capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(set));

            final int distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(set)[putIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(distance);

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

    private static <T> long putMaxDistance(T hashed, long key, int value, int defaultPreviousValue, MaxDistanceLongMapOperations<T, long[]> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final long[] values = operations.getValues(hashed);
        final int putIndex = IntCapacityPutResult.getPutIndex(putResult);

        final long result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[putIndex];

        values[putIndex] = value;

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    public static <T, U> U putMaxDistance(T hashed, long key, U value, U defaultPreviousValue, MaxDistanceLongMapOperations<T, U[]> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final U[] values = operations.getValues(hashed);
        final int putIndex = IntCapacityPutResult.getPutIndex(putResult);

        final U result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[putIndex];

        values[putIndex] = value;

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    private static <T> long putMaxDistance(T hashed, long key, MaxDistanceLongMapOperations<T, ?> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("operations", operations));
        }

        long result = IntCapacityPutResult.makePutResult(false, HashArray.NO_INDEX);

        for (;;) {

            final int hashArrayIndex = HashFunctions.hashArrayIndex(key, operations.getKeyMask(hashed));

            final long putResult = operations.put(hashed, operations.getHashArray(hashed), key, hashArrayIndex);
            final int putIndex = IntCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

            final int capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(hashed));

            final int distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(hashed)[putIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(distance);

                result = IntCapacityPutResult.makePutResult(newAdded, putIndex);

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

    public static <T, U> boolean addValue(U set, T value, MaxDistanceObjectSetOperations<T, U> operations) {

        Objects.requireNonNull(set);
        Objects.requireNonNull(value);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("set", set).add("value", value).add("operations", operations));
        }

        boolean result;

        for (;;) {

            final int hashArrayIndex = HashFunctions.objectHashArrayIndex(value, operations.getKeyMask(set));

            final long putResult = operations.add(set, operations.getHashArray(set), value, hashArrayIndex);
            final int putIndex = IntCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

            final int capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(set));

            final int distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(set)[putIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(distance);

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

    public static <T, K, V> V putMaxDistance(T hashed, K key, V value, V defaultPreviousValue, MaxDistanceObjectMapOperations<T, K, V[]> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(key);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        final long putResult = putMaxDistance(hashed, key, operations);

        final V[] values = operations.getValues(hashed);
        final int putIndex = IntCapacityPutResult.getPutIndex(putResult);

        final V result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[putIndex];

        values[putIndex] = value;

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue).add("operations", operations));
        }

        return result;
    }

    private static <T, K> long putMaxDistance(T hashed, K key, MaxDistanceObjectMapOperations<T, K, ?> operations) {

        Objects.requireNonNull(hashed);
        Objects.requireNonNull(key);
        Objects.requireNonNull(operations);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("operations", operations));
        }

        long result = IntCapacityPutResult.makePutResult(false, HashArray.NO_INDEX);

        for (;;) {

            final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, operations.getKeyMask(hashed));

            final long putResult = operations.put(hashed, operations.getHashArray(hashed), key, hashArrayIndex);

            final int putIndex = IntCapacityPutResult.getPutIndex(putResult);
            final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

            final int capacity = Integers.checkUnsignedLongToUnsignedInt(operations.getCapacity(hashed));

            final int distance = computeDistance(putIndex, hashArrayIndex, capacity);

            if (distance <= 255) {

                operations.getMaxDistances(hashed)[putIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(distance);

                result = IntCapacityPutResult.makePutResult(newAdded, putIndex);

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

    public static int computeDistance(int putIndex, int hashArrayIndex, int capacity) {

        Checks.isIntIndex(putIndex);
        Checks.isIntIndex(hashArrayIndex);
        Checks.isIntCapacityAboveZero(capacity);
        Checks.checkIndex(putIndex, capacity);
        Checks.checkIndex(hashArrayIndex, capacity);

        return putIndex >= hashArrayIndex ? putIndex - hashArrayIndex : capacity - hashArrayIndex + putIndex;
    }

    public static byte[] copyMaxDistances(byte[] maxDistances) {

        Objects.requireNonNull(maxDistances);

        return Array.copyOf(maxDistances);
    }
}
