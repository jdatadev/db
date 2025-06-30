package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

abstract class BaseImmutableObjectArrayListTest<T, U extends BaseObjectArrayList<String>> extends BaseObjectArrayListTest<T> {

    abstract U createStringList(String ... values);

    @Test
    @Category(UnitTest.class)
    public final void testGet() {

        final String abc = "abc";
        final String bcd = "bcd";
        final String cde = "cde";

        final U emptyList = createStringList();

        assertThatThrownBy(() -> emptyList.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> emptyList.get(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> emptyList.get(1)).isInstanceOf(IndexOutOfBoundsException.class);

        final U oneElementList = createStringList(abc);

        checkElementsSameAs(oneElementList, abc);
        assertThatThrownBy(() -> oneElementList.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> oneElementList.get(1)).isInstanceOf(IndexOutOfBoundsException.class);

        final U twoElementsList = createStringList(abc, bcd);

        checkElementsSameAs(twoElementsList, abc, bcd);
        assertThatThrownBy(() -> twoElementsList.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> twoElementsList.get(2)).isInstanceOf(IndexOutOfBoundsException.class);

        final U threeElementsList = createStringList(abc, bcd, cde);

        checkElementsSameAs(threeElementsList, abc, bcd, cde);
        assertThatThrownBy(() -> threeElementsList.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> threeElementsList.get(3)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetHead() {

        final U emptyList = createStringList();

        assertThatThrownBy(() -> emptyList.getHead()).isInstanceOf(IllegalStateException.class);

        final String abc = "abc";
        final String bcd = "bcd";

        final U oneElementList = createStringList(abc);

        assertThat(oneElementList.getHead()).isSameAs(abc);

        final U twoElementsList = createStringList(abc, bcd);

        assertThat(twoElementsList.getHead()).isSameAs(abc);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetTail() {

        final U emptyList = createStringList();

        assertThatThrownBy(() -> emptyList.getTail()).isInstanceOf(IllegalStateException.class);

        final String abc = "abc";
        final String bcd = "bcd";

        final U oneElementList = createStringList(abc);

        assertThat(oneElementList.getTail()).isSameAs(abc);

        final U twoElementsList = createStringList(abc, bcd);

        assertThat(twoElementsList.getTail()).isSameAs(bcd);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final U emptyList = createStringList();

        assertThat(emptyList.getNumElements()).isEqualTo(0L);

        final String abc = "abc";

        final U oneElementList = createStringList(abc);

        assertThat(oneElementList.getNumElements()).isEqualTo(1L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final U emptyList = createStringList();

        assertThat(emptyList.isEmpty()).isTrue();

        final String abc = "abc";

        final U oneElementList = createStringList(abc);

        assertThat(oneElementList.isEmpty()).isFalse();
    }
}
