package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

abstract class BaseImmutableObjectArrayListTest<T extends IList<Integer>, U extends ObjectIndexList<String>> extends BaseImmutableObjectListTest<T, U> {

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
}
