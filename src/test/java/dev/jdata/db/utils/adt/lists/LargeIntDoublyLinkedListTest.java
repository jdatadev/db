package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeIntDoublyLinkedListTest extends BaseLargeIntListTest<MutableLargeIntDoublyLinkedList> {

    @Override
    MutableLargeIntDoublyLinkedList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new MutableLargeIntDoublyLinkedList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(MutableLargeIntDoublyLinkedList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode();
    }

    @Override
    void removeNode(MutableLargeIntDoublyLinkedList list, long node, long previousNode) {

        list.removeNode(node);
    }

    @Override
    long removeTail(MutableLargeIntDoublyLinkedList list) {

        return list.removeTail();
    }

    @Override
    void clear(MutableLargeIntDoublyLinkedList list) {

        list.clear();
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
