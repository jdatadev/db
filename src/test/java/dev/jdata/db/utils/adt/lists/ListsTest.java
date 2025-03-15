package dev.jdata.db.utils.adt.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.collections.BaseCollectionsTest;

public final class ListsTest extends BaseCollectionsTest<List<Integer>, List<String>> {

    @Test
    @Category(UnitTest.class)
    public void testUnmodifiableCopyOf() {

        assertThatThrownBy(() -> Lists.unmodifiableCopyOf(null)).isInstanceOf(NullPointerException.class);

        assertThat(Lists.unmodifiableCopyOf(Collections.emptyList())).isSameAs(Collections.emptyList());
        assertThat(Lists.unmodifiableCopyOf(new ArrayList<>())).isSameAs(Collections.emptyList());

        checkUnmodifiableCopyOf(0);
        checkUnmodifiableCopyOf(1, 2, 3);
    }

    private void checkUnmodifiableCopyOf(Integer ... values) {

        final List<Integer> list = Arrays.asList(values);

        final List<Integer> unmodifiableCopy = Lists.unmodifiableCopyOf(list);

        assertThat(unmodifiableCopy).isNotSameAs(list);
        assertThat(unmodifiableCopy).isEqualTo(list);

        assertThatThrownBy(() -> unmodifiableCopy.add(123)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testRemove() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected List<Integer> createCollection(Integer[] values) {

        return Arrays.asList(values);
    }

    @Override
    protected boolean isOrdered() {

        return true;
    }

    @Override
    protected <T> Collection<T> ofIntRange(int start, int numElements, IntFunction<T> mapper) {

        return Lists.ofIntRange(start, numElements, mapper);
    }

    @Override
    protected List<String> map(List<Integer> collection, Function<Integer, String> mapper) {

        return Lists.map(collection, mapper);
    }

    @Override
    protected List<String> filterAndMap(List<Integer> collection, Predicate<Integer> predicate, Function<Integer, String> mapper) {

        return Lists.filterAndMap(collection, predicate, mapper);
    }
}
