package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;

import dev.jdata.db.utils.adt.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;

public final class IntSet extends BaseIntegerSet<int[]> {

    private static final boolean DEBUG = DebugConstants.DEBUG_INT_SET;

    private static final boolean ASSERT = AssertionContants.ASSERT_INT_SET;

    private static final int NO_ELEMENT = -1;

    public IntSet(int initialCapacityExponent) {
        this(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public IntSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor, int[]::new);

        clearHashed();
    }

    public boolean contains(int element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        final int[] set = getHashed();

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

    public void add(int value) {

        Checks.isNotNegative(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final boolean newAdded = add(getHashed(), value);

        if (newAdded) {

            increaseNumElements();
        }

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    public boolean remove(int element) {

        Checks.isNotNegative(element);

        if (DEBUG) {

            enter(b -> b.add("element", element));
        }

        final int hashSetIndex = HashFunctions.hashIndex(element, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashTableIndex=%d element=%d keyMask=0x%08x", hashSetIndex, element, getKeyMask());
        }

        final int[] set = getHashed();

        final int setLength = set.length;

        boolean removed = false;

        boolean done = false;

        for (int i = hashSetIndex; i < setLength; ++ i) {

            final int setElement = set[i];

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

                final int setElement = set[i];

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

            decreaseNumElements();
        }

        if (DEBUG) {

            exit(removed);
        }

        return removed;
    }

    @Override
    protected int[] rehash(int[] set, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("set", set).add("newCapacity", newCapacity));
        }

        final int[] newSet = new int[newCapacity];

        clearSet(newSet);

        final int setLength = set.length;

        for (int i = 0; i < setLength; ++ i) {

            final int element = set[i];

            if (element != NO_ELEMENT) {

                add(newSet, element);
            }
        }

        if (DEBUG) {

            exit(newSet);
        }

        return newSet;
    }

    @Override
    protected void clearHashed() {

        clearSet(getHashed());
    }

    private static void clearSet(int[] set) {

        Arrays.fill(set, NO_ELEMENT);
    }

    private boolean add(int[] set, int value) {

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

                final int setElement = set[i];

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
}
