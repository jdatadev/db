package dev.jdata.db.utils.adt.lists;

import java.util.function.BiPredicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

abstract class BaseMutableObjectArrayListTest<T extends IIndexListView<Integer>, U extends BaseObjectArrayList<String>> extends BaseMutableObjectListTest<T, U> {

    @Test
    @Category(UnitTest.class)
    public final void testArrayListAddTailVarargsMany() {

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

            assertThat(list.get(i)).isSameAs(instance1);
            assertThat(list.get(i + 1)).isSameAs(instance2);

            for (int j = 0; j <= i; ++ j) {

                assertThat(list.get(j)).isSameAs(array[j]);
            }
        }
    }

    @Category(UnitTest.class)
    public final void testArrayListAddTailMany() {

        checkArrayListAddTailMany((l, s) -> {

            addTail(l, s);

            return true;
        });
    }

    protected final void checkArrayListAddTailMany(BiPredicate<U, String> listTailAdder) {

        final U list = createStringList();

        checkArrayListAddTailMany(list, listTailAdder);
    }

    protected final void checkArrayListAddTailMany(U list, BiPredicate<U, String> listTailAdder) {

        final int numElements = IOnlyElementsView.intNumElements(list);

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

    @Override
    protected final boolean hasCapacity() {

        return true;
    }
}
