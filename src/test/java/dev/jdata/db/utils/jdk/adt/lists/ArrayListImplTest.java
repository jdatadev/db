package dev.jdata.db.utils.jdk.adt.lists;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.lists.BaseObjectArrayList;
import dev.jdata.db.utils.adt.lists.BaseRandomAccessMutableObjectArrayListTest;

public final class ArrayListImplTest extends BaseRandomAccessMutableObjectArrayListTest<ArrayListImpl<Integer>, ArrayListImpl<String>> {

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        assertThatThrownBy(() -> new ArrayListImpl<>(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ArrayListImpl<>(1, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ArrayListImpl<>(-1, String[]::new)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ArrayListImpl<>(0, String[]::new)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testAdd() {

        checkAddTail(List::add);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddMany() {

        checkArrayListAddTailMany(List::add);
    }

    @Test
    @Category(UnitTest.class)
    public void testSize() {

        final List<String> list = new ArrayListImpl<>(String[]::new);

        final String abc = "abc";

        assertThat(list.size()).isEqualTo(0);

        list.add(abc);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @Category(UnitTest.class)
    public void testIterator() {

        final List<String> list = new ArrayListImpl<>(String[]::new);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.add(abc);
        checkElementsSameAs(list.iterator(), abc);

        list.add(bcd);
        checkElementsSameAs(list.iterator(), abc, bcd);

        list.add(cde);
        checkElementsSameAs(list.iterator(), abc, bcd, cde);
    }

    @SafeVarargs
    private static <E, L extends BaseObjectArrayList<E>> void checkElementsSameAs(Iterator<E> iterator, E ... expectedElements) {

        int count = 0;

        while (iterator.hasNext()) {

            assertThat(iterator.next()).isSameAs(expectedElements[count ++]);
        }

        assertThat(count).isEqualTo(expectedElements.length);

        assertThatThrownBy(() -> iterator.next()).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Override
    protected void clear(ArrayListImpl<String> list) {

        list.clear();
    }

    @Override
    protected ArrayListImpl<Integer> createTestElements(Integer[] elementsToAdd) {

        final ArrayListImpl<Integer> list = new ArrayListImpl<>(Integer[]::new);

        switch (elementsToAdd.length) {

        case 0:
            break;

        case 1:

            list.add(elementsToAdd[0]);
            break;

        default:

            list.addAll(Arrays.asList(elementsToAdd));
            break;
        }

        return list;
    }

    @Override
    protected long getCapacity(ArrayListImpl<String> list) {

        return list.getCapacity();
    }

    @Override
    protected void addHead(ArrayListImpl<String> list, String string) {

        list.add(0, string);
    }

    @Override
    protected void addTail(ArrayListImpl<String> list, String string) {

        list.add(string);
    }

    @Override
    protected void addTail(ArrayListImpl<String> list, String... strings) {

        list.addAll(Arrays.asList(strings));
    }

    @Override
    protected void checkNumElements(ArrayListImpl<String> arrayListImpl, int expectedNumElements) {

        super.checkNumElements(arrayListImpl, expectedNumElements);

        assertThat(arrayListImpl.size()).isEqualTo(expectedNumElements);
    }

    @Override
    protected ArrayListImpl<String> createStringList() {

        return new ArrayListImpl<>(String[]::new);
    }

    @Override
    protected ArrayListImpl<String> createStringList(int initialCapacity) {

        return new ArrayListImpl<>(initialCapacity, String[]::new);
    }

    @Override
    protected void add(ArrayListImpl<String> list, String string) {

        list.add(string);
    }
}
