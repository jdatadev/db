package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeLongDoublyLinkedListTest extends BaseLargeLongListTest<LargeLongDoublyLinkedList> {

    @Override
    LargeLongDoublyLinkedList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new LargeLongDoublyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(LargeLongDoublyLinkedList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode();
    }

    @Override
    void removeNode(LargeLongDoublyLinkedList list, long node, long previousNode) {

        list.removeNode(node);
    }

    @Override
    long removeTail(LargeLongDoublyLinkedList list) {

        return list.removeTail();
    }

    @Test
    @Category(UnitTest.class)
    public void testAddTailLongDoublyLinkedList() {

        checkAddTailLongDoublyLinkedList();
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveTail() {

        checkRemoveTail();
    }
}
