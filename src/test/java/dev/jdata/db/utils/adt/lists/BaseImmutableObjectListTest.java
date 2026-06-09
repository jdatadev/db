package dev.jdata.db.utils.adt.lists;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

abstract class BaseImmutableObjectListTest<T extends IList<Integer>, U extends IList<String>> extends BaseObjectListTest<T, U> {

    abstract U createStringList(String ... values);

    @Test
    @Category(UnitTest.class)
    public final void testGetHead() {

        final U emptyList = createStringList();

        assertThatThrownBy(() -> emptyList.getHead()).isInstanceOf(NoSuchElementException.class);

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

        assertThatThrownBy(() -> emptyList.getTail()).isInstanceOf(NoSuchElementException.class);

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
