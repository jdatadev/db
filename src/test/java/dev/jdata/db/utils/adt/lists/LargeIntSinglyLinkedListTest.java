package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeIntSinglyLinkedListTest extends BaseLargeIntListTest<LargeIntSinglyLinkedList> {

    @Override
    LargeIntSinglyLinkedList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new LargeIntSinglyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(LargeIntSinglyLinkedList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode(newTailNode);
    }

    @Override
    void removeNode(LargeIntSinglyLinkedList list, long node, long previousNode) {

        list.removeNode(node, previousNode);
    }

    @Override
    void clear(LargeIntSinglyLinkedList list) {

        list.clear();
    }

    @Category(UnitTest.class)
    public void testRemoveHeadNode() {

        final LargeIntSinglyLinkedList list = new LargeIntSinglyLinkedList(10, 100);

        final long node1 = list.addHead(123);
        final long node2 = list.addHead(234);
        final long node3 = list.addHead(345);

        checkElements(list, 345L, 234, 123);

        assertThat(list.removeHeadNode()).isEqualTo(node3);
        checkElements(list, 234, 123);

        assertThat(list.removeHeadNode()).isEqualTo(node2);
        checkElements(list, 123);

        assertThat(list.removeHeadNode()).isEqualTo(node1);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveTailNode() {

        final LargeIntSinglyLinkedList list = new LargeIntSinglyLinkedList(10, 100);

        final long node1 = list.addHead(123);
        final long node2 = list.addHead(234);
        final long node3 = list.addHead(345);

        checkElements(list, 345, 234, 123);

        assertThat(list.removeTailNodeAndReturnNode(node2)).isEqualTo(node1);
        checkElements(list, 345, 234);

        assertThat(list.removeTailNodeAndReturnNode(node3)).isEqualTo(node2);
        checkElements(list, 345);

        assertThat(list.removeTailNodeAndReturnNode(BaseLargeList.NO_NODE)).isEqualTo(node3);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }
}
