package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.ILongContainsOnlyPredicate;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class BaseLargeNodeListTest<T extends BaseLargeNodeList<?, ?, ?> & ISingleHeadNodeListGetters & IOnlyElementsView> extends BaseLongNodeListTest {

    abstract T createLargeList(int initialOuterCapacity, int innerCapacity);

    abstract long getValue(T list, long node);

    abstract boolean contains(T list, int value);
    abstract boolean containsOnly(T list, int value);
    abstract boolean containsOnly(T list, int value, ILongContainsOnlyPredicate predicate);

    abstract long[] toArray(T list);

    abstract long addHeadAndReturnNode(T list, int value);
    abstract void addHead(T list, int value);
    abstract long addTailAndReturnNode(T list, int value);

    abstract long removeHeadAndReturnValue(T list);
    abstract long removeNodeAndReturnPreviousNode(T list, long node);

    abstract void clear(T list);

    final void removeHead(T list) {

        removeHeadAndReturnValue(list);
    }

    int removeTailAndReturnValue(T list) {

        throw new UnsupportedOperationException();
    }

    void removeTail(T list) {

        throw new UnsupportedOperationException();
    }

    int removeNodeAndReturnValue(T list, long node) {

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public final void testLengthOne() {

        final T list = createLargeList(1, 1);

        addHead(list, 123);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddHead() {

        final T list = createLargeList(10, 100);

        checkNoElements(list);

        addHead(list, 123);
        checkElements(list, 123);

        addHead(list, 234);
        checkElements(list, 234, 123);

        addHead(list, 345);
        checkElements(list, 345, 234, 123);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddTail() {

        final T list = createLargeList(10, 100);

        checkNoElements(list);

        addTailAndReturnNode(list, 123);
        checkElements(list, 123);

        addTailAndReturnNode(list, 234);
        checkElements(list, 123, 234);

        addTailAndReturnNode(list, 345);
        checkElements(list, 123, 234, 345);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveHead() {

        final T list = createLargeList(10, 100);

        addHead(list, 123);
        addHead(list, 234);
        addHead(list, 345);

        checkElements(list, 345, 234, 123);

        assertThat(removeHeadAndReturnValue(list)).isEqualTo(345);
        checkElements(list, 234, 123);

        assertThat(removeHeadAndReturnValue(list)).isEqualTo(234);
        checkElements(list, 123);

        assertThat(removeHeadAndReturnValue(list)).isEqualTo(123);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveNode() {

        final T list = createLargeList(10, 100);

        final long node1 = addHeadAndReturnNode(list, 123);
        final long node2 = addHeadAndReturnNode(list, 234);
        final long node3 = addHeadAndReturnNode(list, 345);

        checkElements(list, 345, 234, 123);

        checkRemoveNode(list, node2, node3);
        checkElements(list, 345, 123);

        checkRemoveNode(list, node3, NO_NODE);
        checkElements(list, 123);

        checkRemoveNode(list, node1, NO_NODE);

        checkNoElements(list);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final T list = createLargeList(10, 100);

        assertThat(list).isEmpty();

        addHead(list, 123);
        assertThat(list).isNotEmpty();

        addHead(list, 234);
        assertThat(list).isNotEmpty();

        addHead(list, 345);
        assertThat(list).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final T list = createLargeList(10, 100);

        assertThat(list).hasNumElements(0);

        addHead(list, 123);
        assertThat(list).hasNumElements(1);

        addHead(list, 234);
        assertThat(list).hasNumElements(2);

        addHead(list, 345);
        assertThat(list).hasNumElements(3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetValue() {

        final T list = createLargeList(10, 100);

        final long node1 = addHeadAndReturnNode(list, 123);
        final long node2 = addHeadAndReturnNode(list, 234);
        final long node3 = addHeadAndReturnNode(list, 345);

        assertThat(getValue(list, node1)).isEqualTo(123);
        assertThat(getValue(list, node2)).isEqualTo(234);
        assertThat(getValue(list, node3)).isEqualTo(345);
    }

    @Test
    @Category(UnitTest.class)
    public final void testContains() {

        final T list = createLargeList(10, 100);

        addHead(list, 123);

        assertThat(contains(list, 123)).isTrue();
        assertThat(contains(list, 234)).isFalse();

        removeHead(list);

        assertThat(contains(list, 123)).isFalse();
        assertThat(contains(list, 234)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsOnlyValue() {

        final T list = createLargeList(10, 100);

        addHead(list, 123);

        assertThat(containsOnly(list, 123)).isTrue();
        assertThat(containsOnly(list, 234)).isFalse();

        addHead(list, 234);

        assertThat(containsOnly(list, 123)).isFalse();
        assertThat(containsOnly(list, 234)).isFalse();

        addHead(list, 123);

        assertThat(containsOnly(list, 123)).isFalse();
        assertThat(containsOnly(list, 234)).isFalse();

        removeHead(list);
        removeHead(list);

        addHead(list, 123);

        assertThat(containsOnly(list, 123)).isTrue();
        assertThat(containsOnly(list, 234)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsOnlyValueWithPredicate() {

        final T list = createLargeList(10, 100);

        final ILongContainsOnlyPredicate containsOnlyPredicate = (i, l) -> i / 10 == l;

        addHead(list, 123);

        assertThat(containsOnly(list, 1230, containsOnlyPredicate)).isTrue();
        assertThat(containsOnly(list, 2340, containsOnlyPredicate)).isFalse();

        addHead(list, 234);

        assertThat(containsOnly(list, 1230, containsOnlyPredicate)).isFalse();
        assertThat(containsOnly(list, 2340, containsOnlyPredicate)).isFalse();

        addHead(list, 123);

        assertThat(containsOnly(list, 1230, containsOnlyPredicate)).isFalse();
        assertThat(containsOnly(list, 2340, containsOnlyPredicate)).isFalse();

        removeHead(list);
        removeHead(list);

        addHead(list, 123);

        assertThat(containsOnly(list, 1230, containsOnlyPredicate)).isTrue();
        assertThat(containsOnly(list, 2340, containsOnlyPredicate)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetHeadNode() {

        final T list = createLargeList(10, 100);

        final long node1 = addHeadAndReturnNode(list, 123);
        assertThat(list.getHeadNode()).isEqualTo(node1);

        final long node2 = addHeadAndReturnNode(list, 234);
        assertThat(list.getHeadNode()).isEqualTo(node2);

        final long node3 = addHeadAndReturnNode(list, 345);
        assertThat(list.getHeadNode()).isEqualTo(node3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetTailNode() {

        final T list = createLargeList(10, 100);

        final long node1 = addHeadAndReturnNode(list, 123);
        assertThat(list.getTailNode()).isEqualTo(node1);

        addHead(list, 234);
        assertThat(list.getTailNode()).isEqualTo(node1);

        addHead(list, 345);
        assertThat(list.getTailNode()).isEqualTo(node1);
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        final T list = createLargeList(10, 100);

        checkNoElements(list);

        addHead(list, 123);
        checkElements(list, 123);

        clear(list);
        checkNoElements(list);

        addHead(list, 123);
        addHead(list, 234);
        checkElements(list, 234, 123);

        clear(list);
        checkNoElements(list);

        addHead(list, 123);
        addHead(list, 234);
        addHead(list, 345);
        checkElements(list, 345, 234, 123);

        clear(list);
        checkNoElements(list);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemoveHead() {

        final T list = createLargeList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = addHeadAndReturnNode(list, 123);
        checkFreeListIsEmpty(list);

        removeHead(list);
        checkFreeListElements(list, node1);

        final long node2 = addHeadAndReturnNode(list, 234);
        checkFreeListIsEmpty(list);

        removeHead(list);
        checkFreeListElements(list, node2);

        final long node3 = addHeadAndReturnNode(list, 345);
        final long node4 = addHeadAndReturnNode(list, 456);

        checkFreeListIsEmpty(list);

        removeHead(list);
        checkFreeListElements(list, node4);

        removeHead(list);
        checkFreeListElements(list, node3, node4);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemove() {

        final T list = createLargeList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = addHeadAndReturnNode(list, 123);
        checkFreeListIsEmpty(list);

        checkRemoveNode(list, node1, NO_NODE);
        checkFreeListElements(list, node1);

        final long node2 = addHeadAndReturnNode(list, 234);
        checkFreeListIsEmpty(list);

        checkRemoveNode(list, node2, NO_NODE);
        checkFreeListElements(list, node2);

        final long node3 = addHeadAndReturnNode(list, 345);
        final long node4 = addHeadAndReturnNode(list, 456);
        final long node5 = addHeadAndReturnNode(list, 567);

        checkFreeListIsEmpty(list);

        checkRemoveNode(list, node4, node5);
        checkRemoveNode(list, node3, node5);
        checkRemoveNode(list, node5, NO_NODE);

        checkFreeListElements(list, node5, node3, node4);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemoveTail() {

        final T list = createLargeList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = addHeadAndReturnNode(list, 123);
        checkFreeListIsEmpty(list);

        checkRemoveTailNode(list, -1, NO_NODE);
        checkFreeListElements(list, node1);

        final long node2 = addHeadAndReturnNode(list, 234);
        checkFreeListIsEmpty(list);

        checkRemoveTailNode(list, -1, NO_NODE);
        checkFreeListElements(list, node2);

        final long node3 = addHeadAndReturnNode(list, 345);
        final long node4 = addHeadAndReturnNode(list, 456);

        checkFreeListIsEmpty(list);

        checkRemoveTailNode(list, 345, node4);
        checkRemoveTailNode(list, -1, NO_NODE);

        checkFreeListElements(list, node4, node3);
    }

    final void checkAddTailLongDoublyLinkedList() {

        final T list = createLargeList(10, 100);

        addTailAndReturnNode(list, 123);
        addTailAndReturnNode(list, 234);
        addTailAndReturnNode(list, 345);

        checkElements(list, 123, 234, 345);

        assertThat(removeTailAndReturnValue(list)).isEqualTo(345);
        checkElements(list, 123, 234);

        assertThat(removeTailAndReturnValue(list)).isEqualTo(234);
        checkElements(list, 123);

        assertThat(removeTailAndReturnValue(list)).isEqualTo(123);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    final void checkRemoveTailDoublyLinkedList() {

        final T list = createLargeList(10, 100);

        addHead(list, 123);
        addHead(list, 234);
        addHead(list, 345);

        checkElements(list, 345, 234, 123);

        removeTail(list);
        checkElements(list, 345, 234);

        removeTail(list);
        checkElements(list, 345);

        removeTail(list);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    final void checkRemoveAndReturnValueDoublyLinkedList() {

        final T list = createLargeList(10, 100);

        final long node1 = addHeadAndReturnNode(list, 123);
        final long node2 = addHeadAndReturnNode(list, 234);
        final long node3 = addHeadAndReturnNode(list, 345);

        checkElements(list, 345, 234, 123);

        assertThat(removeNodeAndReturnValue(list, node2)).isEqualTo(234);
        checkElements(list, 345, 123);

        assertThat(removeNodeAndReturnValue(list, node3)).isEqualTo(123);
        checkElements(list, 345);

        assertThat(removeNodeAndReturnValue(list, node1)).isEqualTo(345);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    final void checkElements(T list, long ... elements) {

        assertThat(list).isNotEmpty();
        assertThat(list).hasNumElements(elements.length);

        assertThat(toArray(list)).isEqualTo(elements);
    }

    final void checkNoElements(T list) {

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    private static <T extends BaseLargeNodeList<?, ?, ?>> void checkFreeListIsEmpty(T list) {

        assertThat(list.freeListToArray()).isEmpty();
    }

    private static <T extends BaseLargeNodeList<?, ?, ?>> void checkFreeListElements(T list, long ... elements) {

        assertThat(list.freeListToArray()).isEqualTo(elements);
    }

    private void checkRemoveTailNode(T list, long expectedValue, long expectedNewTailNode) {

        final long value = removeTailAndReturnValue(list);

        if (expectedValue == -1) {

            assertThat(list).isEmpty();
        }
        else {
            assertThat(value).isEqualTo(expectedValue);
        }

        assertThat(list.getTailNode()).isEqualTo(expectedNewTailNode);
    }

    private void checkRemoveNode(T list, long node, long expectedPreviousNode) {

        final long previousNode = removeNodeAndReturnPreviousNode(list, node);

        assertThat(previousNode).isEqualTo(expectedPreviousNode);
    }
}
