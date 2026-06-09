package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableObjectDoublyLinkedListTest extends BaseMutableObjectLinkedListTest<IMutableDoublyLinkedList<Integer>, MutableObjectDoublyLinkedList<String>> {

    @Test
    @Category(UnitTest.class)
    public void testGetHeadNode() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        assertThat(list.getHeadNode()).isNull();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        assertThat(list).isEmpty();

        list.addHead(abc);
        assertThat(list.getHeadNode().getElement()).isSameAs(abc);

        list.clear();

        list.addTail(abc, bcd, cde);
        assertThat(list.getHeadNode().getElement()).isSameAs(abc);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetTailNode() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        assertThat(list.getTailNode()).isNull();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        assertThat(list).isEmpty();

        list.addHead(abc);
        assertThat(list.getTailNode().getElement()).isSameAs(abc);

        list.clear();

        list.addTail(abc, bcd, cde);
        assertThat(list.getTailNode().getElement()).isSameAs(cde);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNext() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.addHead(abc);

        final Node<String> abcNode = list.getHeadNode();

        list.removeNode(abcNode);

        assertThatThrownBy(() -> abcNode.getNext()).isInstanceOf(IllegalStateException.class);

        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        assertThat(list.getHeadNode().getNext().getElement()).isSameAs(bcd);
        assertThat(list.getHeadNode().getNext().getNext().getElement()).isSameAs(cde);
        assertThat(list.getHeadNode().getNext().getNext().getNext()).isNull();
        assertThat(list.getTailNode().getNext()).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testGetPrevious() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.addHead(abc);

        final Node<String> abcNode = list.getHeadNode();

        list.removeNode(abcNode);

        assertThatThrownBy(() -> abcNode.getPrevious()).isInstanceOf(IllegalStateException.class);

        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        assertThat(list.getTailNode().getPrevious().getElement()).isSameAs(bcd);
        assertThat(list.getTailNode().getPrevious().getPrevious().getElement()).isSameAs(abc);
        assertThat(list.getTailNode().getPrevious().getPrevious().getPrevious()).isNull();
        assertThat(list.getHeadNode().getPrevious()).isNull();
    }

    @Test
    @Category(UnitTest.class)
    public void testContains() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        assertThatThrownBy(() -> list.contains(null, null, (e, p) -> true)).isInstanceOf(NullPointerException.class);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.addTail(abc);

        final Node<String> node = list.getTailNode();
        assertThat(node).isNotNull();

        assertThatThrownBy(() -> list.contains(node, null, null)).isInstanceOf(NullPointerException.class);

        final Object parameter = new Object();

        list.contains(list.getHeadNode(), parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return true;
        });

        list.clear();
        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        assertThatThrownBy(() -> list.contains(list.getHeadNode(), null, (e, p) -> true)).isInstanceOf(IllegalStateException.class);

        final Node<String> abcNode = list.getHeadNode();

        assertThat(list.contains(abcNode, null, (e, p) -> e == abc)).isTrue();
        assertThat(list.contains(abcNode, null, (e, p) -> e == bcd)).isTrue();
        assertThat(list.contains(abcNode, null, (e, p) -> e == cde)).isTrue();

        final Node<String> bcdNode = abcNode.getNext();

        assertThat(list.contains(bcdNode, null, (e, p) -> e == abc)).isFalse();
        assertThat(list.contains(bcdNode, null, (e, p) -> e == bcd)).isTrue();
        assertThat(list.contains(bcdNode, null, (e, p) -> e == cde)).isTrue();

        final Node<String> cdeNode = bcdNode.getNext();

        assertThat(list.contains(cdeNode, null, (e, p) -> e == abc)).isFalse();
        assertThat(list.contains(cdeNode, null, (e, p) -> e == bcd)).isFalse();
        assertThat(list.contains(cdeNode, null, (e, p) -> e == cde)).isTrue();
    }

    @Test
    @Category(UnitTest.class)
    public void testFindAtMostOneNode() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        assertThatThrownBy(() -> list.findAtMostOne(null, null)).isInstanceOf(NullPointerException.class);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.addTail(abc);

        final Node<String> node = list.getTailNode();
        assertThat(node).isNotNull();

        assertThatThrownBy(() -> list.contains(node, null, null)).isInstanceOf(NullPointerException.class);

        final Object parameter = new Object();

        list.contains(list.getHeadNode(), parameter, (e, p) -> {

            assertThat(p).isSameAs(parameter);

            return true;
        });

        list.clear();
        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        assertThatThrownBy(() -> list.findAtMostOne(null, (e, p) -> true)).isInstanceOf(IllegalStateException.class);

        assertThat(list.findAtMostOneNode(null, (e, p) -> false)).isNull();

        final Node<String> abcNode = list.getHeadNode();
        final Node<String> bcdNode = abcNode.getNext();
        final Node<String> cdeNode = bcdNode.getNext();

        assertThat(list.findAtMostOneNode(null, (e, p) -> e == abc)).isSameAs(abcNode);
        assertThat(list.findAtMostOneNode(null, (e, p) -> e == bcd)).isSameAs(bcdNode);
        assertThat(list.findAtMostOneNode(null, (e, p) -> e == cde)).isSameAs(cdeNode);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemove() {

        final MutableObjectDoublyLinkedList<String> list = createStringList();

        assertThatThrownBy(() -> list.removeNode(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> list.removeNode(new Node<>())).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {

            final Node<String> node = new Node<>();

            node.initialize(list);

            list.removeNode(node);

        }).isInstanceOf(NoSuchElementException.class);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        assertThat(list).isEmpty();

        list.addHead(abc);

        list.removeNode(list.getListHeadNode());
        assertThat(list).isEmpty();

        list.addTail(abc, bcd);

        list.removeNode(list.getListHeadNode());
        checkElementsSameAs(list, bcd);

        list.removeNode(list.getListHeadNode());
        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        list.removeNode(list.getListHeadNode());
        checkElementsSameAs(list, bcd, cde);

        list.removeNode(list.getListHeadNode());
        checkElementsSameAs(list, cde);

        list.removeNode(list.getListHeadNode());
        assertThat(list).isEmpty();

        list.addTail(abc);

        list.removeNode(list.getListTailNode());
        assertThat(list).isEmpty();

        list.addTail(abc, bcd);

        list.removeNode(list.getListTailNode());
        checkElementsSameAs(list, abc);

        list.removeNode(list.getListTailNode());
        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        list.removeNode(list.getListTailNode());
        checkElementsSameAs(list, abc, bcd);

        list.removeNode(list.getListTailNode());
        checkElementsSameAs(list, abc);

        list.removeNode(list.getListTailNode());
        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        Node<String> bcdNode = list.getHeadNode().getNext();

        assertThat(bcdNode.getElement()).isSameAs(bcd);

        list.removeNode(bcdNode);
        checkElementsSameAs(list, abc, cde);

        list.removeNode(list.getHeadNode());
        checkElementsSameAs(list, cde);

        list.removeNode(list.getHeadNode());
        assertThat(list).isEmpty();

        list.addTail(abc, bcd, cde);

        bcdNode = list.getHeadNode().getNext();

        assertThat(bcdNode.getElement()).isSameAs(bcd);

        list.removeNode(bcdNode);
        checkElementsSameAs(list, abc, cde);

        list.removeNode(list.getTailNode());
        checkElementsSameAs(list, abc);

        list.removeNode(list.getTailNode());
        assertThat(list).isEmpty();
    }

    @Override
    protected void clear(MutableObjectDoublyLinkedList<String> list) {

        list.clear();
    }

    @Override
    protected IMutableDoublyLinkedList<Integer> createTestElements(Integer[] elementsToAdd) {

        final MutableObjectDoublyLinkedList<Integer> result = HeapMutableObjectDoublyLinkedList.create(AllocationType.HEAP);

        if (elementsToAdd.length != 0) {

            result.addTail(elementsToAdd);
        }

        return result;
    }

    @Override
    protected long getCapacity(MutableObjectDoublyLinkedList<String> list) {

        return list.getCapacity();
    }

    @Override
    protected void addTail(MutableObjectDoublyLinkedList<String> list, String string) {

        list.addTail(string);
    }

    @Override
    protected void addTail(MutableObjectDoublyLinkedList<String> list, String... strings) {

        list.addTail(strings);
    }

    @Override
    protected MutableObjectDoublyLinkedList<String> createStringList() {

        return HeapMutableObjectDoublyLinkedList.create(AllocationType.HEAP);
    }

    @Override
    protected MutableObjectDoublyLinkedList<String> createStringList(int initialCapacity) {

        return HeapMutableObjectDoublyLinkedList.create(AllocationType.HEAP);
    }

    @Override
    protected void add(MutableObjectDoublyLinkedList<String> list, String string) {

        list.addTail(string);
    }
}
