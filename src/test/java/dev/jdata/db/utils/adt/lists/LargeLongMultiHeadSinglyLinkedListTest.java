package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class LargeLongMultiHeadSinglyLinkedListTest extends BaseLargeMultiHeadListTest {

    @Test
    @Category(UnitTest.class)
    public void testRemoveNodeByValue() {

        final LargeLongMultiHeadSinglyLinkedList<TestList> list = new LargeLongMultiHeadSinglyLinkedList<>(1, 1);

        final long value = 123L;
        final long otherValue = 234L;

        final TestList testList = new TestList();

        final long addedNode = list.addHead(testList, value, NO_NODE, NO_NODE, TestList::setHeadNode, TestList::setTailNode);
        assertThat(addedNode).isNotEqualTo(NO_NODE);

        final long otherValueRemovedNode = list.removeAtMostOneNodeByValue(testList, otherValue, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode,
                TestList::setTailNode);
        assertThat(otherValueRemovedNode).isEqualTo(NO_NODE);

        final long valueRemovedNode = list.removeAtMostOneNodeByValue(testList, value, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode, TestList::setTailNode);
        assertThat(valueRemovedNode).isEqualTo(addedNode);
    }
}
