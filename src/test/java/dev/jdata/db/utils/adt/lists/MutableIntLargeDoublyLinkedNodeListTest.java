package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class MutableIntLargeDoublyLinkedNodeListTest extends BaseIntLargeNodeListTest<MutableIntLargeDoublyLinkedNodeList> {

    @Override
    MutableIntLargeDoublyLinkedNodeList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return new MutableIntLargeDoublyLinkedNodeList(initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeTailNode(MutableIntLargeDoublyLinkedNodeList list, long newTailNode) {

        return list.removeTailNodeAndReturnNode();
    }

    @Override
    void removeNode(MutableIntLargeDoublyLinkedNodeList list, long node, long previousNode) {

        list.removeNodeAndReturnValue(node);
    }

    @Override
    long removeTail(MutableIntLargeDoublyLinkedNodeList list) {

        return list.removeTail();
    }

    @Override
    void clear(MutableIntLargeDoublyLinkedNodeList list) {

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
