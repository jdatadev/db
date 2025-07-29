package dev.jdata.db.utils.adt.hashed.helpers;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeIntArray;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

public class LargeHashArray {

    private static final boolean DEBUG = DebugConstants.DEBUG_LARGE_HASH_ARRAY;

    private static final boolean ASSERT = AssertionContants.ASSERT_LARGE_HASH_ARRAY;

    private static final Class<?> debugClass = LargeHashArray.class;

    public static final long NO_INDEX = -1L;

    public static long getIndexScanHashArrayToMaxHashArrayIndex(LargeIntArray hashArray, int element, long hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(element);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final long hashArrayLimit = hashArray.getLimit();

        final long noIndex = NO_INDEX;

        long found = noIndex;

        for (long i = hashArrayIndex; remaining != 0 && i < hashArrayLimit; ++ i, -- remaining) {

            if (hashArray.get(i) == element) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; remaining != 0 && i < hashArrayIndex; ++ i, -- remaining) {

                if (hashArray.get(i) == element) {

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

    public static long getIndexScanHashArrayToMaxHashArrayIndex(LargeLongArray hashArray, long element, long hashArrayIndex, int max) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(element);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("element", element).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        int remaining = max + 1;

        final long hashArrayLimit = hashArray.getLimit();

        final long noIndex = NO_INDEX;

        long found = noIndex;

        for (long i = hashArrayIndex; remaining != 0 && i < hashArrayLimit; ++ i, -- remaining) {

            if (hashArray.get(i) == element) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (int i = 0; remaining != 0 && i < hashArrayIndex; ++ i, -- remaining) {

                if (hashArray.get(i) == element) {

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

    public static long getIndexScanEntireHashArray(LargeIntArray hashArray, int key, int keyMask) {

        Checks.isNotEmpty(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final long hashArrayLimit = hashArray.getLimit();

        final long noIndex = NO_INDEX;

        long found = noIndex;

        for (long i = hashArrayIndex; i < hashArrayLimit; ++ i) {

            if (hashArray.get(i) == key) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (long i = 0L; i < hashArrayIndex; ++ i) {

                if (hashArray.get(i) == key) {

                    found = i;
                    break;
                }
            }
        }

        if (found == noIndex) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return found;
    }

    public static long getIndexScanEntireHashArray(LargeLongArray hashArray, long key, long keyMask) {

        Checks.isNotEmpty(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(key, keyMask);

        final long hashArrayLimit = hashArray.getLimit();

        final long noIndex = NO_INDEX;

        long found = noIndex;

        for (long i = hashArrayIndex; i < hashArrayLimit; ++ i) {

            if (hashArray.get(i) == key) {

                found = i;
                break;
            }
        }

        if (found == noIndex) {

            for (long i = 0L; i < hashArrayIndex; ++ i) {

                if (hashArray.get(i) == key) {

                    found = i;
                    break;
                }
            }
        }

        if (found == noIndex) {

            throw new IllegalStateException();
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, found, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return found;
    }

    public static long add(LargeIntArray hashArray, int value, long hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long noElement = LongNonBucket.NO_ELEMENT;

        final long hashArrayLength = hashArray.getLimit();

        final long noIndex = NO_INDEX;

        long found = noIndex;

        boolean newAdded = false;

        for (long i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray.get(i);

            if (mapKey == noElement) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map new foundIndex=" + i);
                }

                hashArray.set(i, value);

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

            for (long i = 0L; i < hashArrayIndex; ++ i) {

                final int mapKey = hashArray.get(i);

                if (mapKey == noElement) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    hashArray.set(i, value);

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

        final long result = LongPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    public static long add(LargeLongArray hashArray, long value, long hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long noElement = LongNonBucket.NO_ELEMENT;

        final long hashArrayLength = hashArray.getLimit();

        final long noIndex = NO_INDEX;

        long found = noIndex;

        boolean newAdded = false;

        for (long i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray.get(i);

            if (mapKey == noElement) {

                if (DEBUG) {

                    PrintDebug.debug(debugClass, "add to map new foundIndex=" + i);
                }

                hashArray.set(i, value);

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

            for (long i = 0L; i < hashArrayIndex; ++ i) {

                final long mapKey = hashArray.get(i);

                if (mapKey == noElement) {

                    if (DEBUG) {

                        PrintDebug.debug(debugClass, "add to map foundIndex=" + i);
                    }

                    hashArray.set(i, value);

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

        final long result = LongPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            PrintDebug.exitWithBinary(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }
}
