package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseObjectMap<K, T> extends BaseExponentMap<K[]> implements ObjectKeyMap<K> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_INT_MAP;

    private static final Object NO_KEY = null;

    private final IntFunction<T> createValues;

    private T values;

    protected abstract void put(T values, int index, T newValues, int newIndex);
    protected abstract void clearValues(T values);

    protected BaseObjectMap(int initialCapacityExponent, float loadFactor, IntFunction<K[]> createKeyArray, IntFunction<T> createValues) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createKeyArray, createValues);
    }

    BaseObjectMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeyArray, IntFunction<T> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, BaseObjectMap::clearKeyMap);

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
    public final boolean containsKey(K key) {

        Objects.requireNonNull(key);

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
    public final K[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final K[] result = createHashedNonCleared(numElements);

        keys(result);

        if (DEBUG) {

            exit();
        }

        return result;
    }

    public final int keys(K[] dst) {

        return keysAndValues(dst, null, null, null);
    }

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValue<K[], T, P1, P2> forEachKeyAndValue) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        final K[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        final T values = getValues();

        for (int i = 0; i < keyMapLength; ++ i) {

            final K mapKey = keyMap[i];

            if (mapKey != NO_KEY) {

                forEachKeyAndValue.each(keyMap, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <S, D> int keysAndValues(K[] keysDst, S src, D dst, ValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final K[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int dstIndex = 0;

        for (int i = 0; i < keyMapLength; ++ i) {

            final K mapKey = keyMap[i];

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

    protected final int getIndex(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.objectHashIndex(key, getKeyMask());

        final K[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int found = NO_INDEX;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final K keyMapKey = keyMap[i];

            if (keyMapKey != NO_KEY && keyMapKey.equals(key)) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                final K keyMapKey = keyMap[i];

                if (keyMapKey != NO_KEY && keyMapKey.equals(key)) {

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

    protected final int put(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final K[] keyMap = getHashed();

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
    public final boolean remove(K key) {

        return removeAndReturnIndex(key) != NO_INDEX;
    }

    final int removeAndReturnIndex(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashTableIndex = HashFunctions.objectHashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%s keyMask=0x%08x", hashTableIndex, key.toString(), getKeyMask());
        }

        final K[] keyMap = getHashed();

        final int keyMapLength = keyMap.length;

        int removedIndex = NO_INDEX;

        boolean done = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final K mapKey = keyMap[i];

            if (mapKey == NO_KEY) {

                done = true;

                break;
            }
            else if (mapKey.equals(key)) {

                if (DEBUG) {

                    debug("remove from map foundIndex=" + i);
                }

                @SuppressWarnings("unchecked")
                final K noKey = (K)NO_KEY;

                keyMap[i] = noKey;

                removedIndex = i;
                break;
            }
        }

        if (removedIndex == NO_INDEX && !done) {

            for (int i = 0; i < hashTableIndex; ++ i) {

                final K mapKey = keyMap[i];

                if (mapKey == NO_KEY) {

                    if (ASSERT) {

                        done = true;
                    }

                    break;
                }
                else if (mapKey.equals(key)) {

                    if (DEBUG) {

                        debug("remove from map foundIndex=" + i);
                    }

                    @SuppressWarnings("unchecked")
                    final K noKey = (K)NO_KEY;

                    keyMap[i] = noKey;

                    removedIndex = i;
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
    protected final K[] rehash(K[] keyMap, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("keyMap", keyMap).add("newCapacity", newCapacity));
        }

        final K[] newKeyMap = createHashedNonCleared(newCapacity);

        clearKeyMap(newKeyMap);

        final T newValuesArray = createValues.apply(newCapacity);

        final int mapLength = keyMap.length;

        for (int i = 0; i < mapLength; ++ i) {

            final K key = keyMap[i];

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

    private long put(K[] keyMap, K key) {

        if (DEBUG) {

            enter(b -> b.add("keyMap", keyMap).add("key", key));
        }

        final int hashTableIndex = HashFunctions.objectHashIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%s keyMask=0x%08x", hashTableIndex, key.toString(), getKeyMask());
        }

        final int keyMapLength = keyMap.length;

        int found = NO_INDEX;

        boolean newAdded = false;

        for (int i = hashTableIndex; i < keyMapLength; ++ i) {

            final K mapKey = keyMap[i];

            if (mapKey == NO_KEY) {

                if (DEBUG) {

                    debug("add to map new foundIndex=" + i);
                }

                keyMap[i] = key;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey.equals(key)) {

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

                final K mapKey = keyMap[i];

                if (mapKey == NO_KEY) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    keyMap[i] = key;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey.equals(key)) {

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

    private static <K> void clearKeyMap(K[] keyMap) {

        Arrays.fill(keyMap, NO_KEY);
    }
}
