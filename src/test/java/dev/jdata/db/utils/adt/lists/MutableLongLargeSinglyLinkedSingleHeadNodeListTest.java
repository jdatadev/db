package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongLargeSinglyLinkedSingleHeadNodeListTest extends BaseLongLargeNodeListTest<MutableLongLargeSinglyLinkedSingleHeadNodeList> {

    @Override
    MutableLongLargeSinglyLinkedSingleHeadNodeList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableLongLargeSinglyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeNodeAndReturnPreviousNode(MutableLongLargeSinglyLinkedSingleHeadNodeList list, long node) {

        return list.removeNodeByFindingPrevious(node);
    }

    @Override
    void clear(MutableLongLargeSinglyLinkedSingleHeadNodeList list) {

        list.clear();
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveTail() {

        checkRemoveTailDoublyLinkedList();
    }
}
