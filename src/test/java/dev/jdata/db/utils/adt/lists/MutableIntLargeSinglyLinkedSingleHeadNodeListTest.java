package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntLargeSinglyLinkedSingleHeadNodeListTest extends BaseIntLargeNodeListTest<MutableIntLargeSinglyLinkedSingleHeadNodeList> {

    @Override
    MutableIntLargeSinglyLinkedSingleHeadNodeList createLargeList(int initialOuterCapacity, int innerCapacity) {

        return HeapMutableIntLargeSinglyLinkedSingleHeadNodeList.create(AllocationType.HEAP, initialOuterCapacity, innerCapacity);
    }

    @Override
    long removeNodeAndReturnPreviousNode(MutableIntLargeSinglyLinkedSingleHeadNodeList list, long node) {

        return list.removeNodeByFindingPrevious(node);
    }

    @Override
    void clear(MutableIntLargeSinglyLinkedSingleHeadNodeList list) {

        list.clear();
    }

    @Test
    @Category(UnitTest.class)
    public void testRemoveTail() {

        checkRemoveTailDoublyLinkedList();
    }
}
