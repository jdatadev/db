package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

abstract class BaseLongListTest<T extends LongList> extends BaseTest {

    abstract T createLongList(int initialOuterCapacity, int innerCapacity);

    abstract long removeTailNode(T list, long newTailNode);
    abstract void removeNode(T list, long node, long previousNode);

    @Test
    @Category(UnitTest.class)
    public final void testAddHead() {

        final LongList list = createLongList(10, 100);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);

        list.addHead(123L);
        checkElements(list, 123L);

        list.addHead(234L);
        checkElements(list, 234L, 123L);

        list.addHead(345L);
        checkElements(list, 345L, 234L, 123L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddTail() {

        final LongList list = createLongList(10, 100);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);

        list.addTail(123L);
        checkElements(list, 123L);

        list.addTail(234L);
        checkElements(list, 123L, 234L);

        list.addTail(345);
        checkElements(list, 123L, 234L, 345L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveHead() {

        final LongList list = createLongList(10, 100);

        list.addHead(123L);
        list.addHead(234L);
        list.addHead(345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(list.removeHead()).isEqualTo(345L);
        checkElements(list, 234L, 123L);

        assertThat(list.removeHead()).isEqualTo(234L);
        checkElements(list, 123L);

        assertThat(list.removeHead()).isEqualTo(123L);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveNode() {

        final T list = createLongList(10, 100);

        final long node1 = list.addHead(123L);
        final long node2 = list.addHead(234L);
        final long node3 = list.addHead(345L);

        checkElements(list, 345L, 234L, 123L);

        removeNode(list, node2, node3);
        checkElements(list, 345L, 123L);

        removeNode(list, node3, LongList.NO_NODE);
        checkElements(list, 123L);

        removeNode(list, node1, LongList.NO_NODE);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final LongList list = createLongList(10, 100);

        assertThat(list).isEmpty();

        list.addHead(123L);
        assertThat(list).isNotEmpty();

        list.addHead(234L);
        assertThat(list).isNotEmpty();

        list.addHead(345L);
        assertThat(list).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final LongList list = createLongList(10, 100);

        assertThat(list).hasNumElements(0);

        list.addHead(123L);
        assertThat(list).hasNumElements(1);

        list.addHead(234L);
        assertThat(list).hasNumElements(2);

        list.addHead(345L);
        assertThat(list).hasNumElements(3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetValue() {

        final LongList list = createLongList(10, 100);

        final long node1 = list.addHead(123L);
        final long node2 = list.addHead(234L);
        final long node3 = list.addHead(345L);

        assertThat(list.getValue(node1)).isEqualTo(123L);
        assertThat(list.getValue(node2)).isEqualTo(234L);
        assertThat(list.getValue(node3)).isEqualTo(345L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetHeadNode() {

        final LongList list = createLongList(10, 100);

        final long node1 = list.addHead(123L);
        assertThat(list.getHeadNode()).isEqualTo(node1);

        final long node2 = list.addHead(234L);
        assertThat(list.getHeadNode()).isEqualTo(node2);

        final long node3 = list.addHead(345L);
        assertThat(list.getHeadNode()).isEqualTo(node3);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetTailNode() {

        final LongList list = createLongList(10, 100);

        final long node1 = list.addHead(123L);
        assertThat(list.getTailNode()).isEqualTo(node1);

        list.addHead(234L);
        assertThat(list.getTailNode()).isEqualTo(node1);

        list.addHead(345L);
        assertThat(list.getTailNode()).isEqualTo(node1);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemoveHead() {

        final LongList list = createLongList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = list.addHead(123L);
        checkFreeListIsEmpty(list);

        list.removeHead();
        checkFreeListElements(list, node1);

        final long node2 = list.addHead(234L);
        checkFreeListIsEmpty(list);

        list.removeHead();
        checkFreeListElements(list, node2);

        final long node3 = list.addHead(345L);
        final long node4 = list.addHead(456L);

        checkFreeListIsEmpty(list);

        list.removeHead();
        list.removeHead();

        checkFreeListElements(list, node3, node4);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemove() {

        final T list = createLongList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = list.addHead(123L);
        checkFreeListIsEmpty(list);

        removeNode(list, node1, LongList.NO_NODE);
        checkFreeListElements(list, node1);

        final long node2 = list.addHead(234L);
        checkFreeListIsEmpty(list);

        removeNode(list, node2, LongList.NO_NODE);
        checkFreeListElements(list, node2);

        final long node3 = list.addHead(345L);
        final long node4 = list.addHead(456L);
        final long node5 = list.addHead(567L);

        checkFreeListIsEmpty(list);

        removeNode(list, node4, node5);
        removeNode(list, node3, node5);
        removeNode(list, node5, LongList.NO_NODE);

        checkFreeListElements(list, node5, node3, node4);
    }

    @Test
    @Category(UnitTest.class)
    public final void testFreeListRemoveTail() {

        final T list = createLongList(10, 100);

        checkFreeListIsEmpty(list);

        final long node1 = list.addHead(123L);
        checkFreeListIsEmpty(list);

        removeTailNode(list, LongList.NO_NODE);
        checkFreeListElements(list, node1);

        final long node2 = list.addHead(234L);
        checkFreeListIsEmpty(list);

        removeTailNode(list, LongList.NO_NODE);
        checkFreeListElements(list, node2);

        final long node3 = list.addHead(345L);
        final long node4 = list.addHead(456L);

        checkFreeListIsEmpty(list);

        removeTailNode(list, node4);
        removeTailNode(list, LongList.NO_NODE);

        checkFreeListElements(list, node4, node3);
    }

    static void checkElements(LongList list, long ... elements) {

        assertThat(list).isNotEmpty();
        assertThat(list).hasNumElements(elements.length);

        assertThat(list.toArray()).isEqualTo(elements);
    }

    static void checkFreeListIsEmpty(LongList list) {

        assertThat(list.freeListToArray()).isEmpty();
    }

    static void checkFreeListElements(LongList list, long ... elements) {

        assertThat(list.freeListToArray()).isEqualTo(elements);
    }
}
