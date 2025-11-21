package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongLargeDoublyLinkedNodeListTest extends BaseLongLargeNodeListTest<MutableLongLargeDoublyLinkedSingleHeadNodeList> {

    @Override
    MutableLongLargeDoublyLinkedSingleHeadNodeList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableLongLargeDoublyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }

    @Override
    int removeTailAndReturnValue(MutableLongLargeDoublyLinkedSingleHeadNodeList list) {

        return Integers.checkLongToInt(list.removeTailAndReturnValue());
    }

    @Override
    int removeNodeAndReturnValue(MutableLongLargeDoublyLinkedSingleHeadNodeList list, long node) {

        return Integers.checkLongToInt(list.removeNodeAndReturnValue(node));
    }

    @Override
    long removeNodeAndReturnPreviousNode(MutableLongLargeDoublyLinkedSingleHeadNodeList list, long node) {

        list.removeNode(node);

        return list.getPreviousNode(node);
    }

    @Override
    void removeTail(MutableLongLargeDoublyLinkedSingleHeadNodeList list) {

        list.removeTail();
    }

    @Override
    void clear(MutableLongLargeDoublyLinkedSingleHeadNodeList list) {

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
