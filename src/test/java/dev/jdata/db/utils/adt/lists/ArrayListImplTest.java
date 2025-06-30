package dev.jdata.db.utils.adt.lists;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.collections.Coll;

public final class ArrayListImplTest extends BaseMutableObjectArrayListTest<ArrayListImpl<Integer>, ArrayListImpl<String>> {

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        assertThatThrownBy(() -> new ArrayListImpl<>(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ArrayListImpl<>(null, 1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ArrayListImpl<>(String[]::new, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new ArrayListImpl<>(String[]::new, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testAdd() {

        checkAddTail(List::add);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddMany() {

        checkAddTailMany(List::add);
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
    protected ArrayListImpl<Integer> createTestElements(Integer[] elementsToAdd) {

        final ArrayListImpl<Integer> list = new ArrayListImpl<>(Integer[]::new);

        switch (elementsToAdd.length) {

        case 0:
            break;

        case 1:

            list.addTailElement(elementsToAdd[0]);
            break;

        default:

            list.addTailElements(elementsToAdd);
            break;
        }

        return list;
    }

    @Override
    protected <P> long count(ArrayListImpl<Integer> elements, P parameter, BiPredicate<Integer, P> predicate) {

        return Coll.count(elements, parameter, predicate);
    }

    @Override
    protected long countWithClosure(ArrayListImpl<Integer> elements, Predicate<Integer> predicate) {

        return Coll.closureOrConstantCount(elements, predicate);
    }

    @Override
    protected int maxInt(ArrayListImpl<Integer> elements, int defaultValue, ToIntFunction<Integer> mapper) {

        return Coll.maxInt(elements, defaultValue, mapper);
    }

    @Override
    protected long maxLong(ArrayListImpl<Integer> elements, long defaultValue, ToLongFunction<Integer> mapper) {

        return Coll.maxLong(elements, defaultValue, mapper);
    }

    @Override
    int getCapacity(ArrayListImpl<String> list) {

        return list.getCapacity();
    }

    @Override
    void addHead(ArrayListImpl<String> list, String string) {

        list.addHeadElement(string);
    }

    @Override
    void addTail(ArrayListImpl<String> list, String string) {

        list.add(string);
    }

    @Override
    void addTail(ArrayListImpl<String> list, String... strings) {

        list.addAll(Arrays.asList(strings));
    }

    @Override
    void checkNumElements(BaseObjectArrayList<?> list, int expectedNumElements) {

        super.checkNumElements(list, expectedNumElements);

        final ArrayListImpl<?> arrayListImpl = (ArrayListImpl<?>)list;

        assertThat(arrayListImpl.size()).isEqualTo(expectedNumElements);
    }

    @Override
    ArrayListImpl<String> createStringList() {

        return new ArrayListImpl<>(String[]::new);
    }

    @Override
    ArrayListImpl<String> createStringList(int initialCapacity) {

        return new ArrayListImpl<>(String[]::new, initialCapacity);
    }

    @Override
    void add(ArrayListImpl<String> list, String string) {

        list.add(string);
    }
}
