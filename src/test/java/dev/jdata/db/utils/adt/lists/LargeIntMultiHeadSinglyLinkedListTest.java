package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeIntMultiHeadSinglyLinkedListTest extends BaseLargeMultiHeadListTest {

    @Test
    @Category(UnitTest.class)
    public void testRemoveNodeByValue() {

        final LargeIntMultiHeadSinglyLinkedList<TestList> list = new LargeIntMultiHeadSinglyLinkedList<>(1, 1);

        final int value = 123;
        final int otherValue = 234;

        final TestList testList = new TestList();

        final long addedNode = list.addHead(testList, value, BaseList.NO_NODE, BaseList.NO_NODE, TestList::setHeadNode, TestList::setTailNode);
        assertThat(addedNode).isNotEqualTo(BaseList.NO_NODE);

        final long otherValueRemovedNode = list.removeNodeByValue(testList, otherValue, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode,
                TestList::setTailNode);
        assertThat(otherValueRemovedNode).isEqualTo(BaseList.NO_NODE);

        final long valueRemovedNode = list.removeNodeByValue(testList, value, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode, TestList::setTailNode);
        assertThat(valueRemovedNode).isEqualTo(addedNode);
    }
}
