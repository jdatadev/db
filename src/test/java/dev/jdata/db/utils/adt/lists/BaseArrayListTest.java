package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.BaseElementsAggregatesTest;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseArrayListTest<T, U extends BaseArrayList<String>> extends BaseElementsAggregatesTest<T> {

    abstract U createStringList();
    abstract U createStringList(int initialCapacity);
    abstract void add(U list, String string);

    @Test
    @Category(UnitTest.class)
    public final void testAddHead() {

        final U list = createStringList();

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
    public final void testAddTail() {

        checkAddTail((l, s) -> {

            l.addTail(s);

            return true;
        });
    }

    final void checkAddTail(BiPredicate<U, String> listTailAdder) {

        final U list = createStringList();

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
    public final void testAddTailVarargs() {

        final U list = createStringList();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";
        final String def = "def";
        final String efg = "efg";

        assertThatThrownBy(() -> list.addTail(new String[0])).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> list.addTail(new String[] { abc })).isInstanceOf(UnsupportedOperationException.class);

        list.addTail(abc, bcd);
        checkElementsSameAs(list, abc, bcd);

        list.addTail(cde, def, efg);
        checkElementsSameAs(list, abc, bcd, cde, def, efg);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddTailVarargsMany() {

        final U list = createStringList();

        final int numToAdd = 10 * 1000;

        final String[] array = new String[numToAdd];

        for (int i = 0; i < numToAdd; i += 2) {

            final String instance1 = String.valueOf(i);
            final String instance2 = String.valueOf(i + 1);

            list.addTail(instance1, instance2);

            array[i] = instance1;
            array[i + 1] = instance2;

            checkNumElements(list, i + 2);

            assertThat(list.get(i)).isSameAs(instance1);
            assertThat(list.get(i + 1)).isSameAs(instance2);

            for (int j = 0; j <= i; ++ j) {

                assertThat(list.get(j)).isSameAs(array[j]);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddHeadMany() {

        final U list = createStringList();

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
    public final void testAddTailMany() {

        checkAddTailMany((l, s) -> {

            l.addTail(s);

            return true;
        });
    }

    final void checkAddTailMany(BiPredicate<U, String> listTailAdder) {

        final U list = createStringList();

        checkAddTailMany(list, listTailAdder);
    }

    final void checkAddTailMany(U list, BiPredicate<U, String> listTailAdder) {

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
    public final void testSet() {

        final U list = createStringList();

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

        add(list, abc);

        assertThat(list.get(0)).isSameAs(abc);
        assertThatThrownBy(() -> list.set(-1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);

        add(list, bcd);
        assertThatThrownBy(() -> list.set(-1, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.set(2, outOfBounds)).isInstanceOf(IndexOutOfBoundsException.class);

        add(list, cde);
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
    public final void testGet() {

        final U list = createStringList();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(1)).isInstanceOf(IndexOutOfBoundsException.class);

        add(list, abc);
        checkElementsSameAs(list, abc);
        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(1)).isInstanceOf(IndexOutOfBoundsException.class);

        add(list, bcd);
        checkElementsSameAs(list, abc, bcd);
        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(2)).isInstanceOf(IndexOutOfBoundsException.class);

        add(list, cde);
        checkElementsSameAs(list, abc, bcd, cde);
        assertThatThrownBy(() -> list.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> list.get(3)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetHead() {

        final U list = createStringList();

        assertThatThrownBy(() -> list.getHead()).isInstanceOf(IllegalStateException.class);

        final String abc = "abc";
        final String bcd = "bcd";

        add(list, abc);
        assertThat(list.getHead()).isSameAs(abc);

        add(list, bcd);
        assertThat(list.getHead()).isSameAs(abc);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetTail() {

        final U list = createStringList();

        assertThatThrownBy(() -> list.getTail()).isInstanceOf(IllegalStateException.class);

        final String abc = "abc";
        final String bcd = "bcd";

        add(list, abc);
        assertThat(list.getTail()).isSameAs(abc);

        add(list, bcd);
        assertThat(list.getTail()).isSameAs(bcd);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final U list = createStringList();

        final String abc = "abc";

        assertThat(list.getNumElements()).isEqualTo(0L);

        add(list, abc);
        assertThat(list.getNumElements()).isEqualTo(1L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final U list = createStringList();

        final String abc = "abc";

        assertThat(list.isEmpty()).isTrue();

        add(list, abc);
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testInitialCapacity() {

        final int initialCapacity = 123;

        final U list = createStringList(initialCapacity);

        assertThat(list.getCapacity()).isEqualTo(initialCapacity);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetCapacity() {

        final U list = createStringList();

        final long initialCapacity = list.getCapacity();

        int i;

        for (i = 0; i < initialCapacity; ++ i) {

            final String instance = String.valueOf(i);

            add(list, instance);
        }

        final String instance = String.valueOf(i);

        add(list, instance);

        assertThat(list.getCapacity()).isEqualTo(initialCapacity * 2);
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        final U list = createStringList();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        add(list, abc);
        checkElementsSameAs(list, abc);

        list.clear();
        checkNumElements(list, 0);

        add(list, abc);
        add(list, bcd);
        checkElementsSameAs(list, abc, bcd);

        list.clear();
        checkNumElements(list, 0);

        add(list, abc);
        add(list, bcd);
        add(list, cde);
        checkElementsSameAs(list, abc, bcd, cde);

        list.clear();
        checkNumElements(list, 0);
    }

    @SafeVarargs
    private final <E, L extends BaseArrayList<E>> void checkElementsSameAs(L list, E ... expectedElements) {

        final int numElements = expectedElements.length;

        checkNumElements(list, numElements);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(list.get(i)).isSameAs(expectedElements[i]);
        }
    }

    void checkNumElements(BaseArrayList<?> list, int expectedNumElements) {

        assertThat(list.isEmpty()).isEqualTo(expectedNumElements == 0);
        assertThat(list.getNumElements()).isEqualTo(expectedNumElements);
    }
}
