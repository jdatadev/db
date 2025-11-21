package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public class HashArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_HASH_ARRAY;

    private static final boolean ASSERT = AssertionContants.ASSERT_HASH_ARRAY;

    private static final Class<?> debugClass = HashArray.class;

    public static final int NO_INDEX = -1;

    public static int getIndexScanHashArrayToMaxKeyMask(int[] hashArray, int key, int keyMask, int max) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);
        Checks.isIntLengthAboveZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask).add("max", max));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int result = getIndexScanHashArrayToMaxHashArrayIndex(hashArray, key, hashArrayIndex, max);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask).add("max", max));
        }

        return result;
    }

    public static int getIndexScanHashArrayToMaxHashArrayIndex(int[] hashArray, int element, int hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(element);
        Checks.isIntIndex(hashArrayIndex);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        for (int i = hashArrayIndex; remaining != 0 && i < hashArrayLength; ++ i, -- remaining) {

            if (hashArray[i] == element) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; remaining != 0 && i < hashArrayIndex; ++ i, -- remaining) {

                if (hashArray[i] == element) {

                    found = i;
                    break;
                }
            }
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return found;
    }

    public static <K> int getIndexScanHashArrayToMaxHashArrayIndex(K[] hashArray, K element, int hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        Objects.requireNonNull(element);
        Checks.isIntIndex(hashArrayIndex);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        for (int i = hashArrayIndex; remaining != 0 && i < hashArrayLength; ++ i, -- remaining) {

            if (element.equals(hashArray[i])) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; remaining != 0 && i < hashArrayIndex; ++ i, -- remaining) {

                if (hashArray[i].equals(element)) {

                    found = i;
                    break;
                }
            }
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return found;
    }

    public static int getIndexScanHashArrayToMaxKeyMask(long[] hashArray, long key, int keyMask, int max) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Checks.isIntLengthAboveZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask).add("max", max));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int result = getIndexScanHashArrayToMaxHashArrayIndex(hashArray, key, hashArrayIndex, max);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask).add("max", max));
        }

        return result;
    }

    public static int getIndexScanHashArrayToMaxHashArrayIndex(long[] hashArray, long element, int hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(element);
        Checks.isIntIndex(hashArrayIndex);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        for (int i = hashArrayIndex; remaining != 0 && i < hashArrayLength; ++ i, -- remaining) {

            if (hashArray[i] == element) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; remaining != 0 && i < hashArrayIndex; ++ i, -- remaining) {

                if (hashArray[i] == element) {

                    found = i;
                    break;
                }
            }
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("element", element));
        }

        return found;
    }

    public static int getIndexScanEntireHashArray(int[] hashArray, int key, int keyMask) {

        Checks.isNotEmpty(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int result = getIndexScanEntireHashArrayIndex(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return result;
    }

    public static int getIndexScanEntireHashArrayIndex(int[] hashArray, int key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Checks.checkArrayIndex(hashArray, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (hashArray[i] == key) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (hashArray[i] == key) {

                    found = i;
                    break;
                }
            }
        }

        if (found == noIndex) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return found;
    }

    public static <K> int getIndexScanEntireHashArray(K[] hashArray, K key, int keyMask) {

        Checks.isNotEmpty(hashArray);
        Objects.requireNonNull(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, keyMask);

        final int result = getIndexScanEntireHashArrayIndex(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return result;
    }

    public static <K> int getIndexScanEntireHashArrayIndex(K[] hashArray, K key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Objects.requireNonNull(key);
        Checks.checkArrayIndex(hashArray, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (key.equals(hashArray[i])) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (key.equals(hashArray[i])) {

                    found = i;
                    break;
                }
            }
        }

        if (found == noIndex) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return found;
    }

    public static long add(int[] hashArray, int value, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Checks.isIntIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final int noElement = IntNonBucket.NO_ELEMENT;

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

            if (mapKey == noElement) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map new foundIndex=" + i);
                }

                hashArray[i] = value;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey == value) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map existing foundIndex=" + i);
                }

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final int mapKey = hashArray[i];

                if (mapKey == noElement) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    hashArray[i] = value;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey == value) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    found = i;
                    break;
                }
            }
        }

        if (ASSERT) {

            Assertions.areNotEqual(found, noIndex);
        }

        final long result = IntCapacityPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    public static long add(long[] hashArray, long value, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Checks.isIntIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long noElement = LongNonBucket.NO_ELEMENT;

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey == noElement) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map new foundIndex=" + i);
                }

                hashArray[i] = value;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey == value) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map existing foundIndex=" + i);
                }

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final long mapKey = hashArray[i];

                if (mapKey == noElement) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    hashArray[i] = value;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey == value) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    found = i;
                    break;
                }
            }
        }

        if (ASSERT) {

            Assertions.areNotEqual(found, noIndex);
        }

        final long result = IntCapacityPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    public static <T> long add(T[] hashArray, T value, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Objects.requireNonNull(value);
        Checks.isIntIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        @SuppressWarnings("unchecked")
        final T noElement = (T)ObjectNonBucket.NO_ELEMENT;

        final int hashArrayLength = hashArray.length;

        final int noIndex = NO_INDEX;

        int found = noIndex;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final T mapKey = hashArray[i];

            if (mapKey == noElement) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map new foundIndex=" + i);
                }

                hashArray[i] = value;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey.equals(value)) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map existing foundIndex=" + i);
                }

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final T mapKey = hashArray[i];

                if (mapKey == noElement) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    hashArray[i] = value;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey.equals(value)) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    found = i;
                    break;
                }
            }
        }

        if (ASSERT) {

            Assertions.areNotEqual(found, noIndex);
        }

        final long result = IntCapacityPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    public static int removeAndReturnIndexScanEntire(int[] hashArray, int key, int keyMask) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int indexOfRemoved = getIndexScanEntireHashArray(hashArray, key, keyMask);

        if (indexOfRemoved == NO_INDEX) {

            throw new IllegalStateException();
        }

        hashArray[indexOfRemoved] = IntNonBucket.NO_ELEMENT;

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexOfRemoved, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return indexOfRemoved;
    }

    public static <P> int removeAndReturnIndexScanToMax(int[] hashArray, int key, int keyMask, byte[] maxDistances) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);
        Objects.requireNonNull(maxDistances);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("keyMask", keyMask).add("maxDistances", maxDistances));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int indexToRemove = removeAndReturnIndexScanToMax(hashArray, key, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (indexToRemove != NO_INDEX) {

            maxDistances[hashArrayIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(MaxDistance.computeDistance(indexToRemove, hashArrayIndex, hashArray.length));
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexToRemove, b -> b.add("key", key).add("keyMask", keyMask).add("maxDistances", maxDistances));
        }

        return indexToRemove;
    }

    private static int removeAndReturnIndexScanToMax(int[] hashArray, int key, int hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int indexToRemove = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(hashArray, key, hashArrayIndex, max);

        if (indexToRemove != NO_INDEX) {

            hashArray[indexToRemove] = IntNonBucket.NO_ELEMENT;
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexToRemove, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return indexToRemove;
    }

    public static int getIndexScanEntireHashArray(long[] hashArray, long key, int keyMask) {

        Checks.isNotEmpty(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int result = getIndexScanEntireHashArrayIndex(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return result;
    }

    public static int getIndexScanEntireHashArrayIndex(long[] hashArray, long key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Checks.checkArrayIndex(hashArray, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final int noIndex = NO_INDEX;

        final int hashArrayLength = hashArray.length;

        int found = noIndex;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (hashArray[i] == key) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (hashArray[i] == key) {

                    found = i;
                    break;
                }
            }
        }

        if (found == noIndex) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).hex("hashArrayIndex", hashArrayIndex));
        }

        return found;
    }

    public static int removeAndReturnIndexScanEntire(long[] hashArray, long key, int keyMask) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int indexOfRemoved = getIndexScanEntireHashArray(hashArray, key, keyMask);

        if (indexOfRemoved == NO_INDEX) {

            throw new IllegalStateException();
        }

        hashArray[indexOfRemoved] = LongNonBucket.NO_ELEMENT;

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexOfRemoved, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return indexOfRemoved;
    }

    public static <P> int removeAndReturnIndexScanToMax(long[] hashArray, long key, int keyMask, byte[] maxDistances) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Objects.requireNonNull(maxDistances);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("keyMask", keyMask).add("maxDistances", maxDistances));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int indexToRemove = removeAndReturnIndexScanToMax(hashArray, key, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (indexToRemove != NO_INDEX) {

            maxDistances[hashArrayIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(MaxDistance.computeDistance(indexToRemove, hashArrayIndex, hashArray.length));
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexToRemove, b -> b.add("key", key).add("keyMask", keyMask).add("maxDistances", maxDistances));
        }

        return indexToRemove;
    }

    private static int removeAndReturnIndexScanToMax(long[] hashArray, long key, int hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int indexToRemove = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(hashArray, key, hashArrayIndex, max);

        if (indexToRemove != NO_INDEX) {

            hashArray[indexToRemove] = LongNonBucket.NO_ELEMENT;
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexToRemove, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return indexToRemove;
    }

    public static <T> int removeAndReturnIndexScanEntire(T[] hashArray, T key, int keyMask, T noKey) {

        Objects.requireNonNull(hashArray);
        Objects.requireNonNull(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("keyMask", keyMask).add("noKey", noKey));
        }

        final int indexOfRemoved = getIndexScanEntireHashArray(hashArray, key, keyMask);

        if (indexOfRemoved == NO_INDEX) {

            throw new IllegalStateException();
        }

        hashArray[indexOfRemoved] = noKey;

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexOfRemoved, b -> b.add("key", key).add("keyMask", keyMask).add("noKey", noKey));
        }

        return indexOfRemoved;
    }

    public static <T> int removeAndReturnIndexScanToMax(T[] hashArray, T key, int keyMask, T noKey, byte[] maxDistances) {

        Objects.requireNonNull(hashArray);
        Objects.requireNonNull(key);
        Objects.requireNonNull(maxDistances);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("keyMask", keyMask).add("noKey", noKey).add("maxDistances", maxDistances));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, keyMask);

        final int indexToRemove = removeAndReturnIndexScanToMax(hashArray, key, hashArrayIndex, noKey, maxDistances[hashArrayIndex]);

        if (indexToRemove != NO_INDEX) {

            maxDistances[hashArrayIndex] = Integers.checkUnsignedIntToUnsignedByteAsByte(MaxDistance.computeDistance(indexToRemove, hashArrayIndex, hashArray.length));
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexToRemove, b -> b.add("key", key).add("keyMask", keyMask).add("noKey", noKey).add("maxDistances", maxDistances));
        }

        return indexToRemove;
    }

    private static <T> int removeAndReturnIndexScanToMax(T[] hashArray, T key, int hashArrayIndex, T noKey, int max) {

        Objects.requireNonNull(hashArray);
        Objects.requireNonNull(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("noKey", noKey).add("max", max));
        }

        final int indexToRemove = getIndexScanHashArrayToMaxHashArrayIndex(hashArray, key, hashArrayIndex, max);

        if (indexToRemove != NO_INDEX) {

            hashArray[indexToRemove] = noKey;
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, indexToRemove, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("noKey", noKey).add("max", max));
        }

        return indexToRemove;
    }
}
