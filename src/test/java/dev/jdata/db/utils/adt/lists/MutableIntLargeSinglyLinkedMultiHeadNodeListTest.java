package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class MutableIntLargeSinglyLinkedMultiHeadNodeListTest extends BaseLargeMultiHeadNodeListTest {

    @Test
    @Category(UnitTest.class)
    public void testRemoveNodeByValue() {

        final MutableIntLargeSinglyLinkedMultiHeadNodeList<TestList> list = new MutableIntLargeSinglyLinkedMultiHeadNodeList<>(1, 1);

        final int value = 123;
        final int otherValue = 234;

        final TestList testList = new TestList();

        final long addedNode = list.addHead(testList, value, NO_NODE, NO_NODE, TestList::setHeadNode, TestList::setTailNode);
        assertThat(addedNode).isNotEqualTo(NO_NODE);

        final long otherValueRemovedNode = list.removeNodeByValue(testList, otherValue, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode,
                TestList::setTailNode);
        assertThat(otherValueRemovedNode).isEqualTo(NO_NODE);

        final long valueRemovedNode = list.removeNodeByValue(testList, value, testList.getHeadNode(), testList.getTailNode(), TestList::setHeadNode, TestList::setTailNode);
        assertThat(valueRemovedNode).isEqualTo(addedNode);
    }
}
