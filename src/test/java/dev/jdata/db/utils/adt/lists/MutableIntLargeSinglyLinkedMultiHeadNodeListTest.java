package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntLargeSinglyLinkedMultiHeadNodeListTest extends BaseLargeMultiHeadNodeListTest {

    @Test
    @Category(UnitTest.class)
    public void testRemoveNodeByValue() {

        final MutableIntLargeSinglyLinkedMultiHeadNodeList<TestList> list = HeapMutableIntLargeSinglyLinkedMultiHeadNodeList.create(AllocationType.HEAP, 1, 1);

        final int value = 123;
        final int otherValue = 234;

        final TestList testList = new TestList();

        final long addedNode = list.addHead(testList, value, NO_NODE, NO_NODE, TestList::setHeadNode, TestList::setTailNode);
        assertThat(addedNode).isNotEqualTo(NO_NODE);

        final long otherValueRemovedNode = list.removeAtMostOneNodeByValue(testList, otherValue, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode,
                TestList::setTailNode);
        assertThat(otherValueRemovedNode).isEqualTo(NO_NODE);

        final long valueRemovedNode = list.removeAtMostOneNodeByValue(testList, value, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode,
                TestList::setTailNode);
        assertThat(valueRemovedNode).isEqualTo(addedNode);
    }
}
