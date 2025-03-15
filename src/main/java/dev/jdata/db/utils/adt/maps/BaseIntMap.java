package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseIntMap<T> extends BaseExponentMap<int[]> implements IntKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_INT_MAP;

    private static final int NO_KEY = -1;

    private final IntFunction<T> createValues;

    private T values;

    protected abstract void put(T values, int index, T newValues, int newIndex);
    protected abstract void clearValues(T values);

    protected BaseIntMap(int initialCapacityExponent, float loadFactor, IntFunction<T> createValues) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createValues);
    }

    BaseIntMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntMap::clearKeyMap);

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
    public final boolean containsKey(int key) {

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
    public final int[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final int[] result = new int[numElements];

        keys(result);

        if (DEBUG) {

            exit();
        }

        return result;
    }

    public final int keys(int[] dst) {

        return keysAndValues(dst, null, null, null);
    }

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValue<int[], T, P1, P2> forEachKeyAndValue) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        final int[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        final T values = getValues();

        for (int i = 0; i < keyMapLength; ++ i) {

            final int mapKey = keyMap[i];

            if (mapKey != NO_KEY) {

                forEachKeyAndValue.each(keyMap, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <S, D> int keysAndValues(int[] keysDst, S src, D dst, ValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final int[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int dstIndex = 0;

        for (int i = 0; i < keyMapLength; ++ i) {

            final int mapKey = keyMap[i];

            if (mapKey != NO_KEY) {

                keysDst[dstIndex] = mapKey;

                if (dst != null) {

                    valueSetter.setValue(src, i, dst, dstIndex);
                }

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit(dstIndex);
        }

        return dstIndex;
    }

    protected final int getIndex(int key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        final int[] keyMap = getHashed();

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

    protected final int put(int key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final int[] keyMap = getHashed();

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
    public final boolean remove(int key) {

        return removeAndReturnIndex(key) != NO_INDEX;
    }

    final int removeAndReturnIndex(int key) {

        Checks.isNotNegative(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.hashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashTableIndex, key, getKeyMask());
        }

        final int[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int removedIndex = NO_INDEX;

        boolean done = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final int mapKey = keyMap[i];

            if (mapKey == key) {

                if (DEBUG) {

                    debug("remove from map foundIndex=" + i);
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

                final int mapKey = keyMap[i];

                if (mapKey == key) {

                    if (DEBUG) {

                        debug("remove from map foundIndex=" + i);
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
    protected final int[] rehash(int[] keyMap, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("keyMap", keyMap).add("newCapacity", newCapacity));
        }

        final int[] newKeyMap = new int[newCapacity];

        clearKeyMap(newKeyMap);

        final T newValuesArray = createValues.apply(newCapacity);

        final int mapLength = keyMap.length;

        for (int i = 0; i < mapLength; ++ i) {

            final int key = keyMap[i];

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

    @Deprecated // move to base class?
    static boolean getPutNewAdded(long putResult) {

        return putResult >>> 32 != 0L;
    }

    @Deprecated // move to base class?
    protected static int getPutIndex(long putResult) {

        return (int)(putResult & 0xFFFFFFFFL);
    }

    private long put(int[] keyMap, int key) {

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

            final int mapKey = keyMap[i];

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

                final int mapKey = keyMap[i];

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

    private static void clearKeyMap(int[] keyMap) {

        Arrays.fill(keyMap, NO_KEY);
    }
}
