package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntLargeDoublyLinkedSingleHeadNodeListTest extends BaseIntLargeNodeListTest<MutableIntLargeDoublyLinkedSingleHeadNodeList> {

    @Override
    MutableIntLargeDoublyLinkedSingleHeadNodeList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableIntLargeDoublyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }

    @Override
    int removeTailAndReturnValue(MutableIntLargeDoublyLinkedSingleHeadNodeList list) {

        return list.removeTailAndReturnValue();
    }

    @Override
    void removeTail(MutableIntLargeDoublyLinkedSingleHeadNodeList list) {

        list.removeTail();
    }

    @Override
    int removeNodeAndReturnValue(MutableIntLargeDoublyLinkedSingleHeadNodeList list, long node) {

        return list.removeNodeAndReturnValue(node);
    }

    @Override
    long removeNodeAndReturnPreviousNode(MutableIntLargeDoublyLinkedSingleHeadNodeList list, long node) {

        list.removeNode(node);

        return list.getTailNode();
    }

    @Override
    void clear(MutableIntLargeDoublyLinkedSingleHeadNodeList list) {

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

        checkRemoveTailDoublyLinkedList();
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveAndReturnValue() {

        checkRemoveAndReturnValueDoublyLinkedList();
    }
}
