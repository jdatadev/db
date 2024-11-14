package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.adt.BaseElements;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseLongList extends BaseElements implements LongListIterable {

    public static boolean isEmpty(long head, long tail) {

        final boolean isEmpty;

        if (head == NO_NODE) {

            if (tail == NO_NODE) {

                throw new IllegalStateException();
            }

            isEmpty = true;
        }
        else {
            isEmpty = false;
        }

        return isEmpty;
    }

    @FunctionalInterface
    public interface LongNodeSetter<T> {

        void setNode(T list, long node);
    }

    static final long NO_NODE = -1L;

    private final int innerCapacity;

    long[][] next;

    private long[][] values;

    private int numOuterEntries;

    private long freeListHead;

    abstract void reallocateOuter(int newOuterLength);

    BaseLongList(int initialOuterCapacity, int innerCapacity) {

        Checks.isCapacity(initialOuterCapacity);
        Checks.isCapacity(innerCapacity);

        this.innerCapacity = innerCapacity;

        this.next = new long[initialOuterCapacity][];

        next[0] = allocateListNodes(innerCapacity);

        this.values = new long[innerCapacity][];

        values[0] = new long[innerCapacity];

        this.numOuterEntries = 1;

        this.freeListHead = NO_NODE;
    }

    @Override
    public final long getNextNode(long node) {

        return getNode(next, node);
    }

    final boolean containsValue(long value, long head) {

        boolean found = false;

        for (long node = head; node != NO_NODE; node = getNextNode(node)) {

            if (getValue(node) == value) {

                found = true;
                break;
            }
        }

        return found;
    }

    final long[] toArray(long headNode) {

        final long[] result = new long[getNumElements()];

        int dstIndex = 0;

        for (long node = headNode; node != NO_NODE; node = getNextNode(node)) {

            result[dstIndex ++] = getValue(node);
        }

        return result;
    }

    final void addNodeToFreeList(long[][] nodes, long node) {

        setNode(nodes, node, freeListHead);

        this.freeListHead = node;
    }

    final long[] freeListToArray() {

        int numNodes = 0;

        for (long node = freeListHead; node != NO_NODE; node = getNextNode(node)) {

            ++ numNodes;
        }

        final long[] result = new long[numNodes];

        int dstIndex = 0;

        for (long node = freeListHead; node != NO_NODE; node = getNextNode(node)) {

            result[dstIndex ++] = node;
        }

        return result;
    }

    final long allocateNode(long[][] next) {

        final long result;

        if (freeListHead != NO_NODE) {

            result = freeListHead;

            this.freeListHead = getNode(next, freeListHead);
        }
        else {
            long[] array = next[numOuterEntries - 1];

            int numNodes = (int)array[0];

            if (numNodes == array.length - 1) {

                final int currentOuterLength = next.length;

                if (numOuterEntries == currentOuterLength) {

                    final int newOuterLength = currentOuterLength * 4;

                    reallocateOuter(newOuterLength);

                    this.next = Arrays.copyOf(next, newOuterLength);
                    this.values = Arrays.copyOf(values, newOuterLength);
                }

                array = next[numOuterEntries] = allocateListNodes(numNodes);

                allocateInner(numOuterEntries ++,  innerCapacity);

                numNodes = 0;
            }

            ++ array[0];

            result = ((numOuterEntries - 1) << 32) | (numNodes + 1);
        }

        return result;
    }

    final long getValue(long node) {

        return getNode(values, node);
    }

    final void setValue(long node, long value) {

        setNode(values, node, value);
    }

    final void allocateInner(int outerIndex, int innerCapacity) {

        Checks.isIndex(outerIndex);
        Checks.isCapacity(innerCapacity);

        values[outerIndex] = new long[innerCapacity];
    }

    final long getHeadNode(long head) {

        if (head == NO_NODE) {

            throw new IllegalStateException();
        }

        return head;
    }

    final long getTailNode(long tail) {

        if (tail == NO_NODE) {

            throw new IllegalStateException();
        }

        return tail;
    }

    final long allocateNode() {

        return allocateNode(next);
    }

    final void setNextNode(long node, long value) {

        setNode(next, node, value);
    }

    final void addNodeToFreeList(long node) {

        addNodeToFreeList(next, node);
    }

    static long[] allocateListNodes(int capacity) {

        final long[] nodes = new long[capacity];

        Arrays.fill(nodes, NO_NODE);

        nodes[0] = 0;

        return nodes;
    }

    static long getNode(long[][] list, long node) {

        return list[(int)(node >> 32)][(int)(node & 0xFFFFFFFF) + 1];
    }

    static void setNode(long[][] list, long node, long value) {

        list[(int)(node >> 32)][(int)(node & 0xFFFFFFFF) + 1] = value;
    }
}
