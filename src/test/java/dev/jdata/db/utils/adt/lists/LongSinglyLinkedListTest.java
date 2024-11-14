package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LongSinglyLinkedListTest extends BaseLongListTest<LongSinglyLinkedList> {

    @Override
    LongSinglyLinkedList createLongList(int initialOuterCapacity, int innerCapacity) {

        return new LongSinglyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(LongSinglyLinkedList list, long newTailNode) {

        return list.removeTailNode(newTailNode);
    }

    @Override
    void removeNode(LongSinglyLinkedList list, long node, long previousNode) {

        list.removeNode(node, previousNode);
    }

    @Category(UnitTest.class)
    public void testRemoveHeadNode() {

        final LongSinglyLinkedList list = new LongSinglyLinkedList(10, 100);

        final long node1 = list.addHead(123L);
        final long node2 = list.addHead(234L);
        final long node3 = list.addHead(345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(list.removeHeadNode()).isEqualTo(node3);
        checkElements(list, 234L, 123L);

        assertThat(list.removeHeadNode()).isEqualTo(node2);
        checkElements(list, 123L);

        assertThat(list.removeHeadNode()).isEqualTo(node1);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveTailNode() {

        final LongSinglyLinkedList list = new LongSinglyLinkedList(10, 100);

        final long node1 = list.addHead(123L);
        final long node2 = list.addHead(234L);
        final long node3 = list.addHead(345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(list.removeTailNode(node2)).isEqualTo(node1);
        checkElements(list, 345L, 234L);

        assertThat(list.removeTailNode(node3)).isEqualTo(node2);
        checkElements(list, 345L);

        assertThat(list.removeTailNode(LongList.NO_NODE)).isEqualTo(node3);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }
}
