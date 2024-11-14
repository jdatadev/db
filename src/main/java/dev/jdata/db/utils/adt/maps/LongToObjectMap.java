package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.AssertionContants;
import dev.jdata.db.utils.adt.DebugConstants;
import dev.jdata.db.utils.adt.hashed.BaseExponentHashed;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.Checks;

public final class LongToObjectMap<T> extends BaseExponentHashed<T[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_TO_OBJECT_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_LONG_TO_OBJECT_MAP;

    private static final long NO_KEY = -1L;

    private long[] keyMap;

    public LongToObjectMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createArray);
    }

    public LongToObjectMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createMap) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createMap);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createMap", createMap));
        }

        this.keyMap = new long[computeCapacity()];

        clearKeyMap(keyMap);

        if (DEBUG) {

            exit();
        }
    }

    public long[] keys() {

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

    public T get(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        final int keyMapLength = keyMap.length;

        final T[] hashed = getHashed();

        T found = null;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            if (keyMap[i] == key) {

                found = hashed[i];
                break;
            }
        }

        if (found == null) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                if (keyMap[i] == key) {

                    found = hashed[i];
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(found, b -> b.add("key", key));
        }

        return found;
    }

    public void put(long key, T value) {

        Checks.isNotNegative(key);
        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value));
        }

        checkCapacity(1);

        final boolean newAdded = put(keyMap, key, getHashed(), value);

        if (newAdded) {

            increaseNumElements();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key).add("value", value));
        }
    }

    public boolean remove(long key) {

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
    protected T[] rehash(T[] map, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("map", map).add("newCapacity", newCapacity));
        }

        final long[] newKeyMap = new long[newCapacity];

        clearKeyMap(newKeyMap);

        final T[] newMap = createHashed(newCapacity);

        final int mapLength = keyMap.length;

        for (int i = 0; i < mapLength; ++ i) {

            final long key = keyMap[i];

            if (key != NO_KEY) {

                put(newKeyMap, key, newMap, map[i]);
            }
        }

        this.keyMap = newKeyMap;

        if (DEBUG) {

            exit(newMap);
        }

        return newMap;
    }

    @Override
    protected void clearHashed() {

        if (DEBUG) {

            enter();
        }

        clearKeyMap(keyMap);

        if (DEBUG) {

            exit();
        }
    }

    private static void clearKeyMap(long[] keyMap) {

        Arrays.fill(keyMap, NO_KEY);
    }

    private boolean put(long[] keyMap, long key, T[] map, T value) {

        if (DEBUG) {

            enter(b -> b.add("keyMap", keyMap).add("key", key).add("map", map).add("value", value));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashTableIndex, key, getKeyMask());
        }

        final int keyMapLength = keyMap.length;

        boolean found = false;

        boolean newAdded = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey == NO_KEY) {

                if (DEBUG) {

                    debug("add to map foundIndex=" + i);
                }

                keyMap[i] = key;
                map[i] = value;

                found = true;

                newAdded = true;
                break;
            }
            else if (mapKey == key) {

                if (DEBUG) {

                    debug("add to map foundIndex=" + i);
                }

                keyMap[i] = key;
                map[i] = value;

                found = true;
                break;
            }
        }

        if (!found) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                final long mapKey = keyMap[i];

                if (mapKey == NO_KEY) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = key;
                    map[i] = value;

                    if (ASSERT) {

                        found = true;
                    }

                    newAdded = true;
                    break;
                }
                else if (mapKey == key) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = key;
                    map[i] = value;

                    if (ASSERT) {

                        found = true;
                    }
                    break;
                }
            }
        }

        if (ASSERT) {

            if (!found) {

                throw new IllegalStateException();
            }
        }

        if (DEBUG) {

            exit(newAdded, b -> b.add("keyMap", keyMap).add("key", key).add("map", map).add("value", value));
        }

        return newAdded;
    }
}
