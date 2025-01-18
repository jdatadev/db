package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.elements.Elements;
import dev.jdata.db.utils.adt.lists.LongList.ContainsOnlyPredicate;

abstract class BaseLargeListTest<T extends BaseLargeList<?, ?> & LargeList & Elements> extends BaseTest {

    abstract T createLargeList(int initialOuterCapacity, int innerCapacity);

    abstract long getValue(T list, long node);

    abstract boolean contains(T list, long value);
    abstract boolean containsOnly(T list, long value);
    abstract boolean containsOnly(T list, long value, ContainsOnlyPredicate predicate);

    abstract long[] toArray(T list);

    abstract long addHead(T list, long value);
    abstract long addTail(T list, long value);

    abstract long removeHead(T list);
    abstract long removeTailNode(T list, long newTailNode);
    abstract void removeNode(T list, long node, long previousNode);

    long removeTail(T list) {

        throw new UnsupportedOperationException();
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddHead() {

        final T list = createLargeList(10, 100);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);

        addHead(list, 123L);
        checkElements(list, 123L);

        addHead(list, 234L);
        checkElements(list, 234L, 123L);

        addHead(list, 345L);
        checkElements(list, 345L, 234L, 123L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddTail() {

        final T list = createLargeList(10, 100);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);

        addTail(list, 123L);
        checkElements(list, 123L);

        addTail(list, 234L);
        checkElements(list, 123L, 234L);

        addTail(list, 345);
        checkElements(list, 123L, 234L, 345L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveHead() {

        final T list = createLargeList(10, 100);

        addHead(list, 123L);
        addHead(list, 234L);
        addHead(list, 345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(removeHead(list)).isEqualTo(345L);
        checkElements(list, 234L, 123L);

        assertThat(removeHead(list)).isEqualTo(234L);
        checkElements(list, 123L);

        assertThat(removeHead(list)).isEqualTo(123L);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveNode() {

        final T list = createLargeList(10, 100);

        final long node1 = addHead(list, 123L);
        final long node2 = addHead(list, 234L);
        final long node3 = addHead(list, 345L);

        checkElements(list, 345L, 234L, 123L);

        removeNode(list, node2, node3);
        checkElements(list, 345L, 123L);

        removeNode(list, node3, BaseLargeList.NO_NODE);
        checkElements(list, 123L);

        removeNode(list, node1, BaseLargeList.NO_NODE);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final T list = createLargeList(10, 100);

        assertThat(list).isEmpty();

        addHead(list, 123L);
        assertThat(list).isNotEmpty();

        addHead(list, 234L);
        assertThat(list).isNotEmpty();

        addHead(list, 345L);
        assertThat(list).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final T list = createLargeList(10, 100);

        assertThat(list).hasNumElements(0);

        addHead(list, 123L);
        assertThat(list).hasNumElements(1);

        addHead(list, 234L);
        assertThat(list).hasNumElements(2);

        addHead(list, 345L);
        assertThat(list).hasNumElements(3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetValue() {

        final T list = createLargeList(10, 100);

        final long node1 = addHead(list, 123L);
        final long node2 = addHead(list, 234L);
        final long node3 = addHead(list, 345L);

        assertThat(getValue(list, node1)).isEqualTo(123L);
        assertThat(getValue(list, node2)).isEqualTo(234L);
        assertThat(getValue(list, node3)).isEqualTo(345L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testContains() {

        final T list = createLargeList(10, 100);

        addHead(list, 123L);

        assertThat(contains(list, 123L)).isTrue();
        assertThat(contains(list, 234L)).isFalse();

        removeHead(list);

        assertThat(contains(list, 123L)).isFalse();
        assertThat(contains(list, 234L)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsOnlyValue() {

        final T list = createLargeList(10, 100);

        addHead(list, 123L);

        assertThat(containsOnly(list, 123L)).isTrue();
        assertThat(containsOnly(list, 234L)).isFalse();

        addHead(list, 234L);

        assertThat(containsOnly(list, 123L)).isFalse();
        assertThat(containsOnly(list, 234L)).isFalse();

        addHead(list, 123L);

        assertThat(containsOnly(list, 123L)).isFalse();
        assertThat(containsOnly(list, 234L)).isFalse();

        removeHead(list);
        removeHead(list);

        addHead(list, 123L);

        assertThat(containsOnly(list, 123L)).isTrue();
        assertThat(containsOnly(list, 234L)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsOnlyValueWithPredicate() {

        final T list = createLargeList(10, 100);

        final ContainsOnlyPredicate containsOnlyPredicate = (i, l) -> i / 10 == l;

        addHead(list, 123L);

        assertThat(containsOnly(list, 1230L, containsOnlyPredicate)).isTrue();
        assertThat(containsOnly(list, 2340L, containsOnlyPredicate)).isFalse();

        addHead(list, 234L);

        assertThat(containsOnly(list, 1230L, containsOnlyPredicate)).isFalse();
        assertThat(containsOnly(list, 2340L, containsOnlyPredicate)).isFalse();

        addHead(list, 123L);

        assertThat(containsOnly(list, 1230L, containsOnlyPredicate)).isFalse();
        assertThat(containsOnly(list, 2340L, containsOnlyPredicate)).isFalse();

        removeHead(list);
        removeHead(list);

        addHead(list, 123L);

        assertThat(containsOnly(list, 1230L, containsOnlyPredicate)).isTrue();
        assertThat(containsOnly(list, 2340L, containsOnlyPredicate)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetHeadNode() {

        final T list = createLargeList(10, 100);

        final long node1 = addHead(list, 123L);
        assertThat(list.getHeadNode()).isEqualTo(node1);

        final long node2 = addHead(list, 234L);
        assertThat(list.getHeadNode()).isEqualTo(node2);

        final long node3 = addHead(list, 345L);
        assertThat(list.getHeadNode()).isEqualTo(node3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetTailNode() {

        final T list = createLargeList(10, 100);

        final long node1 = addHead(list, 123L);
        assertThat(list.getTailNode()).isEqualTo(node1);

        addHead(list, 234L);
        assertThat(list.getTailNode()).isEqualTo(node1);

        addHead(list, 345L);
        assertThat(list.getTailNode()).isEqualTo(node1);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemoveHead() {

        final T list = createLargeList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = addHead(list, 123L);
        checkFreeListIsEmpty(list);

        removeHead(list);
        checkFreeListElements(list, node1);

        final long node2 = addHead(list, 234L);
        checkFreeListIsEmpty(list);

        removeHead(list);
        checkFreeListElements(list, node2);

        final long node3 = addHead(list, 345L);
        final long node4 = addHead(list, 456L);

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

        final long node1 = addHead(list, 123L);
        checkFreeListIsEmpty(list);

        removeNode(list, node1, BaseLargeList.NO_NODE);
        checkFreeListElements(list, node1);

        final long node2 = addHead(list, 234L);
        checkFreeListIsEmpty(list);

        removeNode(list, node2, BaseLargeList.NO_NODE);
        checkFreeListElements(list, node2);

        final long node3 = addHead(list, 345L);
        final long node4 = addHead(list, 456L);
        final long node5 = addHead(list, 567L);

        checkFreeListIsEmpty(list);

        removeNode(list, node4, node5);
        removeNode(list, node3, node5);
        removeNode(list, node5, BaseLargeList.NO_NODE);

        checkFreeListElements(list, node5, node3, node4);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemoveTail() {

        final T list = createLargeList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = addHead(list, 123L);
        checkFreeListIsEmpty(list);

        removeTailNode(list, BaseLargeList.NO_NODE);
        checkFreeListElements(list, node1);

        final long node2 = addHead(list, 234L);
        checkFreeListIsEmpty(list);

        removeTailNode(list, BaseLargeList.NO_NODE);
        checkFreeListElements(list, node2);

        final long node3 = addHead(list, 345L);
        final long node4 = addHead(list, 456L);

        checkFreeListIsEmpty(list);

        removeTailNode(list, node4);
        removeTailNode(list, BaseLargeList.NO_NODE);

        checkFreeListElements(list, node4, node3);
    }

    final void checkAddTailLongDoublyLinkedList() {

        final T list = createLargeList(10, 100);

        addTail(list, 123L);
        addTail(list, 234L);
        addTail(list, 345L);

        checkElements(list, 123L, 234L, 345L);

        assertThat(removeTail(list)).isEqualTo(345L);
        checkElements(list, 123L, 234L);

        assertThat(removeTail(list)).isEqualTo(234L);
        checkElements(list, 123L);

        assertThat(removeTail(list)).isEqualTo(123L);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    final void checkRemoveTail() {

        final T list = createLargeList(10, 100);

        addHead(list, 123L);
        addHead(list, 234L);
        addHead(list, 345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(removeTail(list)).isEqualTo(123L);
        checkElements(list, 345L, 234L);

        assertThat(removeTail(list)).isEqualTo(234L);
        checkElements(list, 345L);

        assertThat(removeTail(list)).isEqualTo(345L);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    final void checkElements(T list, long ... elements) {

        assertThat(list).isNotEmpty();
        assertThat(list).hasNumElements(elements.length);

        assertThat(toArray(list)).isEqualTo(elements);
    }

    private static <T extends BaseLargeList<?, ?> & Elements> void checkElements(long headNode, T list, long ... elements) {

        assertThat(list).isNotEmpty();
        assertThat(list).hasNumElements(elements.length);

        assertThat(list.toListArrayValues(headNode)).isEqualTo(elements);
    }

    static <T extends BaseLargeList<?, ?>> void checkFreeListIsEmpty(T list) {

        assertThat(list.freeListToArray()).isEmpty();
    }

    static <T extends BaseLargeList<?, ?>> void checkFreeListElements(T list, long ... elements) {

        assertThat(list.freeListToArray()).isEqualTo(elements);
    }
}
