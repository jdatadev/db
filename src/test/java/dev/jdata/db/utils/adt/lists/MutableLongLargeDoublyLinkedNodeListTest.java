package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class MutableLongLargeDoublyLinkedNodeListTest extends BaseLongLargeNodeListTest<MutableLongLargeDoublyLinkedNodeList> {

    @Override
    MutableLongLargeDoublyLinkedNodeList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new MutableLongLargeDoublyLinkedNodeList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(MutableLongLargeDoublyLinkedNodeList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode();
    }

    @Override
    void removeNode(MutableLongLargeDoublyLinkedNodeList list, long node, long previousNode) {

        list.removeNode(node);
    }

    @Override
    long removeTail(MutableLongLargeDoublyLinkedNodeList list) {

        return list.removeTail();
    }

    @Override
    void clear(MutableLongLargeDoublyLinkedNodeList list) {

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
