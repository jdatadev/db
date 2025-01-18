package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;

import dev.jdata.db.utils.bits.BitsUtil;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseLargeList<T, U extends BaseValues<T, BaseInnerOuterList<T, U>, U>> extends BaseInnerOuterList<T, U> {

    public static boolean isEmpty(long headNode, long tailNode) {

        final boolean isEmpty;

        if (headNode == NO_NODE) {

            if (tailNode != NO_NODE) {

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
    interface BaseValuesFactory<T, U extends BaseValues<T, BaseInnerOuterList<T, U>, U>> {

        U create(int initialOuterCapacity);
    }

    private final int innerCapacity;

    private long[][] next;

    private int numOuterEntries;

    private long freeListHead;

    abstract void reallocateOuter(int newOuterLength);
    abstract void allocateInner(int outerIndex, int innerCapacity);

    BaseLargeList(int initialOuterCapacity, int innerCapacity, BaseValuesFactory<T, U> valuesFactory) {
        super(valuesFactory.create(initialOuterCapacity), 32, BitsUtil.maskLong(32, 0));

        Checks.isCapacity(initialOuterCapacity);
        Checks.isCapacity(innerCapacity);

        this.innerCapacity = innerCapacity;

        this.next = new long[initialOuterCapacity][];

        next[0] = allocateListNodes(innerCapacity);

        getValues().allocateInner(0, innerCapacity);

        this.numOuterEntries = 1;

        this.freeListHead = NO_NODE;
    }

    @Override
    public final long getNextNode(long node) {

        return getNode(next, node);
    }

    final void addNodeToFreeList(long node) {

        addNodeToFreeList(next, node);
    }

    private void addNodeToFreeList(long[][] nodes, long node) {

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

    final long allocateNextNode() {

        return allocateNode(next);
    }

    private long allocateNode(long[][] next) {

        final long result;

        if (freeListHead != NO_NODE) {

            result = freeListHead;

            this.freeListHead = getNode(next, freeListHead);
        }
        else {
            long[] array = next[numOuterEntries - 1];

            int numNodes = (int)array[0];

            if (numNodes == array.length - 1) {

                final U values = getValues();

                final int currentOuterLength = next.length;

                if (numOuterEntries == currentOuterLength) {

                    final int newOuterLength = currentOuterLength * 4;

                    reallocateOuter(newOuterLength);

                    values.reallocateOuter(newOuterLength);

                    this.next = Arrays.copyOf(next, newOuterLength);
                }

                array = next[numOuterEntries] = allocateListNodes(numNodes);

                allocateInner(numOuterEntries, innerCapacity);

                values.allocateInner(numOuterEntries ++,  innerCapacity);

                numNodes = 0;
            }

            ++ array[0];

            result = ((numOuterEntries - 1) << 32) | (numNodes + 1);
        }

        return result;
    }

    final long checkHeadNode(long headNode) {

        if (headNode == NO_NODE) {

            throw new IllegalStateException();
        }

        return headNode;
    }

    final long checkTailNode(long tailNode) {

        if (tailNode == NO_NODE) {

            throw new IllegalStateException();
        }

        return tailNode;
    }

    final long allocateNode() {

        return allocateNode(next);
    }

    final void setNextNode(long node, long value) {

        setNode(next, node, value);
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
