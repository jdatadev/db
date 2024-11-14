package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.DebugConstants;
import dev.jdata.db.utils.adt.hashed.BaseExponentHashed;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongArrayMap<T> extends BaseExponentHashed<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_ARRAY_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_LONG_ARRAY_MAP;

    private static final long NO_KEY = -1L;

    static final int NO_INDEX= -1;

    abstract void put(T map, int index, T newMap, int newIndex);

    private long[] keyMap;

    public BaseLongArrayMap(int initialCapacityExponent, IntFunction<T> createArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createArray);
    }

    public BaseLongArrayMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createMap", createArray));
        }

        this.keyMap = new long[computeCapacity()];

        clearKeyMap(keyMap);

        if (DEBUG) {

            exit();
        }
    }

    public final long[] keys() {

        if (DEBUG) {

            enter();
        }

        final long[] result = new long[getNumElements()];

        final int keyMapLength = keyMap.length;

        int dstIndex = 0;

        for (int i = 0; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey != NO_KEY) {

                result[dstIndex ++] = mapKey;
            }
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final int getIndex(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        final int keyMapLength = keyMap.length;

        int found = NO_INDEX;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            if (keyMap[i] == key) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                if (keyMap[i] == key) {

                    found = i;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(found, b -> b.add("key", key));
        }

        return found;
    }

    final int put(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final long putResult = put(keyMap, key);

        final int index = getPutIndex(putResult);

        final boolean newAdded = getPutNewAdded(putResult);

        if (newAdded) {

            increaseNumElements();
        }

        if (DEBUG) {

            exit(index, b -> b.add("key", key));
        }

        return index;
    }

    public final boolean remove(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashTableIndex, key, getKeyMask());
        }

        final int keyMapLength = keyMap.length;

        boolean removed = false;

        boolean done = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey == key) {

                if (DEBUG) {

                    debug("add to map foundIndex=" + i);
                }

                keyMap[i] = NO_KEY;

                removed = true;
                break;
            }
            else if (mapKey == NO_KEY) {

                done = true;

                break;
            }
        }

        if (!removed && !done) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                final long mapKey = keyMap[i];

                if (mapKey == key) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = NO_KEY;

                    removed = true;
                    break;
                }
                else if (mapKey == NO_KEY) {

                    if (ASSERT) {

                        done = true;
                    }

                    break;
                }
            }
        }

        if (ASSERT) {

            if (!removed && !done) {

                throw new IllegalStateException();
            }
        }

        if (removed) {

            decreaseNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    @Override
    protected final T rehash(T map, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("map", map).add("newCapacity", newCapacity));
        }

        final long[] newKeyMap = new long[newCapacity];

        clearKeyMap(newKeyMap);

        final T newMap = createHashed(newCapacity);

        final int mapLength = keyMap.length;

        for (int i = 0; i < mapLength; ++ i) {

            final long key = keyMap[i];

            if (key != NO_KEY) {

                final int index = getPutIndex(put(newKeyMap, key));

                put(map, i, newMap, index);
            }
        }

        this.keyMap = newKeyMap;

        if (DEBUG) {

            exit(newMap);
        }

        return newMap;
    }

    @Override
    protected final void clearHashed() {

        if (DEBUG) {

            enter();
        }

        clearKeyMap(keyMap);

        if (DEBUG) {

            exit();
        }
    }

    static boolean getPutNewAdded(long putResult) {

        return putResult >>> 32 != 0L;
    }

    static int getPutIndex(long putResult) {

        return (int)(putResult & 0xFFFFFFFFL);
    }

    private static void clearKeyMap(long[] keyMap) {

        Arrays.fill(keyMap, NO_KEY);
    }

    private long put(long[] keyMap, long key) {

        if (DEBUG) {

            enter(b -> b.add("keyMap", keyMap).add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashTableIndex, key, getKeyMask());
        }

        final int keyMapLength = keyMap.length;

        int found = NO_INDEX;

        boolean newAdded = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey == NO_KEY) {

                if (DEBUG) {

                    debug("add to map foundIndex=" + i);
                }

                keyMap[i] = key;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey == key) {

                if (DEBUG) {

                    debug("add to map foundIndex=" + i);
                }

                keyMap[i] = key;

                found = NO_INDEX;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                final long mapKey = keyMap[i];

                if (mapKey == NO_KEY) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = key;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey == key) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = key;

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

        final long result = ((newAdded ? 1L : 0L) << 32) | found;

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("keyMap", keyMap).add("key", key));
        }

        return result;
    }
}
