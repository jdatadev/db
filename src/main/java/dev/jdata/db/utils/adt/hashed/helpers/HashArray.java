package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Objects;
import java.util.function.Consumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public class HashArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_HASH_ARRAY;

    private static final boolean ASSERT = AssertionContants.ASSERT_HASH_ARRAY;

    private static final Class<?> debugClass = HashArray.class;

    public static final int NO_INDEX = -1;

    public static int getIndexScanHashArrayToMaxKeyMask(int[] hashArray, int key, int keyMask, int max) {

        Objects.requireNonNull(hashArray);
        NonBucket.checkIsHashArrayElement(key);
        Checks.isLengthAboveZero(max);

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
        NonBucket.checkIsHashArrayElement(element);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; remaining != 0 && i < hashArrayLength; ++ i, -- remaining) {

            if (hashArray[i] == element) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

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
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; remaining != 0 && i < hashArrayLength; ++ i, -- remaining) {

            if (element.equals(hashArray[i])) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

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
        NonBucket.checkIsHashArrayElement(key);
        Checks.isLengthAboveZero(max);

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
        NonBucket.checkIsHashArrayElement(element);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; remaining != 0 && i < hashArrayLength; ++ i, -- remaining) {

            if (hashArray[i] == element) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

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
        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (hashArray[i] == key) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (hashArray[i] == key) {

                    found = i;
                    break;
                }
            }
        }

        if (found == NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
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

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (hashArray[i].equals(key)) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (hashArray[i].equals(key)) {

                    found = i;
                    break;
                }
            }
        }

        if (found == NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return found;
    }

    public static long add(int[] hashArray, int value, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final int noElement = NonBucket.NO_ELEMENT;

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

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

                hashArray[i] = value;

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

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

                    hashArray[i] = value;

                    found = i;
                    break;
                }
            }
        }

        if (ASSERT) {

            if (found == NO_INDEX) {

                throw new IllegalStateException();
            }
        }

        final long result = IntPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    public static final <P> int removeAndReturnIndex(int[] hashArray, int value, int keyMask, P parameter, Consumer<P> decreaseNumElements) {

        Objects.requireNonNull(hashArray);
        NonBucket.checkIsHashArrayElement(value);
        Objects.requireNonNull(decreaseNumElements);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("value", value).add("keyMask", keyMask).add("parameter", parameter).add("decreaseNumElements", decreaseNumElements));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, keyMask);

        if (DEBUG) {

            PrintDebug.debugFormatln(debugClass, "lookup hashArrayIndex=%d value=%d keyMask=0x%08x", hashArrayIndex, value, keyMask);
        }

        final int hashArrayLength = hashArray.length;

        int noIndex = HashArray.NO_INDEX;
        int removedIndex = noIndex;
        final int noElement = NonBucket.NO_ELEMENT;

        boolean done = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

            if (mapKey == value) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "remove from map foundIndex=" + i);
                }

                hashArray[i] = noElement;

                removedIndex = i;
                break;
            }
            else if (mapKey == noElement) {

                done = true;

                break;
            }
        }

        if (removedIndex == noIndex && !done) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final int mapKey = hashArray[i];

                if (mapKey == value) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "remove from map foundIndex=" + i);
                    }

                    hashArray[i] = noElement;

                    removedIndex = i;
                    break;
                }
                else if (mapKey == noElement) {

                    if (ASSERT) {

                        done = true;
                    }

                    break;
                }
            }
        }

        final boolean removed = removedIndex != noIndex;

        if (ASSERT) {

            if (!removed && !done) {

                throw new IllegalStateException();
            }
        }

        if (removed) {

            decreaseNumElements.accept(parameter);
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, removedIndex);
        }

        return removedIndex;
    }

    public static int getIndexScanEntireHashArray(long[] hashArray, long key, int keyMask) {

        Checks.isNotEmpty(hashArray);
        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (hashArray[i] == key) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (hashArray[i] == key) {

                    found = i;
                    break;
                }
            }
        }

        if (found == NO_INDEX) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return found;
    }
}
