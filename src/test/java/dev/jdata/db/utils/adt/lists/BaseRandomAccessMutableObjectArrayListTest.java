package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public abstract class BaseRandomAccessMutableObjectArrayListTest<T, U extends BaseObjectArrayList<String>> extends BaseMutableObjectArrayListTest<T, U> {

    protected abstract void addHead(U list, String string);

    @Test
    @Category(UnitTest.class)
    public final void testAddHead() {

        final U list = createStringList();

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        addHead(list, abc);
        checkElementsSameAs(list, abc);

        addHead(list, bcd);
        checkElementsSameAs(list, bcd, abc);

        addHead(list, cde);
        checkElementsSameAs(list, cde, bcd, abc);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddHeadMany() {

        final U list = createStringList();

        final int numToAdd = 10 * 1000;

        final String[] array = new String[numToAdd];

        for (int i = 0; i < numToAdd; ++ i) {

            final String instance = String.valueOf(i);

            addHead(list, instance);

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
    public final void testRemoveExactlyOneInstance() {

        checkArguments((l, i) -> { l.removeAtMostOneInstance(i); return true; });

        final Integer integer = 123;

        assertThatThrownBy(() -> makeList().removeExactlyOneInstance(integer)).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(() -> makeList(0, 1).removeExactlyOneInstance(integer)).isInstanceOf(NoSuchElementException.class);

        checkRemoveInstance((l, i) -> { l.removeAtMostOneInstance(i); return true; });
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveAtMostOneInstance() {

        checkArguments(MutableObjectIndexList::removeAtMostOneInstance);

        final Integer integer = 123;

        assertThat(makeList().removeAtMostOneInstance(integer)).isFalse();
        assertThat(makeList(0, 1).removeAtMostOneInstance(integer)).isFalse();

        checkRemoveInstance(MutableObjectIndexList::removeAtMostOneInstance);
    }

    @FunctionalInterface
    private interface InstanceRemover<T> {

        boolean remove(MutableObjectIndexList<T> list, T instance);
    }

    private static void checkArguments(InstanceRemover<Integer> remover) {

        final Integer integer = 123;

        assertThatThrownBy(() -> remover.remove(makeList(0, integer, 0, integer), integer)).isInstanceOf(IllegalStateException.class);
    }

    private static void checkRemoveInstance(InstanceRemover<Integer> remover) {

        final Integer integer = 123;

        assertThat(remover.remove(makeList(integer), integer)).isTrue();
        assertThat(remover.remove(makeList(integer, 0, 1), integer)).isTrue();
        assertThat(remover.remove(makeList(0, integer, 1), integer)).isTrue();
        assertThat(remover.remove(makeList(0, 1, integer), integer)).isTrue();
    }

    private static MutableObjectIndexList<Integer> makeList(int ... elements) {

        final MutableObjectIndexList<Integer> list = HeapMutableObjectIndexList.create(AllocationType.HEAP, elements.length, Integer[]::new);

        for (int element : elements) {

            list.addTail(element);
        }

        return list;
    }
}
