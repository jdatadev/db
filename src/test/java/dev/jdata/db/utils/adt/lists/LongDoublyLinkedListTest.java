package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LongDoublyLinkedListTest extends BaseLongListTest<LongDoublyLinkedList> {

    @Override
    LongDoublyLinkedList createLongList(int initialOuterCapacity, int innerCapacity) {

        return new LongDoublyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(LongDoublyLinkedList list, long newTailNode) {

        return list.removeTailNode();
    }

    @Override
    void removeNode(LongDoublyLinkedList list, long node, long previousNode) {

        list.removeNode(node);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddTailLongDoublyLinkedList() {

        final LongDoublyLinkedList list = new LongDoublyLinkedList(10, 100);

        list.addTail(123L);
        list.addTail(234L);
        list.addTail(345L);

        checkElements(list, 123L, 234L, 345L);

        assertThat(list.removeTail()).isEqualTo(345L);
        checkElements(list, 123L, 234L);

        assertThat(list.removeTail()).isEqualTo(234L);
        checkElements(list, 123L);

        assertThat(list.removeTail()).isEqualTo(123L);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveTail() {

        final LongDoublyLinkedList list = new LongDoublyLinkedList(10, 100);

        list.addHead(123L);
        list.addHead(234L);
        list.addHead(345L);

        checkElements(list, 345L, 234L, 123L);

        assertThat(list.removeTail()).isEqualTo(123L);
        checkElements(list, 345L, 234L);

        assertThat(list.removeTail()).isEqualTo(234L);
        checkElements(list, 345L);

        assertThat(list.removeTail()).isEqualTo(345L);

        assertThat(list).isEmpty();
        assertThat(list).hasNumElements(0);
    }
}
