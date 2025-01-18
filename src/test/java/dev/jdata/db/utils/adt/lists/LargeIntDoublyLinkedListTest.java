package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeIntDoublyLinkedListTest extends BaseLargeIntListTest<LargeIntDoublyLinkedList> {

    @Override
    LargeIntDoublyLinkedList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new LargeIntDoublyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(LargeIntDoublyLinkedList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode();
    }

    @Override
    void removeNode(LargeIntDoublyLinkedList list, long node, long previousNode) {

        list.removeNode(node);
    }

    @Override
    long removeTail(LargeIntDoublyLinkedList list) {

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
