package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public final class LongSet extends BaseIntegerSet<long[]> implements ILongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_LONG_SET;

    private static final boolean ASSERT = AssertionContants.ASSERT_LONG_SET;

    private static final long NO_ELEMENT = -1L;

    public static LongSet of(long ... values) {

        return new LongSet(values);
    }

    public LongSet() {
        this(3);
    }

    public LongSet(int initialCapacityExponent) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public LongSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor, long[]::new, LongSet::clearSet);
    }

    private LongSet(long[] values) {
        this(computeCapacityExponent(values.length, HashedConstants.DEFAULT_LOAD_FACTOR), HashedConstants.DEFAULT_LOAD_FACTOR);

        for (long value : values) {

            add(value);
        }
    }

    @Override
    public boolean contains(long element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        final long[] set = getHashed();

        final int setLength = set.length;

        boolean found = false;

        for (int i = hashSetIndex; i < setLength; ++ i) {

            if (set[i] == element) {

                found = true;
                break;
            }
        }

        if (!found) {

            for (int i = 0; i < hashSetIndex; ++ i) {

                if (set[i] == element) {

                    found = true;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(found, b -> b.add("element", element));
        }

        return found;
    }

    public void add(long value) {

        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final boolean newAdded = add(getHashed(), value);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    public boolean remove(long element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d key=%d keyMask=0x%08x", hashSetIndex, element, getKeyMask());
        }

        final long[] set = getHashed();

        final int setLength = set.length;

        boolean removed = false;

        boolean done = false;

        for (int i = hashSetIndex; i < setLength; ++ i) {

            final long setElement = set[i];

            if (setElement == element) {

                if (DEBUG) {

                    debug("add to set foundIndex=" + i);
                }

                set[i] = NO_ELEMENT;

                removed = true;
                break;
            }
            else if (setElement == NO_ELEMENT) {

                done = true;

                break;
            }
        }

        if (!removed && !done) {

            for (int i = 0; i < hashSetIndex; ++ i) {

                final long setElement = set[i];

                if (setElement == element) {

                    if (DEBUG) {

                        debug("add to set foundIndex=" + i);
                    }

                    set[i] = NO_ELEMENT;

                    removed = true;
                    break;
                }
                else if (setElement == NO_ELEMENT) {

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

            decrementNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    @Override
    protected long[] rehash(long[] set, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("newCapacity", newCapacity));
        }

        final long[] newSet = new long[newCapacity];

        clearSet(newSet);

        final int setLength = set.length;

        for (int i = 0; i < setLength; ++ i) {

            final long element = set[i];

            if (element != NO_ELEMENT) {

                add(newSet, element);
            }
        }

        if (DEBUG) {

            exit(newSet);
        }

        return newSet;
    }

    private boolean add(long[] set, long value) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("value", value));
        }

        final int hashSetIndex = HashFunctions.hashIndex(value, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashSetIndex=%d value=%d keyMask=0x%08x", hashSetIndex, value, getKeyMask());
        }

        final int setLength = set.length;

        boolean found = false;

        boolean newAdded = false;

        for (int i = hashSetIndex; i < setLength; ++ i) {

            final long setElement = set[i];

            if (setElement == NO_ELEMENT) {

                if (DEBUG) {

                    debug("add to set foundIndex=" + i);
                }

                set[i] = value;

                found = true;

                newAdded = true;
                break;
            }
            else if (setElement == value) {

                if (DEBUG) {

                    debug("add to set foundIndex=" + i);
                }

                set[i] = value;

                found = true;
                break;
            }
        }

        if (!found) {

            for (int i = 0; i < hashSetIndex; ++ i) {

                final long setElement = set[i];

                if (setElement == NO_ELEMENT) {

                    if (DEBUG) {

                        debug("add to set foundIndex=" + i);
                    }

                    set[i] = value;

                    if (ASSERT) {

                        found = true;
                    }

                    newAdded = true;
                    break;
                }
                else if (setElement == value) {

                    if (DEBUG) {

                        debug("add to set foundIndex=" + i);
                    }

                    set[i] = value;

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

            exit(newAdded, b -> b.add("set", set).add("value", value));
        }

        return newAdded;
    }

    private static void clearSet(long[] set) {

        Arrays.fill(set, NO_ELEMENT);
    }

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder(Integers.checkUnsignedLongToUnsignedInt(getNumElements() * 10));

        sb.append(getClass().getSimpleName()).append(" [elements=");

        final long[] set = getHashed();

        Array.toString(set, 0, set.length, sb, e -> e != NO_ELEMENT);

        sb.append(']');

        return sb.toString();
    }
}
