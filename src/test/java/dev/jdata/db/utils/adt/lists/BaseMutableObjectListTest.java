package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;
import java.util.function.BiPredicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class BaseMutableObjectListTest<T extends IListView<Integer>, U extends IListView<String>> extends BaseObjectListTest<T, U> {

    protected abstract boolean hasCapacity();

    protected abstract U createStringList();
    protected abstract U createStringList(int initialCapacity);

    protected abstract long getCapacity(U list);

    protected abstract void add(U list, String string);
    protected abstract void addTail(U list, String string);
    protected abstract void addTail(U list, String ... strings);

    protected abstract void clear(U list);

    @Test
    @Category(UnitTest.class)
    public final void testAddTail() {

        checkAddTail((l, s) -> {

            addTail(l, s);

            return true;
        });
    }

    protected final void checkAddTail(BiPredicate<U, String> listTailAdder) {

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

        assertThatThrownBy(() -> addTail(list, new String[0])).isInstanceOf(IllegalArgumentException.class);

        assertThat(list).isEmpty();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";
        final String def = "def";
        final String efg = "efg";

        addTail(list, abc, bcd);
        checkElementsSameAs(list, abc, bcd);

        addTail(list, cde, def, efg);
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

            addTail(list, instance1, instance2);

            array[i] = instance1;
            array[i + 1] = instance2;

            checkNumElements(list, i + 2);

            final String[] listArray = list.toArray(String[]::new);

            assertThat(listArray[i]).isSameAs(instance1);
            assertThat(listArray[i + 1]).isSameAs(instance2);

            for (int j = 0; j <= i; ++ j) {

                assertThat(listArray[j] ).isSameAs(array[j]);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddTailMany() {

        checkAddTailMany((l, s) -> {

            addTail(l, s);

            return true;
        });
    }

    private void checkAddTailMany(BiPredicate<U, String> listTailAdder) {

        final U list = createStringList();

        checkAddTailMany(list, listTailAdder);
    }

    private void checkAddTailMany(U list, BiPredicate<U, String> listTailAdder) {

        final int numElements = IOnlyElementsView.intNumElements(list);

        final int numToAdd = 10 * 1000;

        final String[] array = new String[numToAdd];

        for (int i = numElements; i < numToAdd; ++ i) {

            final String instance = String.valueOf(i);

            assertThat(listTailAdder.test(list, instance)).isTrue();

            array[i] = instance;

            checkNumElements(list, i + 1);

            final String[] listArray = list.toArray(String[]::new);

            assertThat(listArray[i]).isSameAs(instance);

            for (int j = numElements; j <= i; ++ j) {

                assertThat(listArray[j]).isSameAs(array[j]);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetHead() {

        final U list = createStringList();

        assertThatThrownBy(() -> list.getHead()).isInstanceOf(NoSuchElementException.class);

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

        assertThatThrownBy(() -> list.getTail()).isInstanceOf(NoSuchElementException.class);

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

        assertThat(getCapacity(list)).isEqualTo(hasCapacity() ? initialCapacity : Long.MAX_VALUE);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetCapacity() {

        final U list = createStringList();

        if (hasCapacity()) {

            final long initialCapacity = getCapacity(list);

            int i;

            for (i = 0; i < initialCapacity; ++ i) {

                final String instance = String.valueOf(i);

                add(list, instance);
            }

            final String instance = String.valueOf(i);

            add(list, instance);

            assertThat(getCapacity(list)).isEqualTo(initialCapacity * 2);
        }
        else {
            for (int i = 0; i < 1000; ++ i) {

                final String instance = String.valueOf(i);

                add(list, instance);

                assertThat(getCapacity(list)).isEqualTo(Long.MAX_VALUE);
            }
        }
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

        clear(list);
        checkNumElements(list, 0);

        add(list, abc);
        add(list, bcd);
        checkElementsSameAs(list, abc, bcd);

        clear(list);
        checkNumElements(list, 0);

        add(list, abc);
        add(list, bcd);
        add(list, cde);
        checkElementsSameAs(list, abc, bcd, cde);

        clear(list);
        checkNumElements(list, 0);
    }
}
