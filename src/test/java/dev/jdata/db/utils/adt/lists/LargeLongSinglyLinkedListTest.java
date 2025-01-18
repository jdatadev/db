package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeLongSinglyLinkedListTest extends BaseLargeLongListTest<LargeLongSinglyLinkedList> {

    @Override
    LargeLongSinglyLinkedList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new LargeLongSinglyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(LargeLongSinglyLinkedList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode(newTailNode);
    }

    @Override
    void removeNode(LargeLongSinglyLinkedList list, long node, long previousNode) {

        list.removeNode(node, previousNode);
    }

    @Category(UnitTest.class)
    public void testRemoveHeadNode() {

        final LargeLongSinglyLinkedList list = new LargeLongSinglyLinkedList(10, 100);

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

        final LargeLongSinglyLinkedList list = new LargeLongSinglyLinkedList(10, 100);

        final long node1 = list.addHead(123L);
        final long node2 = list.addHead(234L);
        final long node3 = list.addHead(345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(list.removeTailNodeAndReturnNode(node2)).isEqualTo(node1);
        checkElements(list, 345L, 234L);

        assertThat(list.removeTailNodeAndReturnNode(node3)).isEqualTo(node2);
        checkElements(list, 345L);

        assertThat(list.removeTailNodeAndReturnNode(BaseLargeList.NO_NODE)).isEqualTo(node3);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }
}
