package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseLongMap<T> extends BaseExponentMap<long[]> implements LongKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_LONG_MAP;

    private static final long NO_KEY = -1L;

    protected static final int NO_INDEX= -1;

    private final IntFunction<T> createValues;

    private T values;

    protected abstract void put(T values, int index, T newValues, int newIndex);
    protected abstract void clearValues(T values);

    protected BaseLongMap(int initialCapacityExponent, float loadFactor, IntFunction<T> createValues) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createValues);
    }

    BaseLongMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new, BaseLongMap::clearKeyMap);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createValues", createValues));
        }

        this.createValues= Objects.requireNonNull(createValues);

        this.values = createValues.apply(computeCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean containsKey(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final boolean result = index != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final long[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final long[] result = new long[numElements];

        keys(result);

        if (DEBUG) {

            exit();
        }

        return result;
    }

    final void keys(long[] dst) {

        keysAndValues(dst, null, null, null);
    }

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValue<long[], T, P1, P2> forEachKeyAndValue) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        final long[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        final T values = getValues();

        for (int i = 0; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey != NO_KEY) {

                forEachKeyAndValue.each(keyMap, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <S, D> void keysAndValues(long[] keysDst, S src, D dst, ValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final long[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int dstIndex = 0;

        for (int i = 0; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey != NO_KEY) {

                keysDst[dstIndex] = mapKey;

                if (dst != null) {

                    valueSetter.setValue(src, i, dst, dstIndex);
                }

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final int getIndex(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        final long[] keyMap = getHashed();

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

    protected final int put(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final long[] keyMap = getHashed();

        final long putResult = put(keyMap, key);

        final int index = getPutIndex(putResult);

        final boolean newAdded = getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(index, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return index;
    }

    @Override
    public final boolean remove(long key) {

        return removeAndReturnIndex(key) != NO_INDEX;
    }

    final int removeAndReturnIndex(long key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashTableIndex, key, getKeyMask());
        }

        final long[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int removedIndex = NO_INDEX;

        boolean done = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final long mapKey = keyMap[i];

            if (mapKey == key) {

                if (DEBUG) {

                    debug("add to map foundIndex=" + i);
                }

                keyMap[i] = NO_KEY;

                removedIndex = i;
                break;
            }
            else if (mapKey == NO_KEY) {

                done = true;

                break;
            }
        }

        if (removedIndex == NO_INDEX && !done) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                final long mapKey = keyMap[i];

                if (mapKey == key) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = NO_KEY;

                    removedIndex = i;
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

        final boolean removed = removedIndex != NO_INDEX;

        if (ASSERT) {

            if (!removed && !done) {

                throw new IllegalStateException();
            }
        }

        if (removed) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removedIndex);
        }

        return removedIndex;
    }

    @Override
    protected final long[] rehash(long[] keyMap, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("keyMap", keyMap).add("newCapacity", newCapacity));
        }

        final long[] newKeyMap = new long[newCapacity];

        clearKeyMap(newKeyMap);

        final T newValuesArray = createValues.apply(newCapacity);

        final int mapLength = keyMap.length;

        for (int i = 0; i < mapLength; ++ i) {

            final long key = keyMap[i];

            if (key != NO_KEY) {

                final int newIndex = getPutIndex(put(newKeyMap, key));

                newKeyMap[newIndex] = key;

                put(values, i, newValuesArray, newIndex);
            }
        }

        this.values = newValuesArray;

        if (DEBUG) {

            exit(newKeyMap);
        }

        return newKeyMap;
    }

    protected final T getValues() {
        return values;
    }

    static boolean getPutNewAdded(long putResult) {

        return putResult >>> 32 != 0L;
    }

    protected static int getPutIndex(long putResult) {

        return (int)(putResult & 0xFFFFFFFFL);
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

                    debug("add to map new foundIndex=" + i);
                }

                keyMap[i] = key;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey == key) {

                if (DEBUG) {

                    debug("add to map existing foundIndex=" + i);
                }

                keyMap[i] = key;

                found = i;
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

    private static void clearKeyMap(long[] keyMap) {

        Arrays.fill(keyMap, NO_KEY);
    }
}
