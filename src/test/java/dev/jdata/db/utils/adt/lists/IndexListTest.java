package dev.jdata.db.utils.adt.lists;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.scalars.Integers;

public final class IndexListTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        assertThatThrownBy(() -> new IndexList<>(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new IndexList<>(null, 1)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new IndexList<>(String[]::new, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new IndexList<>(String[]::new, 0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testOfSingleton() {

        final IndexList<String> list = IndexList.of("0");

        checkAddTailMany(list, List::add);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddHead() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.addHead(abc);
        checkElementsSameAs(list, abc);

        list.addHead(bcd);
        checkElementsSameAs(list, bcd, abc);

        list.addHead(cde);
        checkElementsSameAs(list, cde, bcd, abc);
    }

    @Test
    @Category(UnitTest.class)
    public void testAdd() {

        checkAddTail(List::add);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddTail() {

        checkAddTail((l, s) -> {

            l.addTail(s);

            return true;
        });
    }

    private static void checkAddTail(BiPredicate<IndexList<String>, String> listTailAdder) {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        assertThat(listTailAdder.test(list, abc)).isTrue();
        checkElementsSameAs(list, abc);

        assertThat(listTailAdder.test(list, bcd)).isTrue();
        checkElementsSameAs(list, abc, bcd);

        assertThat(listTailAdder.test(list, cde)).isTrue();
        checkElementsSameAs(list, abc, bcd, cde);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddHeadMany() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final int numToAdd = 10 * 1000;

        final String[] array = new String[numToAdd];

        for (int i = 0; i < numToAdd; ++ i) {

            final String instance = String.valueOf(i);

            list.addHead(instance);

            array[i] = instance;

            checkNumElements(list, i + 1);

            assertThat(list.get(0)).isSameAs(instance);

            for (int j = 0; j <= i; ++ j) {

                assertThat(list.get(j)).isSameAs(array[i - j]);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testAddMany() {

        checkAddTailMany(List::add);
    }

    @Test
    @Category(UnitTest.class)
    public void testAddTailMany() {

        checkAddTailMany((l, s) -> {

            l.addTail(s);

            return true;
        });
    }

    private static void checkAddTailMany(BiPredicate<IndexList<String>, String> listTailAdder) {

        final IndexList<String> list = new IndexList<>(String[]::new);

        checkAddTailMany(list, listTailAdder);
    }

    private static void checkAddTailMany(IndexList<String> list, BiPredicate<IndexList<String>, String> listTailAdder) {

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(list.getNumElements());

        final int numToAdd = 10 * 1000;

        final String[] array = new String[numToAdd];

        for (int i = numElements; i < numToAdd; ++ i) {

            final String instance = String.valueOf(i);

            assertThat(listTailAdder.test(list, instance)).isTrue();

            array[i] = instance;

            checkNumElements(list, i + 1);

            assertThat(list.get(i)).isSameAs(instance);

            for (int j = numElements; j <= i; ++ j) {

                assertThat(list.get(j)).isSameAs(array[j]);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testSet() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String outOfBounds = "outOfBounds";

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        final String def = "def";
        final String efg = "efg";
        final String fgi = "fgi";

        assertThatThrownBy(() -> list.set(-1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(0, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);

        list.add(abc);

        assertThat(list.get(0)).isSameAs(abc);
        assertThatThrownBy(() -> list.set(-1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);

        list.add(bcd);
        assertThatThrownBy(() -> list.set(-1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(2, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);

        list.add(cde);
        assertThatThrownBy(() -> list.set(-1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(3, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);

        list.set(1, efg);
        checkElementsSameAs(list, abc, efg, cde);

        list.set(0, def);
        checkElementsSameAs(list, def, efg, cde);

        list.set(2, fgi);
        checkElementsSameAs(list, def, efg, fgi);
    }

    @Test
    @Category(UnitTest.class)
    public void testGet() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(1)).isInstanceOf(IndexOutOfBoundsException.class);

        list.add(abc);
        checkElementsSameAs(list, abc);
        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(1)).isInstanceOf(IndexOutOfBoundsException.class);

        list.add(bcd);
        checkElementsSameAs(list, abc, bcd);
        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(2)).isInstanceOf(IndexOutOfBoundsException.class);

        list.add(cde);
        checkElementsSameAs(list, abc, bcd, cde);
        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(3)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetHead() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        assertThatThrownBy(() -> list.getHead()).isInstanceOf(IllegalStateException.class);

        final String abc = "abc";
        final String bcd = "bcd";

        list.add(abc);
        assertThat(list.getHead()).isSameAs(abc);

        list.add(bcd);
        assertThat(list.getHead()).isSameAs(abc);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetTail() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        assertThatThrownBy(() -> list.getTail()).isInstanceOf(IllegalStateException.class);

        final String abc = "abc";
        final String bcd = "bcd";

        list.add(abc);
        assertThat(list.getTail()).isSameAs(abc);

        list.add(bcd);
        assertThat(list.getTail()).isSameAs(bcd);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetNumElements() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";

        assertThat(list.getNumElements()).isEqualTo(0L);

        list.add(abc);
        assertThat(list.getNumElements()).isEqualTo(1L);
    }

    @Test
    @Category(UnitTest.class)
    public void testIsEmpty() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";

        assertThat(list.isEmpty()).isTrue();

        list.add(abc);
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public void testSize() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";

        assertThat(list.size()).isEqualTo(0);

        list.add(abc);
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    @Category(UnitTest.class)
    public void testInitialCapacity() {

        final int initialCapacity = 123;

        final IndexList<String> list = new IndexList<>(String[]::new, initialCapacity);

        assertThat(list.getCapacity()).isEqualTo(initialCapacity);
    }

    @Test
    @Category(UnitTest.class)
    public void testGetCapacity() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final long initialCapacity = list.getCapacity();

        int i;

        for (i = 0; i < initialCapacity; ++ i) {

            final String instance = String.valueOf(i);

            list.add(instance);
        }

        final String instance = String.valueOf(i);

        list.add(instance);

        assertThat(list.getCapacity()).isEqualTo(initialCapacity * 2);
    }

    @Test
    @Category(UnitTest.class)
    public void testIterator() {

        final IndexList<String> list = new IndexList<>(String[]::new);

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

    @Test
    @Category(UnitTest.class)
    public void testClear() {

        final IndexList<String> list = new IndexList<>(String[]::new);

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        list.add(abc);
        checkElementsSameAs(list, abc);

        list.clear();
        checkNumElements(list, 0);

        list.add(abc);
        list.add(bcd);
        checkElementsSameAs(list, abc, bcd);

        list.clear();
        checkNumElements(list, 0);

        list.add(abc);
        list.add(bcd);
        list.add(cde);
        checkElementsSameAs(list, abc, bcd, cde);

        list.clear();
        checkNumElements(list, 0);
    }

    @SafeVarargs
    private static <T> void checkElementsSameAs(IndexList<T> list, T ... expectedElements) {

        final int numElements = expectedElements.length;

        checkNumElements(list, numElements);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(list.get(i)).isSameAs(expectedElements[i]);
        }
    }

    @SafeVarargs
    private static <T> void checkElementsSameAs(Iterator<T> iterator, T ... expectedElements) {

        int count = 0;

        while (iterator.hasNext()) {

            assertThat(iterator.next()).isSameAs(expectedElements[count ++]);
        }

        assertThat(count).isEqualTo(expectedElements.length);

        assertThatThrownBy(() -> iterator.next()).isInstanceOf(IndexOutOfBoundsException.class);
    }

    private static void checkNumElements(IndexList<?> list, int expectedNumElements) {

        assertThat(list.isEmpty()).isEqualTo(expectedNumElements == 0);
        assertThat(list.getNumElements()).isEqualTo(expectedNumElements);
        assertThat(list.size()).isEqualTo(expectedNumElements);
    }
}
